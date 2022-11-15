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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.base.util.FlinkUtil;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.common.enums.JobTypeEnum;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.core.di.service.vo.DagPanalVO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.core.scheduler.service.ScheduleService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.constant.GraphConstants;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagNodeDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.engine.seatunnel.service.util.QuartzJobUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.privilege.SecurityContext;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.service.SysConfigService;
import cn.sliew.scaleph.system.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.client.JobStatusMessage;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Deprecated
@Slf4j
@Service
public class SeatunnelJobServiceImpl implements SeatunnelJobService {

    @Autowired
    private DiProjectService diProjectService;
    @Autowired
    private DiJobService diJobService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;
    @Value("${app.engine.flink.state.savepoints.dir}")
    private String savePointDir;

    @Override
    public String preview(Long jobId) throws Exception {
        DiJobDTO job = diJobService.queryJobGraph(jobId);
        return seatunnelConfigService.buildConfig(job);
    }

    @Override
    public void run(DiJobRunVO jobRunParam) throws Exception {
        // 1.执行任务和 flink 集群的绑定
//        diJobService.update(jobRunParam.toDto());
        // 3.获取任务信息
//        DiJobDTO diJobDTO = diJobService.queryJobGraph(jobRunParam.getJobId());
//        // 4.调度或者运行
//        if (JobTypeEnum.BATCH.getValue().equals(diJobDTO.getJobType().getValue())
//                && StringUtils.hasText(diJobDTO.getJobCrontab())) {
//            schedule(diJobDTO);
//        } else {
//            submit(diJobDTO);
//        }
    }

    @Override
    public void submit(DiJobDTO diJobDTO) throws Exception {
//        Path projectPath = getProjectBasePath(diJobDTO.getProjectId());
//        Path jobConfFile = buildConfFile(diJobDTO, projectPath);
//        Path seatunnelJarPath = getSeatunnelJar();
//
//        //build configuration
//        DiClusterConfigDTO clusterConfig = diClusterConfigService.selectOne(diJobDTO.getClusterId());
//        Configuration configuration = buildConfiguration(diJobDTO, seatunnelJarPath, clusterConfig.getConfig(), projectPath.toFile());
//        //build job
//        PackageJarJob jarJob = buildJob(seatunnelJarPath.toUri().toString(), jobConfFile, diJobDTO.getJobAttrList());
//
//        //prevent System.exit() invocation when seatunnel job config check result is false
//        CliClient client = new DescriptorCliClient();
//        ClusterClient clusterClient = SecurityContext.call(() ->
//                client.submit(DeploymentTarget.STANDALONE_SESSION, null, configuration, jarJob));
//
//        Optional<JobID> jobID = FlinkUtil.listJobs(clusterClient).stream().map(JobStatusMessage::getJobId).findFirst();
//        //write log
//        insertJobLog(diJobDTO, configuration, jobID.orElseThrow(() -> new IllegalStateException("flink job id not exists")));
//        diJobDTO.setRuntimeState(RuntimeState.RUNNING);
////        diJobService.update(diJobDTO);
    }

    @Override
    public void schedule(DiJobDTO diJobDTO) throws Exception {
//        DiProjectDTO project = diProjectService.selectOne(diJobDTO.getProjectId());
//        String jobName = QuartzJobUtil.getJobName(project.getProjectCode(), diJobDTO.getJobCode());
//        JobKey seatunnelJobKey =
//                scheduleService.getJobKey(QuartzJobUtil.getFlinkBatchJobName(jobName), Constants.INTERNAL_GROUP);
//        JobDetail seatunnelJob = JobBuilder.newJob(SeatunnelFlinkJob.class)
//                .withIdentity(seatunnelJobKey)
//                .storeDurably()
//                .build();
//        seatunnelJob.getJobDataMap().put(Constants.JOB_PARAM_JOB_INFO, diJobDTO);
//        seatunnelJob.getJobDataMap().put(Constants.JOB_PARAM_PROJECT_INFO, project);
//        TriggerKey seatunnelJobTriKey =
//                scheduleService.getTriggerKey(QuartzJobUtil.getFlinkBatchTriggerKey(jobName), Constants.INTERNAL_GROUP);
//        Trigger seatunnelJobTri = TriggerBuilder.newTrigger()
//                .withIdentity(seatunnelJobTriKey)
//                .withSchedule(CronScheduleBuilder.cronSchedule(diJobDTO.getJobCrontab()))
//                .build();
//        if (scheduleService.checkExists(seatunnelJobKey)) {
//            scheduleService.deleteScheduleJob(seatunnelJobKey);
//        }
//        scheduleService.addScheduleJob(seatunnelJob, seatunnelJobTri);
//        diJobDTO.setRuntimeState(RuntimeState.RUNNING);
//        diJobService.update(diJobDTO);
    }

