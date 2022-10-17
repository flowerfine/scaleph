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
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
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
public class FlinkYarnServiceImpl implements FlinkYarnService {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private FlinkJobService flinkJobService;
    @Autowired
    private FlinkJobInstanceService flinkJobInstanceService;
    @Autowired
    private FlinkArtifactJarService flinkArtifactJarService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;
    @Autowired
    private ClusterCredentialService clusterCredentialService;
    @Autowired
    private FlinkClusterInstanceService flinkClusterInstanceService;

    @Override
    public void createSessionCluster(FlinkSessionClusterAddParam param) throws Exception {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(param.getFlinkClusterConfigId());
        ClusterClient clusterClient = createYarnSessionCluster(flinkClusterConfigDTO);

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setFlinkClusterConfigId(flinkClusterConfigDTO.getId());
        dto.setName(flinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(FlinkClusterStatus.RUNNING);
        dto.setRemark(param.getRemark());
        flinkClusterInstanceService.insert(dto);
    }

    private ClusterClient createYarnSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
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

    private Path loadFlinkArtifactJar(FlinkArtifactJarDTO flinkArtifactJarDTO, Path workspace) throws IOException {
        final Path tempDir = FileUtil.createDir(workspace, flinkArtifactJarDTO.getFlinkArtifact().getName() + "/" + flinkArtifactJarDTO.getVersion());
        final Path jarPath = FileUtil.createFile(tempDir, flinkArtifactJarDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(jarPath, StandardOpenOption.WRITE)) {
            flinkArtifactJarService.download(flinkArtifactJarDTO.getId(), outputStream);
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
    private Configuration buildConfiguration(FlinkClusterConfigDTO flinkClusterConfigDTO, Path clusterCredentialPath) throws IOException {
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(flinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(flinkClusterConfigDTO.getConfigOptions());
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
            FlinkJobForJarDTO flinkJobForJarDTO = flinkJobService.getJobForJarById(id);
            ClusterClient clusterClient = doSubmitJar(flinkJobForJarDTO, workspace);
            recordJobs(flinkJobForJarDTO, clusterClient);
        } finally {
            FileUtil.deleteDir(workspace);
        }
    }

    public ClusterClient doSubmitJar(FlinkJobForJarDTO flinkJobForJarDTO, Path workspace) throws Exception {
        FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkJobForJarDTO.getFlinkClusterConfig().getId());
        Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(flinkJobForJarDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(flinkJobForJarDTO.getFlinkConfig()));
        }

        FlinkArtifactJarDTO flinkArtifactJar = flinkArtifactJarService.selectOne(flinkJobForJarDTO.getFlinkArtifactJar().getId());
        Path flinkArtifactJarPath = loadFlinkArtifactJar(flinkArtifactJar, workspace);
        PackageJarJob packageJarJob = buildJarJob(flinkJobForJarDTO, flinkArtifactJar, flinkArtifactJarPath);
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, Collections.singletonList(flinkArtifactJarPath.toFile().toURL()), Object::toString);

        CliClient client = new DescriptorCliClient();
        switch (flinkClusterConfigDTO.getDeployMode()) {
            case APPLICATION:
                return client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
                return client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            case SESSION:
                configuration.setString(YarnConfigOptions.APPLICATION_ID, flinkJobForJarDTO.getFlinkClusterInstance().getClusterId());
                return client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            default:
                throw new IllegalStateException("unknown flink deploy mode for " + flinkClusterConfigDTO.getDeployMode());
        }
    }

    private void recordJobs(FlinkJobForJarDTO flinkJobForJarDTO, ClusterClient clusterClient) throws Exception {
        Collection<JobStatusMessage> jobs = (Collection<JobStatusMessage>) clusterClient.listJobs().get();
        for (JobStatusMessage job : jobs) {
            FlinkJobInstanceDTO flinkJobInstanceDTO = new FlinkJobInstanceDTO();
            flinkJobInstanceDTO.setFlinkJobCode(flinkJobForJarDTO.getCode());
            flinkJobInstanceDTO.setFlinkJobVersion(flinkJobForJarDTO.getVersion());
            flinkJobInstanceDTO.setJobId(job.getJobId().toHexString());
            flinkJobInstanceDTO.setJobName(job.getJobName());
            flinkJobInstanceDTO.setJobState(FlinkJobState.of(job.getJobState().name()));
            flinkJobInstanceDTO.setClusterId(clusterClient.getClusterId().toString());
            flinkJobInstanceDTO.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
            flinkJobInstanceDTO.setClusterStatus(FlinkClusterStatus.RUNNING);
            flinkJobInstanceService.upsert(flinkJobInstanceDTO);
        }
    }

    private PackageJarJob buildJarJob(FlinkJobForJarDTO flinkJobForJarDTO, FlinkArtifactJarDTO flinkArtifactJar, Path flinkArtifactJarPath) throws MalformedURLException {
        PackageJarJob packageJarJob = new PackageJarJob();
        packageJarJob.setJarFilePath(flinkArtifactJarPath.toFile().toURL().toString());
        packageJarJob.setEntryPointClass(flinkArtifactJar.getEntryClass());
        if (CollectionUtils.isEmpty(flinkJobForJarDTO.getJobConfig()) == false) {
            List<String> args = new ArrayList<>(flinkJobForJarDTO.getJobConfig().size() * 2);
            for (Map.Entry<String, String> entry : flinkJobForJarDTO.getJobConfig().entrySet()) {
                args.add("--" + entry.getKey());
                args.add(entry.getValue());
            }
            packageJarJob.setProgramArgs(args.stream().toArray(length -> new String[length]));
        }
        return packageJarJob;
    }

}
