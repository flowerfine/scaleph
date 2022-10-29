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
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.*;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.JarService;
import cn.sliew.scaleph.resource.service.SeaTunnelReleaseService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.resource.service.dto.JarDTO;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.system.util.SystemUtil;
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
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private FlinkJobService flinkJobService;
    @Autowired
    private FlinkJobInstanceService flinkJobInstanceService;
    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private FlinkClusterInstanceService flinkClusterInstanceService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;
    @Autowired
    private SeaTunnelReleaseService seaTunnelReleaseService;
    @Autowired
    private ClusterCredentialService clusterCredentialService;
    @Autowired
    private JarService jarService;
    @Autowired
    private FlinkArtifactJarService flinkArtifactJarService;
    @Autowired
    private DiJobService diJobService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;

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
            recordJobs(flinkJobForJarDTO.getCode(), flinkJobForJarDTO.getVersion(), clusterClient);
        } finally {
            FileUtil.deleteDir(workspace);
        }
    }

    private ClusterClient doSubmitJar(FlinkJobForJarDTO flinkJobForJarDTO, Path workspace) throws Exception {
        FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkJobForJarDTO.getFlinkClusterConfig().getId());

        Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);

        List<URL> jars = loadJarResources(flinkJobForJarDTO.getJars(), workspace);
        FlinkArtifactJarDTO flinkArtifactJar = flinkArtifactJarService.selectOne(flinkJobForJarDTO.getFlinkArtifactJar().getId());
        Path flinkArtifactJarPath = loadFlinkArtifactJar(flinkArtifactJar, workspace);
        jars.add(flinkArtifactJarPath.toFile().toURL());
        PackageJarJob packageJarJob = buildJarJob(flinkJobForJarDTO, flinkArtifactJar, flinkArtifactJarPath);

        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(flinkJobForJarDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(flinkJobForJarDTO.getFlinkConfig()));
        }
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);

        switch (flinkClusterConfigDTO.getResourceProvider()) {
            case YARN:
                return doSubmitToYARN(flinkJobForJarDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case NATIVE_KUBERNETES:
                return doSubmitToKubernetes(flinkJobForJarDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case STANDALONE:
                return doSubmitToStandalone(flinkJobForJarDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s for flink jar job submission", flinkClusterConfigDTO.getResourceProvider().getValue()));
        }
    }

    @Override
    public void submitSeaTunnel(Long id) throws Exception {
        final Path workspace = getWorkspace();
        try {
            FlinkJobForSeaTunnelDTO flinkJobForSeaTunnelDTO = flinkJobService.getJobForSeaTunnelById(id);
            ClusterClient clusterClient = doSubmitSeaTunnel(flinkJobForSeaTunnelDTO, workspace);
            recordJobs(flinkJobForSeaTunnelDTO.getCode(), flinkJobForSeaTunnelDTO.getVersion(), clusterClient);
        } finally {
            FileUtil.deleteDir(workspace);
        }
    }

    private ClusterClient doSubmitSeaTunnel(FlinkJobForSeaTunnelDTO flinkJobForSeaTunnelDTO, Path workspace) throws Exception {
        FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(flinkJobForSeaTunnelDTO.getFlinkClusterConfig().getId());
        SeaTunnelReleaseDTO seaTunnelRelease = seaTunnelReleaseService.selectByVersion(SeaTunnelVersion.V_2_2_0_BETA);

        Path flinkHomePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkRelease(), workspace);
        Path seatunnelHomePath = loadSeaTunnelRelease(seaTunnelRelease, workspace);

        List<URL> jars = loadJarResources(flinkJobForSeaTunnelDTO.getJars(), workspace);
        DiJobDTO diJobDTO = diJobService.queryJobGraph(flinkJobForSeaTunnelDTO.getFlinkArtifactSeaTunnel().getId());

        Path seatunnelConfPath = buildSeaTunnelConf(diJobDTO, workspace);
        PackageJarJob packageJarJob = buildSeaTunnelJob(seatunnelHomePath, seatunnelConfPath);
        jars.add(SeaTunnelReleaseUtil.getStarterJarPath(seatunnelHomePath).toFile().toURL());
        jars.addAll(loadSeaTunnelConnectors(seaTunnelRelease, diJobDTO, workspace));

        final Path clusterCredentialPath = loadClusterCredential(flinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(flinkJobForSeaTunnelDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(flinkJobForSeaTunnelDTO.getFlinkConfig()));
        }
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);

        switch (flinkClusterConfigDTO.getResourceProvider()) {
            case YARN:
                return doSubmitToYARN(flinkJobForSeaTunnelDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case NATIVE_KUBERNETES:
                return doSubmitToKubernetes(flinkJobForSeaTunnelDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case STANDALONE:
                return doSubmitToStandalone(flinkJobForSeaTunnelDTO.getFlinkClusterInstance(), flinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s for flink seatunnel job submission", flinkClusterConfigDTO.getResourceProvider().getValue()));
        }
    }

    private ClusterClient doSubmitToKubernetes(FlinkClusterInstanceDTO flinkClusterInstanceDTO, FlinkClusterConfigDTO flinkClusterConfigDTO,
                                               Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        switch (flinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                CliClient client = new DescriptorCliClient();
                configuration.setString(KubernetesConfigOptions.CLUSTER_ID, flinkClusterInstanceDTO.getClusterId());
                return client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
            case APPLICATION:
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for native kubernetes", flinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private ClusterClient doSubmitToYARN(FlinkClusterInstanceDTO flinkClusterInstanceDTO, FlinkClusterConfigDTO flinkClusterConfigDTO,
                                         Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        CliClient client = new DescriptorCliClient();
        switch (flinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                configuration.setString(YarnConfigOptions.APPLICATION_ID, flinkClusterInstanceDTO.getClusterId());
                return client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
                return client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            case APPLICATION:
                return client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for YARN", flinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private ClusterClient doSubmitToStandalone(FlinkClusterInstanceDTO flinkClusterInstanceDTO, FlinkClusterConfigDTO flinkClusterConfigDTO,
                                               Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        switch (flinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                URL url = new URL(flinkClusterInstanceDTO.getWebInterfaceUrl());
                configuration.setString(RestOptions.ADDRESS, url.getHost());
                configuration.setInteger(RestOptions.PORT, url.getPort());
                CliClient client = new DescriptorCliClient();
                return client.submit(DeploymentTarget.STANDALONE_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
            case APPLICATION:
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for standalone", flinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private void recordJobs(Long jobCode, Long jobVersion, ClusterClient clusterClient) throws Exception {
        Collection<JobStatusMessage> jobs = (Collection<JobStatusMessage>) clusterClient.listJobs().get();
        for (JobStatusMessage job : jobs) {
            FlinkJobInstanceDTO flinkJobInstanceDTO = new FlinkJobInstanceDTO();
            flinkJobInstanceDTO.setFlinkJobCode(jobCode);
            flinkJobInstanceDTO.setFlinkJobVersion(jobVersion);
            flinkJobInstanceDTO.setJobId(job.getJobId().toHexString());
            flinkJobInstanceDTO.setJobName(job.getJobName());
            flinkJobInstanceDTO.setJobState(FlinkJobState.of(job.getJobState().name()));
            flinkJobInstanceDTO.setClusterId(clusterClient.getClusterId().toString());
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
        if (!dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY)) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (!dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY)) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }

    private Configuration buildKubernetesConfiguration(Configuration dynamicProperties, Path clusterCredentialPath) throws IOException {
        final List<Path> childs = FileUtil.listFiles(clusterCredentialPath);
        checkState(!CollectionUtils.isEmpty(childs), () -> "Kubernetes kubeconfig can't be null");

        final Path kubeConfigFile = childs.get(0);
        dynamicProperties.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, kubeConfigFile.toAbsolutePath().toString());
        if (!dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY)) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (!dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY)) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }

    private Configuration buildStandaloneConfiguration(Configuration dynamicProperties, Path clusterCredentialPath) throws IOException {
        final List<Path> childs = FileUtil.listFiles(clusterCredentialPath);
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

    private PackageJarJob buildJarJob(FlinkJobForJarDTO flinkJobForJarDTO,
                                      FlinkArtifactJarDTO flinkArtifactJar,
                                      Path flinkArtifactJarPath)
            throws MalformedURLException {
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


    private Path getWorkspace() throws IOException {
        return SystemUtil.getRandomWorkspace();
    }

    private Path loadFlinkRelease(FlinkReleaseDTO flinkRelease, Path workspace) throws IOException {
        final Path tempFile = FileUtil.createFile(workspace, flinkRelease.getFileName());
        try (final OutputStream outputStream = FileUtil.getOutputStream(tempFile)) {
            flinkReleaseService.download(flinkRelease.getId(), outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        return FileUtil.listFiles(untarDir).get(0);
    }

    private Path loadClusterCredential(ClusterCredentialDTO clusterCredential, Path workspace) throws IOException {
        final List<FileStatusVO> fileStatusVOS = clusterCredentialService.listCredentialFile(clusterCredential.getId());
        final Path tempDir = FileUtil.createDir(workspace, clusterCredential.getName());
        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            final Path deployConfigFile = Paths.get(tempDir.toString(), fileStatusVO.getName());
            try (final OutputStream outputStream = FileUtil.getOutputStream(deployConfigFile)) {
                clusterCredentialService.downloadCredentialFile(clusterCredential.getId(), fileStatusVO.getName(), outputStream);
            }
        }
        return tempDir;
    }

    private List<URL> loadJarResources(List<Long> jarIds, Path workspace) throws IOException {
        List<URL> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(jarIds)) {
            return result;
        }
        for (Long jarId : jarIds) {
            JarDTO jarDTO = jarService.selectOne(jarId);
            Path path = FileUtil.createFile(workspace, jarDTO.getFileName());
            try (OutputStream output = FileUtil.getOutputStream(path)) {
                jarService.download(jarId, output);
                result.add(path.toFile().toURL());
            }
        }
        return result;
    }

    private Path loadFlinkArtifactJar(FlinkArtifactJarDTO flinkArtifactJarDTO, Path workspace) throws IOException {
        final Path tempDir = FileUtil.createDir(workspace, flinkArtifactJarDTO.getFlinkArtifact().getName() + "/" + flinkArtifactJarDTO.getVersion());
        final Path jarPath = FileUtil.createFile(tempDir, flinkArtifactJarDTO.getFileName());
        try (final OutputStream outputStream = FileUtil.getOutputStream(jarPath)) {
            flinkArtifactJarService.download(flinkArtifactJarDTO.getId(), outputStream);
        }
        return jarPath;
    }

    private Path loadSeaTunnelRelease(SeaTunnelReleaseDTO seaTunnelRelease, Path workspace) throws IOException {
        final Path tempFile = FileUtil.createFile(workspace, seaTunnelRelease.getFileName());
        try (final OutputStream outputStream = FileUtil.getOutputStream(tempFile)) {
            seaTunnelReleaseService.download(seaTunnelRelease.getId(), outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        return FileUtil.listFiles(untarDir).get(0);
    }

    private List<URL> loadSeaTunnelConnectors(SeaTunnelReleaseDTO seaTunnelRelease, DiJobDTO job, Path workspace) throws IOException {
        List<String> connectors = job.getJobStepList().stream()
                .map(DiJobStepDTO::getStepName)
                .map(SeaTunnelPluginMapping::of)
                .map(SeaTunnelPluginMapping::getPluginJarPrefix)
                .distinct()
                .collect(Collectors.toList());
        List<URL> result = new ArrayList<>(connectors.size());
        Path connectorsPath = FileUtil.createDir(workspace, "connectors");
        for (String connector : connectors) {
            String connectorFile = SeaTunnelReleaseUtil.convertToJar(seaTunnelRelease.getVersion().getValue(), connector);
            Path connectorPath = FileUtil.createFile(connectorsPath, connector);
            try (OutputStream outputStream = FileUtil.getOutputStream(connectorPath)) {
                seaTunnelReleaseService.downloadConnector(seaTunnelRelease.getId(), connectorFile, outputStream);
            }
            result.add(connectorPath.toFile().toURL());
        }
        return result;
    }

    private Path buildSeaTunnelConf(DiJobDTO job, Path workspace) throws Exception {
        Path file = FileUtil.createFile(workspace, job.getJobName() + ".json");
        String configJson = seatunnelConfigService.buildConfig(job);
        try (Writer writer = FileUtil.getWriter(file)) {
            FileCopyUtils.copy(configJson, writer);
        }
        return file;
    }

    private PackageJarJob buildSeaTunnelJob(Path seatunnelHomePath, Path seatunnelConfPath) throws IOException {
        PackageJarJob packageJarJob = new PackageJarJob();
        Path starterJarPath = SeaTunnelReleaseUtil.getStarterJarPath(seatunnelHomePath);
        packageJarJob.setJarFilePath(starterJarPath.toFile().toURL().toString());
        packageJarJob.setEntryPointClass(SeaTunnelReleaseUtil.SEATUNNEL_MAIN_CLASS);
        String[] args = new String[]{"--config", seatunnelConfPath.toString()};
        packageJarJob.setProgramArgs(args);
        return packageJarJob;
    }

}