    @Override
    public void stop(Long jobId) throws Exception {
//        DiJobDTO job = diJobService.queryJobGraph(jobId);
//        // 1.取消调度任务
//        unschedule(job);
//        // 2.停掉所有正在运行的任务
//        cancel(job);
//        // 3.更新任务状态
//        job.setRuntimeState(RuntimeState.RUNNING);
////        diJobService.update(job);
    }

    @Override
    public void cancel(DiJobDTO diJobDTO) throws Exception {
//        List<DiJobLogDTO> list = diJobLogService.listRunningJobInstance(diJobDTO.getJobCode());
//        Configuration configuration = GlobalConfiguration.loadConfiguration();
//        for (DiJobLogDTO instance : list) {
//            DiClusterConfigDTO clusterConfig = diClusterConfigService.selectOne(instance.getClusterId());
//            String host = clusterConfig.getConfig().get(JobManagerOptions.ADDRESS.key());
//            int restPort = Integer.parseInt(clusterConfig.getConfig().get(RestOptions.PORT.key()));
//            RestClient client = new FlinkRestClient(host, restPort, configuration);
//            JobClient jobClient = client.job();
//            if (StringUtils.isEmpty(savePointDir)) {
//                // 取消任务时，不做 savepoint
//                CompletableFuture<EmptyResponseBody> future = jobClient.jobTerminate(instance.getJobInstanceId(), "cancel");
//                future.get();
//            } else {
//                // 取消任务时，进行 savepoint
//                if (savePointDir.endsWith("/")) {
//                    savePointDir = savePointDir.substring(0, savePointDir.length() - 1);
//                }
//                String savepointPath = String.join("/", savePointDir, clusterConfig.getClusterName(), instance.getJobInstanceId());
//                StopWithSavepointRequestBody requestBody = new StopWithSavepointRequestBody(savepointPath, true);
//                CompletableFuture<TriggerResponse> future = jobClient.jobStop(instance.getJobInstanceId(), requestBody);
//                future.get();
//            }
//        }
    }

    @Override
    public void unschedule(DiJobDTO diJobDTO) throws Exception {
        DiProjectDTO project = diProjectService.selectOne(diJobDTO.getProjectId());
        String jobName = QuartzJobUtil.getJobName(project.getProjectCode(), diJobDTO.getJobCode());
        JobKey seatunnelJobKey = scheduleService.getJobKey(QuartzJobUtil.getFlinkBatchJobName(jobName), Constants.INTERNAL_GROUP);
        if (scheduleService.checkExists(seatunnelJobKey)) {
            scheduleService.deleteScheduleJob(seatunnelJobKey);
        }
    }

    private Path getProjectBasePath(Long projectId) {
        return FileUtil.getTmpDir().toPath().resolve(String.valueOf(projectId));
    }

    @Override
    public Path buildConfFile(DiJobDTO diJobDTO, Path projectPath) throws Exception {
        String jobJson = seatunnelConfigService.buildConfig(diJobDTO);
        final File tempFile = FileUtil.file(projectPath.toFile(), diJobDTO.getJobCode() + ".json");
        FileUtil.writeUtf8String(jobJson, tempFile);
        return tempFile.toPath();
    }

    @Override
    public Path getSeatunnelJar() throws IOException {
        String seatunnelPath = this.sysConfigService.getSeatunnelHome();
        Path seatunnelJarPath = Paths.get(seatunnelPath, "lib", "seatunnel-core-flink.jar");
        if (Files.notExists(seatunnelJarPath)) {
            throw new IOException("response.error.di.noJar.seatunnel");
        }
        return seatunnelJarPath;
    }

    private Set<File> getSeatunnelPluginJarFile(List<DiJobStepDTO> jobStepList) {
        if (CollectionUtils.isEmpty(jobStepList)) {
            return null;
        }
        Set<File> files = new TreeSet<>();
        String seatunnelPath = this.sysConfigService.getSeatunnelHome();
        Path seatunnelConnectorsPath = Paths.get(seatunnelPath, "connectors", "flink");
        File seatunnelConnectorDir = seatunnelConnectorsPath.toFile();
        for (DiJobStepDTO step : jobStepList) {
//            String pluginTag = this.seatunnelConfigService.getSeatunnelPluginTag(
//                    step.getStepType().getValue(), step.getStepName());
//            FileFilter fileFilter = new RegexFileFilter(".*" + pluginTag + ".*");
//            File[] pluginJars = seatunnelConnectorDir.listFiles(fileFilter);
//            if (pluginJars != null) {
//                Collections.addAll(files, pluginJars);
//            }
        }
        return files;
    }

