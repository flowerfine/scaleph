/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.SessionClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.*;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.system.util.SystemUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

@Service
public class WsFlinkYarnServiceImpl implements WsFlinkYarnService {

    @Autowired
    private WsFlinkClusterConfigService wsFlinkClusterConfigService;
    @Autowired
    private WsFlinkJobService wsFlinkJobService;
    @Autowired
    private WsFlinkJobInstanceService wsFlinkJobInstanceService;
    @Autowired
    private WsFlinkArtifactJarService wsFlinkArtifactJarService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;
    @Autowired
    private ClusterCredentialService clusterCredentialService;
    @Autowired
    private WsFlinkClusterInstanceService wsFlinkClusterInstanceService;

    @Override
    public void createSessionCluster(Long flinkClusterConfigId) throws Exception {
        final WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(flinkClusterConfigId);
        ClusterClient clusterClient = createYarnSessionCluster(wsFlinkClusterConfigDTO);

        WsFlinkClusterInstanceDTO dto = new WsFlinkClusterInstanceDTO();
        dto.setFlinkClusterConfigId(wsFlinkClusterConfigDTO.getId());
        dto.setName(wsFlinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(FlinkClusterStatus.RUNNING);
        wsFlinkClusterInstanceService.insert(dto);
    }

    private ClusterClient createYarnSessionCluster(WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkHomePath = loadFlinkRelease(wsFlinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path clusterCredentialPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, clusterCredentialPath);
        ClusterClient<ApplicationId> clusterClient = SessionClient.create(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration);
        FileUtils.deleteDirectory(workspace.toFile());
        return clusterClient;
    }

    private Path getWorkspace() throws IOException {
        return SystemUtil.getRandomWorkspace();
    }

    private Path loadFlinkRelease(FlinkReleaseDTO flinkRelease, Path workspace) throws IOException {
        final Path tempFile = FileUtil.createFile(workspace, flinkRelease.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
            flinkReleaseService.download(flinkRelease.getId(), outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        return FileUtil.listFiles(untarDir).get(0);
    }

    private Path loadClusterCredential(ClusterCredentialDTO clusterCredential, Path workspace) throws IOException {
        final List<FileStatusVO> fileStatusVOS = clusterCredentialService.listCredentialFile(clusterCredential.getId());
        final Path tempDir = FileUtil.createDir(workspace, clusterCredential.getName());
        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            final Path deployConfigFile = tempDir.resolve(fileStatusVO.getName());
            Files.createFile(deployConfigFile, FileUtil.ATTRIBUTES);
            try (final OutputStream outputStream = Files.newOutputStream(deployConfigFile, StandardOpenOption.WRITE)) {
                clusterCredentialService.downloadCredentialFile(clusterCredential.getId(), fileStatusVO.getName(), outputStream);
            }
        }
        return tempDir;
    }

    private Path loadFlinkArtifactJar(WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO, Path workspace) throws IOException {
        final Path tempDir = FileUtil.createDir(workspace, wsFlinkArtifactJarDTO.getFlinkArtifact().getName() + "/" + wsFlinkArtifactJarDTO.getVersion());
        final Path jarPath = FileUtil.createFile(tempDir, wsFlinkArtifactJarDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(jarPath, StandardOpenOption.WRITE)) {
            wsFlinkArtifactJarService.download(wsFlinkArtifactJarDTO.getId(), outputStream);
        }
        return jarPath;
    }

    /**
     * hadoop conf
     * kubeconfig
     * flink-conf.yaml
     *
     * @see cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider
     */
    private Configuration buildConfiguration(WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO, Path clusterCredentialPath) throws IOException {
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(wsFlinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(wsFlinkClusterConfigDTO.getConfigOptions());
        }
        return buildYarnConfiguration(dynamicProperties, clusterCredentialPath);
    }

    private Configuration buildYarnConfiguration(Configuration dynamicProperties, Path clusterCredentialPath) {
        dynamicProperties.set(CoreOptions.FLINK_HADOOP_CONF_DIR, clusterCredentialPath.toAbsolutePath().toString());
        if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }


    @Override
    public void submitJar(Long id) throws Exception {
        final Path workspace = getWorkspace();
        try {
            WsFlinkJobForJarDTO wsFlinkJobForJarDTO = wsFlinkJobService.getJobForJarById(id);
            ClusterClient clusterClient = doSubmitJar(wsFlinkJobForJarDTO, workspace);
            recordJobs(wsFlinkJobForJarDTO, clusterClient);
        } finally {
            FileUtil.deleteDir(workspace);
        }
    }

    public ClusterClient doSubmitJar(WsFlinkJobForJarDTO wsFlinkJobForJarDTO, Path workspace) throws Exception {
        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(wsFlinkJobForJarDTO.getFlinkClusterConfig().getId());
        Path flinkHomePath = loadFlinkRelease(wsFlinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path clusterCredentialPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(wsFlinkJobForJarDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(wsFlinkJobForJarDTO.getFlinkConfig()));
        }

        WsFlinkArtifactJarDTO flinkArtifactJar = wsFlinkArtifactJarService.selectOne(wsFlinkJobForJarDTO.getFlinkArtifactJar().getId());
        Path flinkArtifactJarPath = loadFlinkArtifactJar(flinkArtifactJar, workspace);
        PackageJarJob packageJarJob = buildJarJob(wsFlinkJobForJarDTO, flinkArtifactJar, flinkArtifactJarPath);
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(flinkArtifactJarPath.toFile().toURL()), Object::toString);

        CliClient client = new DescriptorCliClient();
        switch (wsFlinkClusterConfigDTO.getDeployMode()) {
            case APPLICATION:
                return client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
                return client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            case SESSION:
                configuration.setString(YarnConfigOptions.APPLICATION_ID, wsFlinkJobForJarDTO.getFlinkClusterInstance().getClusterId());
                return client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            default:
                throw new IllegalStateException("unknown flink deploy mode for " + wsFlinkClusterConfigDTO.getDeployMode());
        }
    }

    private void recordJobs(WsFlinkJobForJarDTO wsFlinkJobForJarDTO, ClusterClient clusterClient) throws Exception {
        Collection<JobStatusMessage> jobs = (Collection<JobStatusMessage>) clusterClient.listJobs().get();
        for (JobStatusMessage job : jobs) {
            WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = new WsFlinkJobInstanceDTO();
            wsFlinkJobInstanceDTO.setFlinkJobCode(wsFlinkJobForJarDTO.getCode());
            wsFlinkJobInstanceDTO.setJobId(job.getJobId().toHexString());
            wsFlinkJobInstanceDTO.setJobName(job.getJobName());
            wsFlinkJobInstanceDTO.setJobState(FlinkJobState.of(job.getJobState().name()));
            wsFlinkJobInstanceDTO.setClusterId(clusterClient.getClusterId().toString());
            wsFlinkJobInstanceDTO.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
            wsFlinkJobInstanceDTO.setClusterStatus(FlinkClusterStatus.RUNNING);
            wsFlinkJobInstanceService.upsert(wsFlinkJobInstanceDTO);
        }
    }

    private PackageJarJob buildJarJob(WsFlinkJobForJarDTO wsFlinkJobForJarDTO, WsFlinkArtifactJarDTO flinkArtifactJar, Path flinkArtifactJarPath) throws MalformedURLException {
        PackageJarJob packageJarJob = new PackageJarJob();
        packageJarJob.setJarFilePath(flinkArtifactJarPath.toFile().toURL().toString());
        packageJarJob.setEntryPointClass(flinkArtifactJar.getEntryClass());
        if (CollectionUtils.isEmpty(wsFlinkJobForJarDTO.getJobConfig()) == false) {
            List<String> args = new ArrayList<>(wsFlinkJobForJarDTO.getJobConfig().size() * 2);
            for (Map.Entry<String, String> entry : wsFlinkJobForJarDTO.getJobConfig().entrySet()) {
                args.add("--" + entry.getKey());
                args.add(entry.getValue());
            }
            packageJarJob.setProgramArgs(args.stream().toArray(length -> new String[length]));
        }
        return packageJarJob;
    }

}
