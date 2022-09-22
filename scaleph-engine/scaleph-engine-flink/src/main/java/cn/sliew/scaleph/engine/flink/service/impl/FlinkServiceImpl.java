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
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.DeployMode;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.engine.flink.enums.FlinkClusterStatus;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobConfigJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.client.deployment.StandaloneClusterId;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.rest.RestClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinkServiceImpl implements FlinkService {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private FlinkJobConfigJarService flinkJobConfigJarService;
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
        final DictVO resourceProvider = flinkClusterConfigDTO.getResourceProvider();
        ClusterClient clusterClient;
        if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            clusterClient = createYarnSessionCluster(flinkClusterConfigDTO);
        } else if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            clusterClient = createKubernetesSessionCluster(flinkClusterConfigDTO);
        } else {
            clusterClient = createExistingSessionCluster(flinkClusterConfigDTO);
        }

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setFlinkClusterConfigId(flinkClusterConfigDTO.getId());
        dto.setName(flinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(DictVO.toVO(DictConstants.FLINK_CLUSTER_STATUS, String.valueOf(FlinkClusterStatus.RUNNING.getCode())));
        dto.setRemark(param.getRemark());
        flinkClusterInstanceService.insert(dto);
    }

    @Override
    public void submitJar(Long id) throws Exception {
        final Path workspace = getWorkspace();
        try {
            doSubmitJar(id, workspace);
        } finally {
            TempFileUtil.deleteDir(workspace);
        }
    }

    public void doSubmitJar(Long id, Path workspace) throws Exception {
        FlinkJobConfigJarDTO flinkJobConfigJarDTO = flinkJobConfigJarService.selectOne(id);
        FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkJobConfigJarDTO.getFlinkClusterConfig().getId());
        Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(flinkJobConfigJarDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(flinkJobConfigJarDTO.getFlinkConfig()));
        }

        FlinkArtifactJarDTO flinkArtifactJar = flinkArtifactJarService.selectOne(flinkJobConfigJarDTO.getFlinkArtifactJar().getId());
        Path flinkArtifactJarPath = loadFlinkArtifactJar(flinkArtifactJar, workspace);
        PackageJarJob packageJarJob = buildJarJob(flinkJobConfigJarDTO, flinkArtifactJar, flinkArtifactJarPath);

        CliClient client = new DescriptorCliClient();
        if (flinkClusterConfigDTO.getResourceProvider().getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            } else {
                configuration.setString(YarnConfigOptions.APPLICATION_ID, flinkJobConfigJarDTO.getFlinkClusterInstance().getClusterId());
                client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            }
        } else if (flinkClusterConfigDTO.getResourceProvider().getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Application mode for native kubernetes");
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                throw new UnsupportedOperationException("flink not supports Per-Job mode for native kubernetes");
            } else {
                configuration.setString(KubernetesConfigOptions.CLUSTER_ID, flinkJobConfigJarDTO.getFlinkClusterInstance().getClusterId());
                client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration, packageJarJob);
            }
        } else {
            if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Application mode for standalone");
            } else if (flinkClusterConfigDTO.getDeployMode().getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                throw new UnsupportedOperationException("scaleph not supports Per-Job mode for standalone");
            } else {
                URL url = new URL(flinkJobConfigJarDTO.getFlinkClusterInstance().getWebInterfaceUrl());
                configuration.setString(RestOptions.ADDRESS, url.getHost());
                configuration.setInteger(RestOptions.PORT, url.getPort());
                client.submit(DeploymentTarget.STANDALONE_SESSION, flinkHomePath, configuration, packageJarJob);
            }
        }
    }


    @Override
    public void shutdown(Long id) throws Exception {
        final FlinkClusterInstanceDTO flinkClusterInstanceDTO = flinkClusterInstanceService.selectOne(id);
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkClusterInstanceDTO.getFlinkClusterConfigId());
        final Path workspace = getWorkspace();
        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        final DictVO resourceProvider = flinkClusterConfigDTO.getResourceProvider();
        final DictVO deployMode = flinkClusterConfigDTO.getDeployMode();
        if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            configuration.setString(YarnConfigOptions.APPLICATION_ID, flinkClusterInstanceDTO.getClusterId());
            if (deployMode.getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                DeploymentTarget.YARN_APPLICATION.apply(configuration);
            } else if (deployMode.getValue().equals(String.valueOf(DeployMode.PER_JOB.getCode()))) {
                DeploymentTarget.YARN_PER_JOB.apply(configuration);
            } else if (deployMode.getValue().equals(String.valueOf(DeployMode.SESSION.getCode()))) {
                DeploymentTarget.YARN_SESSION.apply(configuration);
            }
        } else if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            configuration.setString(KubernetesConfigOptions.CLUSTER_ID, flinkClusterInstanceDTO.getClusterId());
            if (deployMode.getValue().equals(String.valueOf(DeployMode.APPLICATION.getCode()))) {
                DeploymentTarget.NATIVE_KUBERNETES_APPLICATION.apply(configuration);
            } else if (deployMode.getValue().equals(String.valueOf(DeployMode.SESSION.getCode()))) {
                DeploymentTarget.NATIVE_KUBERNETES_SESSION.apply(configuration);
            }
        } else {
            // standalone session
        }

        ClusterClient client = FlinkUtil.retrieve(configuration);
        client.shutDownCluster();

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setId(id);
        dto.setStatus(DictVO.toVO(DictConstants.FLINK_CLUSTER_STATUS, String.valueOf(FlinkClusterStatus.STOP.getCode())));
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

        if (clusterCredentialDTO.getConfigType().getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            dynamicProperties.set(CoreOptions.FLINK_HADOOP_CONF_DIR, clusterCredentialPath.toAbsolutePath().toString());
            if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
                dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            }
            if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
                dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            }
            return dynamicProperties;
        }

        if (clusterCredentialDTO.getConfigType().getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            final List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(childs)) {
                return dynamicProperties;
            }
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

        if (clusterCredentialDTO.getConfigType().getValue().equals(String.valueOf(ResourceProvider.STANDALONE.getCode()))) {
            final List<Path> childs = Files.list(clusterCredentialPath).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(childs)) {
                return dynamicProperties;
            }
            return GlobalConfiguration.loadConfiguration(clusterCredentialPath.toString(), dynamicProperties);
        }

        return dynamicProperties;
    }

    private PackageJarJob buildJarJob(FlinkJobConfigJarDTO flinkJobConfigJarDTO, FlinkArtifactJarDTO flinkArtifactJar, Path flinkArtifactJarPath) {
        PackageJarJob packageJarJob = new PackageJarJob();
        packageJarJob.setJarFilePath(flinkArtifactJarPath.toUri().toString());
        packageJarJob.setEntryPointClass(flinkArtifactJar.getEntryClass());
        if (CollectionUtils.isEmpty(flinkJobConfigJarDTO.getJobConfig()) == false) {
            List<String> args = new ArrayList<>(flinkJobConfigJarDTO.getJobConfig().size() * 2);
            for (Map.Entry<String, String> entry : flinkJobConfigJarDTO.getJobConfig().entrySet()) {
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
