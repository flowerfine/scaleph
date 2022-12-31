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
import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.FlinkResourceProvider;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.*;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobStepDTO;
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
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.job.savepoints.SavepointTriggerRequestBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class WsFlinkServiceImpl implements WsFlinkService {

    @Autowired
    private WsFlinkJobService wsFlinkJobService;
    @Autowired
    private WsFlinkJobInstanceService wsFlinkJobInstanceService;
    @Autowired
    private WsFlinkClusterConfigService wsFlinkClusterConfigService;
    @Autowired
    private WsFlinkClusterInstanceService wsFlinkClusterInstanceService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;
    @Autowired
    private SeaTunnelReleaseService seaTunnelReleaseService;
    @Autowired
    private ClusterCredentialService clusterCredentialService;
    @Autowired
    private JarService jarService;
    @Autowired
    private WsFlinkArtifactJarService wsFlinkArtifactJarService;
    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private WsFlinkJobLogService wsFlinkJobLogService;

    /**
     * requires:
     * 1. flink release
     * 2. resource provider certificate: hadoop conf or kubeconfig
     * 3. flink options
     */
    @Override
    public void createSessionCluster(Long projectId, Long flinkClusterConfigId) throws Exception {
        final WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(flinkClusterConfigId);
        final FlinkResourceProvider resourceProvider = wsFlinkClusterConfigDTO.getResourceProvider();
        ClusterClient clusterClient;
        switch (resourceProvider) {
            case YARN:
                clusterClient = createYarnSessionCluster(wsFlinkClusterConfigDTO);
                break;
            case NATIVE_KUBERNETES:
                clusterClient = createKubernetesSessionCluster(wsFlinkClusterConfigDTO);
                break;
            case STANDALONE:
                clusterClient = createExistingSessionCluster(wsFlinkClusterConfigDTO);
                break;
            default:
                throw new UnsupportedOperationException("unknown resource provider for " + resourceProvider);
        }

        WsFlinkClusterInstanceDTO dto = new WsFlinkClusterInstanceDTO();
        dto.setProjectId(projectId);
        dto.setFlinkClusterConfigId(wsFlinkClusterConfigDTO.getId());
        dto.setName(wsFlinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        //todo session cluster has not cluster id
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(FlinkClusterStatus.RUNNING);
        wsFlinkClusterInstanceService.insert(dto);
    }

    @Override
    public void submit(WsFlinkJobDTO wsFlinkJobDTO) throws Exception {
        if (wsFlinkJobDTO != null) {
            final Path workspace = getWorkspace();
            try {
                WsFlinkClusterConfigDTO clusterConfig = wsFlinkJobDTO.getWsFlinkClusterConfig();
                if (clusterConfig != null && clusterConfig.getFlinkRelease() != null) {
                    FlinkReleaseDTO releaseDTO = flinkReleaseService.selectOne(clusterConfig.getFlinkRelease().getId());
                    clusterConfig.setFlinkRelease(releaseDTO);
                }
                if (clusterConfig != null && clusterConfig.getClusterCredential() != null) {
                    ClusterCredentialDTO clusterCredential = clusterCredentialService.selectOne(clusterConfig.getClusterCredential().getId());
                    clusterConfig.setClusterCredential(clusterCredential);
                }
                ClusterClient clusterClient;
                switch (wsFlinkJobDTO.getType()) {
                    case JAR:
                        clusterClient = doSubmitJar(wsFlinkJobDTO, workspace);
                        recordJobs(wsFlinkJobDTO, clusterClient);
                        break;
                    case SEATUNNEL:
                        clusterClient = doSubmitSeatunnel(wsFlinkJobDTO, workspace);
                        recordJobs(wsFlinkJobDTO, clusterClient);
                        break;
                    case SQL:
                        break;
                }
            } finally {
                FileUtil.deleteDir(workspace);
            }
        }
    }

    private ClusterClient doSubmitJar(WsFlinkJobDTO wsFlinkJobDTO, Path workspace) throws Exception {

        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkJobDTO.getWsFlinkClusterConfig();

        Path flinkHomePath = loadFlinkRelease(wsFlinkClusterConfigDTO.getFlinkRelease(), workspace);

        List<URL> jars = loadJarResources(wsFlinkJobDTO.getJars(), workspace);

        WsFlinkArtifactJarDTO flinkArtifactJar = wsFlinkArtifactJarService.selectOne(wsFlinkJobDTO.getFlinkArtifactId());
        Path flinkArtifactJarPath = loadFlinkArtifactJar(flinkArtifactJar, workspace);
        jars.add(flinkArtifactJarPath.toFile().toURL());
        PackageJarJob packageJarJob = buildJarJob(wsFlinkJobDTO, flinkArtifactJar, flinkArtifactJarPath);

        final Path clusterCredentialPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(wsFlinkJobDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(wsFlinkJobDTO.getFlinkConfig()));
        }
        configuration.setString(PipelineOptions.NAME, wsFlinkJobDTO.getName());
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);

        switch (wsFlinkClusterConfigDTO.getResourceProvider()) {
            case YARN:
                return doSubmitToYARN(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case NATIVE_KUBERNETES:
                return doSubmitToKubernetes(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case STANDALONE:
                return doSubmitToStandalone(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s for flink jar job submission", wsFlinkClusterConfigDTO.getResourceProvider().getValue()));
        }
    }

    private ClusterClient doSubmitSeatunnel(WsFlinkJobDTO wsFlinkJobDTO, Path workspace) throws Exception {
        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkJobDTO.getWsFlinkClusterConfig();
        SeaTunnelReleaseDTO seaTunnelRelease = seaTunnelReleaseService.selectByVersion(SeaTunnelVersion.V_2_3_0);

        Path flinkHomePath = loadFlinkRelease(wsFlinkClusterConfigDTO.getFlinkRelease(), workspace);
        Path seatunnelHomePath = loadSeaTunnelRelease(seaTunnelRelease, workspace);

        List<URL> jars = loadJarResources(wsFlinkJobDTO.getJars(), workspace);
        WsDiJobDTO wsDiJobDTO = wsDiJobService.queryJobGraph(wsFlinkJobDTO.getFlinkArtifactId());
        wsDiJobDTO.setJobName(wsFlinkJobDTO.getName());
        Path seatunnelConfPath = buildSeaTunnelConf(wsDiJobDTO, workspace);
        PackageJarJob packageJarJob = buildSeaTunnelJob(seatunnelHomePath, seatunnelConfPath);
        jars.add(SeaTunnelReleaseUtil.getStarterJarPath(seatunnelHomePath).toFile().toURL());
        jars.addAll(loadSeaTunnelConnectors(seaTunnelRelease, wsDiJobDTO, workspace));

        final Path clusterCredentialPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, clusterCredentialPath);
        if (CollectionUtils.isEmpty(wsFlinkJobDTO.getFlinkConfig()) == false) {
            configuration.addAll(Configuration.fromMap(wsFlinkJobDTO.getFlinkConfig()));
        }
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        configuration.setString(PipelineOptions.NAME, wsFlinkJobDTO.getName());
        switch (wsFlinkClusterConfigDTO.getResourceProvider()) {
            case YARN:
                return doSubmitToYARN(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case NATIVE_KUBERNETES:
                return doSubmitToKubernetes(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            case STANDALONE:
                return doSubmitToStandalone(wsFlinkJobDTO.getWsFlinkClusterInstance(), wsFlinkClusterConfigDTO, configuration, flinkHomePath, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s for flink seatunnel job submission", wsFlinkClusterConfigDTO.getResourceProvider().getValue()));
        }
    }

    private ClusterClient doSubmitToKubernetes(WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO, WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO,
                                               Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        switch (wsFlinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                CliClient client = new DescriptorCliClient();
                configuration.setString(KubernetesConfigOptions.CLUSTER_ID, wsFlinkClusterInstanceDTO.getClusterId());
                return client.submit(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
            case APPLICATION:
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for native kubernetes", wsFlinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private ClusterClient doSubmitToYARN(WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO, WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO,
                                         Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        CliClient client = new DescriptorCliClient();
        switch (wsFlinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                configuration.setString(YarnConfigOptions.APPLICATION_ID, wsFlinkClusterInstanceDTO.getClusterId());
                return client.submit(DeploymentTarget.YARN_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
                return client.submit(DeploymentTarget.YARN_PER_JOB, flinkHomePath, configuration, packageJarJob);
            case APPLICATION:
                return client.submitApplication(DeploymentTarget.YARN_APPLICATION, flinkHomePath, configuration, packageJarJob);
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for YARN", wsFlinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private ClusterClient doSubmitToStandalone(WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO, WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO,
                                               Configuration configuration, Path flinkHomePath, PackageJarJob packageJarJob) throws Exception {
        switch (wsFlinkClusterConfigDTO.getDeployMode()) {
            case SESSION:
                URL url = new URL(wsFlinkClusterInstanceDTO.getWebInterfaceUrl());
                configuration.setString(RestOptions.ADDRESS, url.getHost());
                configuration.setInteger(RestOptions.PORT, url.getPort());
                CliClient client = new DescriptorCliClient();
                return client.submit(DeploymentTarget.STANDALONE_SESSION, flinkHomePath, configuration, packageJarJob);
            case PER_JOB:
            case APPLICATION:
            default:
                throw new UnsupportedOperationException(
                        String.format("scaleph not supports %s mode for standalone", wsFlinkClusterConfigDTO.getDeployMode().getValue()));
        }
    }

    private void recordJobs(WsFlinkJobDTO wsFlinkJobDTO, ClusterClient clusterClient) throws Exception {
        Collection<JobStatusMessage> jobs = (Collection<JobStatusMessage>) clusterClient.listJobs().get();
        for (JobStatusMessage job : jobs) {
            WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = new WsFlinkJobInstanceDTO();
            wsFlinkJobInstanceDTO.setFlinkJobCode(wsFlinkJobDTO.getCode());
            wsFlinkJobInstanceDTO.setJobId(job.getJobId().toHexString());
            wsFlinkJobInstanceDTO.setJobName(job.getJobName());
            wsFlinkJobInstanceDTO.setJobState(FlinkJobState.of(job.getJobState().name()));
            wsFlinkJobInstanceDTO.setClusterId(clusterClient.getClusterId().toString());
            wsFlinkJobInstanceDTO.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
            wsFlinkJobInstanceDTO.setClusterStatus(FlinkClusterStatus.RUNNING);
            if (wsFlinkJobDTO.getName().equals(job.getJobName())) {
                wsFlinkJobInstanceService.insert(wsFlinkJobInstanceDTO);
            }
        }
    }

    @Override
    public void stop(Long id) throws Exception {
        final WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = wsFlinkJobInstanceService.selectOne(id);
        final URL url = new URL(wsFlinkJobInstanceDTO.getWebInterfaceUrl());
        final String jobId = wsFlinkJobInstanceDTO.getJobId();

        RestClient client = new FlinkRestClient(url.getHost(), url.getPort(), new Configuration());
        final JobClient jobClient = client.job();

        String savePointPath = String.join("/", String.valueOf(wsFlinkJobInstanceDTO.getFlinkJobCode()), String.valueOf(wsFlinkJobInstanceDTO.getId()));
        Path targetDirectory = SystemUtil.getSavepointDir(savePointPath);
        StopWithSavepointRequestBody requestBody = new StopWithSavepointRequestBody(targetDirectory.toString(), true);
        jobClient.jobStop(jobId, requestBody);

        wsFlinkJobInstanceDTO.setEndTime(new Date());
        wsFlinkJobInstanceDTO.setJobState(FlinkJobState.SUSPENDED);
        wsFlinkJobInstanceDTO.setDuration((wsFlinkJobInstanceDTO.getEndTime().getTime() - wsFlinkJobInstanceDTO.getStartTime().getTime()) / 1000);

        final WsFlinkJobLogDTO flinkJobLogDTO = new WsFlinkJobLogDTO();
        flinkJobLogDTO.setFlinkJobCode(wsFlinkJobInstanceDTO.getFlinkJobCode());
        flinkJobLogDTO.setJobId(wsFlinkJobInstanceDTO.getJobId());
        flinkJobLogDTO.setJobName(wsFlinkJobInstanceDTO.getJobName());
        flinkJobLogDTO.setJobState(wsFlinkJobInstanceDTO.getJobState());
        flinkJobLogDTO.setClusterId(wsFlinkJobInstanceDTO.getClusterId());
        flinkJobLogDTO.setWebInterfaceUrl(wsFlinkJobInstanceDTO.getWebInterfaceUrl());
        flinkJobLogDTO.setClusterStatus(wsFlinkJobInstanceDTO.getClusterStatus());
        flinkJobLogDTO.setStartTime(wsFlinkJobInstanceDTO.getStartTime());
        flinkJobLogDTO.setEndTime(wsFlinkJobInstanceDTO.getEndTime());
        flinkJobLogDTO.setDuration(wsFlinkJobInstanceDTO.getDuration());
        flinkJobLogDTO.setCreator(wsFlinkJobInstanceDTO.getCreator());
        flinkJobLogDTO.setEditor(wsFlinkJobInstanceDTO.getEditor());
        wsFlinkJobLogService.insert(flinkJobLogDTO);
        wsFlinkJobInstanceService.deleteById(id);
    }

    @Override
    public void cancel(Long id) throws Exception {
        final WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = wsFlinkJobInstanceService.selectOne(id);
        final URL url = new URL(wsFlinkJobInstanceDTO.getWebInterfaceUrl());
        final String jobId = wsFlinkJobInstanceDTO.getJobId();

        RestClient client = new FlinkRestClient(url.getHost(), url.getPort(), new Configuration());
        final JobClient jobClient = client.job();
        jobClient.jobTerminate(jobId, "cancel");
        wsFlinkJobInstanceDTO.setEndTime(new Date());
        wsFlinkJobInstanceDTO.setJobState(FlinkJobState.CANCELED);
        wsFlinkJobInstanceDTO.setDuration((wsFlinkJobInstanceDTO.getEndTime().getTime() - wsFlinkJobInstanceDTO.getStartTime().getTime()) / 1000);


        final WsFlinkJobLogDTO flinkJobLogDTO = new WsFlinkJobLogDTO();
        flinkJobLogDTO.setFlinkJobCode(wsFlinkJobInstanceDTO.getFlinkJobCode());
        flinkJobLogDTO.setJobId(wsFlinkJobInstanceDTO.getJobId());
        flinkJobLogDTO.setJobName(wsFlinkJobInstanceDTO.getJobName());
        flinkJobLogDTO.setJobState(wsFlinkJobInstanceDTO.getJobState());
        flinkJobLogDTO.setClusterId(wsFlinkJobInstanceDTO.getClusterId());
        flinkJobLogDTO.setWebInterfaceUrl(wsFlinkJobInstanceDTO.getWebInterfaceUrl());
        flinkJobLogDTO.setClusterStatus(wsFlinkJobInstanceDTO.getClusterStatus());
        flinkJobLogDTO.setStartTime(wsFlinkJobInstanceDTO.getStartTime());
        flinkJobLogDTO.setEndTime(wsFlinkJobInstanceDTO.getEndTime());
        flinkJobLogDTO.setDuration(wsFlinkJobInstanceDTO.getDuration());
        flinkJobLogDTO.setCreator(wsFlinkJobInstanceDTO.getCreator());
        flinkJobLogDTO.setEditor(wsFlinkJobInstanceDTO.getEditor());
        wsFlinkJobLogService.insert(flinkJobLogDTO);
        wsFlinkJobInstanceService.deleteById(id);
    }

    @Override
    public void triggerSavepoint(Long id) throws Exception {
        final WsFlinkJobInstanceDTO wsFlinkJobInstanceDTO = wsFlinkJobInstanceService.selectOne(id);
        final URL url = new URL(wsFlinkJobInstanceDTO.getWebInterfaceUrl());
        final String jobId = wsFlinkJobInstanceDTO.getJobId();
        String savePointPath = String.join("/", String.valueOf(wsFlinkJobInstanceDTO.getFlinkJobCode()), String.valueOf(wsFlinkJobInstanceDTO.getId()));
        Path targetDirectory = SystemUtil.getSavepointDir(savePointPath);
        RestClient client = new FlinkRestClient(url.getHost(), url.getPort(), new Configuration());
        final JobClient jobClient = client.job();
        SavepointTriggerRequestBody requestBody = new SavepointTriggerRequestBody(targetDirectory.toString(), true);
        final CompletableFuture<TriggerResponse> triggerResponseCompletableFuture = jobClient.jobSavepoint(jobId, requestBody);
        triggerResponseCompletableFuture.get();
    }


    @Override
    public void shutdown(Long id) throws Exception {
        final WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO = wsFlinkClusterInstanceService.selectOne(id);
        final WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(wsFlinkClusterInstanceDTO.getFlinkClusterConfigId());
        final Path workspace = getWorkspace();
        final Path clusterCredentialPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, clusterCredentialPath);
        buildConfiguration(wsFlinkClusterInstanceDTO.getClusterId(), wsFlinkClusterConfigDTO.getResourceProvider(), wsFlinkClusterConfigDTO.getDeployMode(), configuration);

        ClusterClient client = FlinkUtil.retrieve(configuration);
        client.shutDownCluster();

        WsFlinkClusterInstanceDTO dto = new WsFlinkClusterInstanceDTO();
        dto.setId(id);
        dto.setStatus(FlinkClusterStatus.STOPED);
        wsFlinkClusterInstanceService.update(dto);
    }


    @Override
    public void shutdownBatch(List<Long> ids) throws Exception {
        for (Long id : ids) {
            shutdown(id);
        }
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

    private ClusterClient createKubernetesSessionCluster(WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkHomePath = loadFlinkRelease(wsFlinkClusterConfigDTO.getFlinkRelease(), workspace);
        final Path flinkDeployConfigPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, flinkDeployConfigPath);
        ClusterClient<String> clusterClient = SessionClient.create(DeploymentTarget.NATIVE_KUBERNETES_SESSION, flinkHomePath, configuration);
        FileUtils.deleteDirectory(workspace.toFile());
        return clusterClient;
    }

    private ClusterClient createExistingSessionCluster(WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkDeployConfigPath = loadClusterCredential(wsFlinkClusterConfigDTO.getClusterCredential(), workspace);
        final Configuration configuration = buildConfiguration(wsFlinkClusterConfigDTO, flinkDeployConfigPath);
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
    private Configuration buildConfiguration(WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO, Path clusterCredentialPath) throws IOException {
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(wsFlinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(wsFlinkClusterConfigDTO.getConfigOptions());
        }

        final ClusterCredentialDTO clusterCredentialDTO = wsFlinkClusterConfigDTO.getClusterCredential();
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

    private PackageJarJob buildJarJob(WsFlinkJobDTO wsFlinkJobDTO,
                                      WsFlinkArtifactJarDTO flinkArtifactJar,
                                      Path flinkArtifactJarPath)
            throws MalformedURLException {
        PackageJarJob packageJarJob = new PackageJarJob();
        packageJarJob.setJarFilePath(flinkArtifactJarPath.toFile().toURL().toString());
        packageJarJob.setEntryPointClass(flinkArtifactJar.getEntryClass());
        if (CollectionUtils.isEmpty(wsFlinkJobDTO.getJobConfig()) == false) {
            List<String> args = new ArrayList<>(wsFlinkJobDTO.getJobConfig().size() * 2);
            for (Map.Entry<String, String> entry : wsFlinkJobDTO.getJobConfig().entrySet()) {
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

    private Path loadFlinkArtifactJar(WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO, Path workspace) throws IOException {
        final Path tempDir = FileUtil.createDir(workspace, wsFlinkArtifactJarDTO.getWsFlinkArtifact().getName() + "/" + wsFlinkArtifactJarDTO.getVersion());
        final Path jarPath = FileUtil.createFile(tempDir, wsFlinkArtifactJarDTO.getFileName());
        try (final OutputStream outputStream = FileUtil.getOutputStream(jarPath)) {
            wsFlinkArtifactJarService.download(wsFlinkArtifactJarDTO.getId(), outputStream);
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

    private List<URL> loadSeaTunnelConnectors(SeaTunnelReleaseDTO seaTunnelRelease, WsDiJobDTO job, Path workspace) throws IOException {
        List<String> connectors = job.getJobStepList().stream()
                .map(WsDiJobStepDTO::getStepName)
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

    private Path buildSeaTunnelConf(WsDiJobDTO job, Path workspace) throws Exception {
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
