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
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.enums.DeployMode;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.*;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class FlinkServiceImpl implements FlinkService {

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

    /**
     * requires:
     * 1. flink release
     * 2. resource provider certificate: hadoop conf or kubeconfig
     * 3. flink options
     */
    @Override
    public void createSessionCluster(FlinkSessionClusterAddParam param) throws Exception {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(param.getFlinkClusterConfigId());
        final FlinkResourceProvider resourceProvider = flinkClusterConfigDTO.getResourceProvider();
        ClusterClient clusterClient;
        switch (resourceProvider) {
            case YARN:
                clusterClient = createYarnSessionCluster(flinkClusterConfigDTO);
                break;
            case NATIVE_KUBERNETES:
                clusterClient = createKubernetesSessionCluster(flinkClusterConfigDTO);
                break;
            case STANDALONE:
                clusterClient = createExistingSessionCluster(flinkClusterConfigDTO);
                break;
            default:
                throw new UnsupportedOperationException("unknown resource provider for " + resourceProvider);
        }

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setFlinkClusterConfigId(flinkClusterConfigDTO.getId());
        dto.setName(flinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(FlinkClusterStatus.RUNNING);
        dto.setRemark(param.getRemark());
        flinkClusterInstanceService.insert(dto);
    }

    @Override
    public void submitJar(Long id) throws Exception {
        final Path workspace = getWorkspace();
        try {
            FlinkJobForJarDTO flinkJobForJarDTO = flinkJobService.getJobForJarById(id);
            ClusterClient clusterClient = doSubmitJar(flinkJobForJarDTO, workspace);
            recordJobs(flinkJobForJarDTO, clusterClient);
        } finally {
            TempFileUtil.deleteDir(workspace);
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

        CliClient client = new DescriptorCliClient();
        if (flinkClusterConfigDTO.getResourceProvider().getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                return client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                return client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            } else {
                configuration.setString(YarnConfigOptions.APPLICATION_ID, flinkJobForJarDTO.getFlinkClusterInstance().getClusterId());
                return client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            }
        } else if (flinkClusterConfigDTO.getResourceProvider().getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Application mode for native kubernetes");
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                throw new UnsupportedOperationException("flink not supports Per-Job mode for native kubernetes");
            } else {
                configuration.setString(KubernetesConfigOptions.CLUSTER_ID, flinkJobForJarDTO.getFlinkClusterInstance().getClusterId());
                return client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration, packageJarJob);
            }
        } else {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Application mode for standalone");
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Per-Job mode for standalone");
            } else {
                URL url = new URL(flinkJobForJarDTO.getFlinkClusterInstance().getWebInterfaceUrl());
                configuration.setString(RestOptions.ADDRESS, url.getHost());
                configuration.setInteger(RestOptions.PORT, url.getPort());
                return client.submit(DeploymentTarget.STANDALONE_SESSION, flinkHomePath, configuration, packageJarJob);
            }
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
            flinkJobInstanceDTO.setClusterId(clusterClient.getClusterId());
            flinkJobInstanceDTO.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
            flinkJobInstanceDTO.setClusterStatus(FlinkClusterStatus.RUNNING);
            flinkJobInstanceService.upsert(flinkJobInstanceDTO);
        }
    }

    @Override
    public void shutdown(Long id) throws Exception {
        final FlinkClusterInstanceDTO flinkClusterInstanceDTO = flinkClusterInstanceService.selectOne(id);
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkClusterInstanceDTO.getFlinkClusterConfigId());
        final Path workspace = getWorkspace();
        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        buildConfiguration(flinkClusterInstanceDTO.getClusterId(), flinkClusterConfigDTO.getResourceProvider(), flinkClusterConfigDTO.getDeployMode(), configuration);

        ClusterClient client = FlinkUtil.retrieve(configuration);
        client.shutDownCluster();

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setId(id);
        dto.setStatus(FlinkClusterStatus.STOPED);
        flinkClusterInstanceService.update(dto);
    }


    @Override
    public void shutdownBatch(List<Long> ids) throws Exception {
        for (Long id : ids) {
            shutdown(id);
        }
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

    private ClusterClient createKubernetesSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path flinkDeployConfigPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, flinkDeployConfigPath);
        ClusterClient<String> clusterClient = SessionClient.create(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration);
        FileUtils.deleteDirectory(workspace.toFile());
        return clusterClient;
    }

    private ClusterClient createExistingSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkDeployConfigPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, flinkDeployConfigPath);
        FileUtils.deleteDirectory(workspace.toFile());
        return new RestClusterClient(configuration, StandaloneClusterId.getInstance());
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

        final ClusterCredentialDTO clusterCredentialDTO = flinkClusterConfigDTO.getClusterCredential();
        final FlinkResourceProvider resourceProvider = clusterCredentialDTO.getConfigType();
        switch (resourceProvider) {
            case YARN:
                return buildYarnConfiguration(dynamicProperties, clusterCredentialPath);
            case NATIVE_KUBERNETES:
                return buildKubernetesConfiguration(dynamicProperties, clusterCredentialPath);
            case STANDALONE:
                return buildStandaloneConfiguration(dynamicProperties, clusterCredentialPath);
            default:
                return dynamicProperties;
        }
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

    private Configuration buildKubernetesConfiguration(Configuration dynamicProperties, Path clusterCredentialPath) throws IOException {
        final List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
        checkState(CollectionUtils.isEmpty(childs) == false, () -> "Kubernetes kubeconfig can't be null");

        final Path kubeConfigFile = childs.get(0);
        dynamicProperties.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, kubeConfigFile.toAbsolutePath().toString());
        if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }

    private Configuration buildStandaloneConfiguration(Configuration dynamicProperties, Path clusterCredentialPath) throws IOException {
        final List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childs)) {
            return dynamicProperties;
        }
        return GlobalConfiguration.loadConfiguration(clusterCredentialPath.toString(), dynamicProperties);
    }

    private void buildConfiguration(String clusterId, FlinkResourceProvider resourceProvider, FlinkDeploymentMode deployMode, Configuration configuration) throws IOException {
        switch (resourceProvider) {
            case YARN:
                configuration.setString(YarnConfigOptions.APPLICATION_ID, clusterId);
                switch (deployMode) {
                    case APPLICATION:
                        DeploymentTarget.YARN_APPLICATION.apply(configuration);
                        break;
                    case PER_JOB:
                        DeploymentTarget.YARN_PER_JOB.apply(configuration);
                        break;
                    case SESSION:
                        DeploymentTarget.YARN_SESSION.apply(configuration);
                        break;
                    default:
                }
                break;
            case NATIVE_KUBERNETES:
                configuration.setString(KubernetesConfigOptions.CLUSTER_ID, clusterId);
                switch (deployMode) {
                    case APPLICATION:
                        DeploymentTarget.NATIVE_KUBERNETES_APPLICATION.apply(configuration);
                        break;
                    case SESSION:
                        DeploymentTarget.NATIVE_KUBERNETES_SESSION.apply(configuration);
                        break;
                    default:
                }
                break;
            case STANDALONE:
                break;
            default:
        }
    }

    private PackageJarJob buildJarJob(FlinkJobForJarDTO flinkJobForJarDTO, FlinkArtifactJarDTO flinkArtifactJar, Path flinkArtifactJarPath) {
        PackageJarJob packageJarJob = new PackageJarJob();
        packageJarJob.setJarFilePath(flinkArtifactJarPath.toUri().toString());
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


    private Path getWorkspace() throws IOException {
        return TempFileUtil.createTempDir();
    }

    private Path loadFlinkRelease(FlinkReleaseDTO flinkRelease, Path workspace) throws IOException {
        final Path tempFile = TempFileUtil.createTempFile(workspace, flinkRelease.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
            flinkReleaseService.download(flinkRelease.getId(), outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        return Files.list(untarDir).collect(Collectors.toList()).get(0);
    }

    private Path loadClusterCredential(ClusterCredentialDTO clusterCredential, Path workspace) throws IOException {
        final List<FileStatusVO> fileStatusVOS = clusterCredentialService.listCredentialFile(clusterCredential.getId());
        final Path tempDir = TempFileUtil.createTempDir(workspace, clusterCredential.getName());
        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            final Path deployConfigFile = tempDir.resolve(fileStatusVO.getName());
            Files.createFile(deployConfigFile, TempFileUtil.attributes);
            try (final OutputStream outputStream = Files.newOutputStream(deployConfigFile, StandardOpenOption.WRITE)) {
                clusterCredentialService.downloadCredentialFile(clusterCredential.getId(), fileStatusVO.getName(), outputStream);
            }
        }
        return tempDir;
    }

    private Path loadFlinkArtifactJar(FlinkArtifactJarDTO flinkArtifactJarDTO, Path workspace) throws IOException {
        final Path tempDir = TempFileUtil.createTempDir(workspace, flinkArtifactJarDTO.getFlinkArtifact().getName() + "/" + flinkArtifactJarDTO.getVersion());
        final Path jarPath = TempFileUtil.createTempFile(tempDir, flinkArtifactJarDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(jarPath, StandardOpenOption.WRITE)) {
            flinkArtifactJarService.download(flinkArtifactJarDTO.getId(), outputStream);
        }
        return jarPath;
    }

}