    @Override
    public Configuration buildConfiguration(DiJobDTO job, Path seatunnelJarPath,
                                            Map<String, String> clusterConf,
                                            File projectPath) throws IOException {
        Configuration configuration = new Configuration();
//        configuration.setString(PipelineOptions.NAME, job.getJobName());
//        configuration.setString(JobManagerOptions.ADDRESS, clusterConf.get(JobManagerOptions.ADDRESS.key()));
//        configuration.setInteger(JobManagerOptions.PORT, Integer.parseInt(clusterConf.get(JobManagerOptions.PORT.key())));
//        configuration.setInteger(RestOptions.PORT, Integer.parseInt(clusterConf.get(RestOptions.PORT.key())));
//
//        Set<String> jars = new TreeSet<>();
//        Set<File> pluginJars = getSeatunnelPluginJarFile(job.getJobStepList());
//        if (!CollectionUtils.isEmpty(pluginJars)) {
//            for (File jar : pluginJars) {
//                jars.add(jar.toURI().toString());
//            }
//        }
//        jars.add(seatunnelJarPath.toUri().toString());
//
//
//        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
//        configuration.setString(DeploymentOptions.TARGET, RemoteExecutor.NAME);
        return configuration;
    }

    private PackageJarJob buildJob(String seatunnelPath, Path confFile, List<DiJobAttrDTO> jobAttrList) throws URISyntaxException {
        PackageJarJob jarJob = new PackageJarJob();
        jarJob.setJarFilePath(seatunnelPath);
        jarJob.setEntryPointClass("org.apache.seatunnel.core.flink.SeatunnelFlink");
        List<String> variables = new ArrayList<>(Arrays.asList("--config", confFile.toString()));
        jobAttrList.stream()
                .filter(attr -> JobAttrTypeEnum.JOB_ATTR.getValue()
                        .equals(attr.getJobAttrType().getValue()))
                .forEach(attr -> {
                    variables.add("--variable");
                    variables.add(attr.getJobAttrKey() + "=" + attr.getJobAttrValue());
                });
        jarJob.setProgramArgs(variables.toArray(new String[0]));
        jarJob.setClasspaths(Collections.emptyList());
        jarJob.setSavepointSettings(SavepointRestoreSettings.none());
        return jarJob;
    }

    @Override
    public List<DagPanelDTO> loadDndPanelInfo() throws PluginException {
        List<DagPanelDTO> list = new ArrayList<>();
        for (SeaTunnelPluginType type : SeaTunnelPluginType.values()) {
            Set<SeaTunnelConnectorPlugin> plugins = seatunnelConnectorService.getAvailableConnectors(type);
            DagPanelDTO panel = toDagPanel(type, plugins);
            if (panel != null) {
                list.add(panel);
            }
        }
        return list;
    }

    private DagPanelDTO toDagPanel(SeaTunnelPluginType pluginType, Set<SeaTunnelConnectorPlugin> plugins) {
        if (CollectionUtils.isEmpty(plugins)) {
            return null;
        }
        DagPanelDTO panel = new DagPanelDTO();
        panel.setId(pluginType.getLabel());
        panel.setHeader(pluginType.getLabel());
        List<DagNodeDTO> nodeList = new ArrayList<>();
        plugins.stream().sorted(Comparator.comparing(plugin -> plugin.getPluginName().ordinal()))
                .forEachOrdered(plugin -> {
                    PluginInfo pluginInfo = plugin.getPluginInfo();
                    DagNodeDTO node = new DagNodeDTO();
                    node.setId(pluginInfo.getName());
                    node.setDescription(pluginInfo.getDescription());
                    node.setLabel(plugin.getPluginName().getLabel());
                    node.setRenderKey(GraphConstants.DND_RENDER_ID);
                    node.setData(buildPluginInfo(plugin));
                    nodeList.add(node);
                });
        panel.setChildren(nodeList);
        return panel;
    }

    private DagPanalVO buildPluginInfo(SeaTunnelConnectorPlugin connector) {
        final DagPanalVO dagPanalVO = new DagPanalVO();
        dagPanalVO.setDisplayName(connector.getPluginName().getLabel() + " " + connector.getPluginType().getLabel());
        dagPanalVO.setName(connector.getPluginName().getValue());
        dagPanalVO.setType(connector.getPluginType().getValue());
        dagPanalVO.setEngine(connector.getEngineType().getValue());
        return dagPanalVO;
    }
}
