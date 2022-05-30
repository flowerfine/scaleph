package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.engine.seatunnel.JobConfigHelper;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.privilege.SecurityContext;
import cn.sliew.scaleph.storage.service.StorageService;
import cn.sliew.scaleph.storage.service.impl.NioFileServiceImpl;
import cn.sliew.scaleph.system.service.SysConfigService;
import cn.sliew.scaleph.system.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
public class SeatunnelJobServiceImpl implements SeatunnelJobService {

    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiJobAttrService diJobAttrService;
    @Autowired
    private DiJobLinkService diJobLinkService;
    @Autowired
    private DiJobStepService diJobStepService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
    @Autowired
    private DiJobLogService diJobLogService;
    @Autowired
    private DiClusterConfigService diClusterConfigService;

    @Resource(name = "${app.resource.type}")
    private StorageService storageService;
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private JobConfigHelper jobConfigHelper;

    @Override
    public DiJobDTO queryJobInfo(Long jobId) {
        DiJobDTO job = diJobService.selectOne(jobId);
        job.setJobAttrList(diJobAttrService.listJobAttr(jobId));
        job.setJobLinkList(diJobLinkService.listJobLink(jobId));
        job.setJobStepList(diJobStepService.listJobStep(jobId));
        return job;
    }

    @Override
    public void submit(DiJobRunVO jobRunParam) {
        // 1.执行任务和 flink 集群的绑定
        diJobService.update(jobRunParam.toDto());
        // 2.绑定任务和资源
        diJobResourceFileService.bindResource(jobRunParam.getJobId(), jobRunParam.getResources());
        // 3.获取任务信息
        DiJobDTO diJobDTO = queryJobInfo(jobRunParam.getJobId());

        Path projectPath = getProjectBasePath(diJobDTO.getProjectId());
        URL jobConfFile = buildConfFile(diJobDTO, projectPath);
        Path seatunnelJarPath = getSeatunnelJar();

        //build configuration
        DiClusterConfigDTO clusterConfig =
                diClusterConfigService.selectOne(jobRunParam.getClusterId());
        Configuration configuration =
                buildConfiguration(diJobDTO, seatunnelJarPath , clusterConfig.getConfig(), projectPath.toFile());
        //build job
        PackageJarJob jarJob =
                buildJob(seatunnelJarPath.toUri().toString(), jobConfFile, diJobDTO.getJobAttrList());

        //prevent System.exit() invocation when seatunnel job config check result is false
        CliClient client = new DescriptorCliClient();
        JobID jobInstanceID = SecurityContext.call(() ->
                client.submit(DeploymentTarget.STANDALONE_SESSION, configuration, jarJob));

        //write log
        DiJobLogDTO jobLogInfo = new DiJobLogDTO();
        jobLogInfo.setProjectId(diJobDTO.getProjectId());
        jobLogInfo.setJobId(diJobDTO.getId());
        jobLogInfo.setJobCode(diJobDTO.getJobCode());
        jobLogInfo.setClusterId(jobRunParam.getClusterId());
        jobLogInfo.setJobInstanceId(jobInstanceID.toString());
        String clusterHost = configuration.getString(JobManagerOptions.ADDRESS);
        int clusterPort = configuration.getInteger(RestOptions.PORT);
        String jobLogUrl =
                "http://" + clusterHost + ":" + clusterPort + "/#/job/" + jobInstanceID +
                        "/overview";
        jobLogInfo.setJobLogUrl(jobLogUrl);
        jobLogInfo.setJobInstanceState(
                DictVO.toVO(DictConstants.JOB_INSTANCE_STATE, JobStatus.INITIALIZING.toString()));
        jobLogInfo.setStartTime(new Date());
        diJobLogService.insert(jobLogInfo);
    }

    @Override
    public void schedule(DiJobDTO diJobDTO) {

    }

    @Override
    public void cancel(Long jobId) {

    }

    private Path getProjectBasePath(Long projectId) {
        return Paths.get(projectId.toString());
    }

    @Override
    public URL buildConfFile(DiJobDTO diJobDTO, Path projectPath) throws IOException {
        String jobJson = jobConfigHelper.buildJob(diJobDTO);
        final Path tempFile = Files.createTempFile(projectPath, diJobDTO.getJobCode(), ".json");
        FileUtil.writeUtf8String(jobJson, tempFile.toFile());
        return tempFile.toUri().toURL();
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

    @Override
    public Configuration buildConfiguration(DiJobDTO job, Path seatunnelJarPath,
                                            Map<String, String> clusterConf,
                                            File projectPath) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setString(PipelineOptions.NAME, job.getJobCode());
        configuration.setString(JobManagerOptions.ADDRESS, clusterConf.get(JobManagerOptions.ADDRESS.key()));
        configuration.setInteger(JobManagerOptions.PORT, Integer.parseInt(clusterConf.get(JobManagerOptions.PORT.key())));
        configuration.setInteger(RestOptions.PORT, Integer.parseInt(clusterConf.get(RestOptions.PORT.key())));

        List<DiResourceFileDTO> resourceList = diJobResourceFileService.listJobResources(job.getId());
        Set<String> jars = new TreeSet<>();
        jars.add(seatunnelJarPath.toUri().toString());

        StorageService localStorageService = new NioFileServiceImpl(projectPath.getAbsolutePath());
        for (DiResourceFileDTO file : resourceList) {
            Long fileSize = storageService.getFileSize(file.getFilePath(), file.getFileName());
            if (localStorageService.exists(file.getFileName()) &&
                    fileSize.equals(localStorageService.getFileSize("", file.getFileName()))) {
                File localFile = FileUtil.file(projectPath, file.getFileName());
                jars.add(localFile.toURI().toString());
            } else {
                InputStream is = storageService.get(file.getFilePath(), file.getFileName());
                File localFile = FileUtil.file(projectPath, file.getFileName());
                FileUtil.writeFromStream(is, localFile);
                jars.add(localFile.toURI().toString());
            }
        }
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        configuration.setString(DeploymentOptions.TARGET, RemoteExecutor.NAME);
        return configuration;
    }

    private PackageJarJob buildJob(String seatunnelPath, URL confFile, List<DiJobAttrDTO> jobAttrList) {
        PackageJarJob jarJob = new PackageJarJob();
        jarJob.setJarFilePath(seatunnelPath);
        jarJob.setEntryPointClass("org.apache.seatunnel.core.flink.SeatunnelFlink");
        List<String> variables = Arrays.asList("--config", confFile.toString());
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
}
