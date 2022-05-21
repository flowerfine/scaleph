package cn.sliew.scaleph.api.schedule;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.scaleph.api.util.I18nUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.common.enums.JobRuntimeStateEnum;
import cn.sliew.scaleph.engine.util.JobConfigHelper;
import cn.sliew.scaleph.service.admin.SystemConfigService;
import cn.sliew.scaleph.service.di.DiClusterConfigService;
import cn.sliew.scaleph.service.di.DiJobLogService;
import cn.sliew.scaleph.service.di.DiJobResourceFileService;
import cn.sliew.scaleph.service.di.DiJobService;
import cn.sliew.scaleph.service.dto.di.*;
import cn.sliew.scaleph.service.storage.StorageService;
import cn.sliew.scaleph.service.storage.impl.NioFileServiceImpl;
import cn.sliew.scaleph.service.vo.DictVO;
import lombok.SneakyThrows;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class SeatunnelFlinkJob extends QuartzJobBean {
    @Autowired
    private JobConfigHelper jobConfigHelper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private DiClusterConfigService diClusterConfigService;
    @Resource(name = "${app.resource.type}")
    private StorageService storageService;
    @Autowired
    private DiJobLogService diJobLogService;
    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        DiJobDTO job = (DiJobDTO) dataMap.get(Constants.JOB_PARAM_JOB_INFO);
        String jobJson = jobConfigHelper.buildJob(job);
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File baseDir = FileUtil.mkdir(tempDir.getAbsolutePath() + File.separator + job.getProjectId());
        File tmpJobConfFile = FileUtil.file(baseDir, job.getJobCode() + ".json");
        FileUtil.writeUtf8String(jobJson, tmpJobConfFile);
        String seatunnelPath = this.systemConfigService.getSeatunnelHome();
        Path seatunnelJarPath = Paths.get(seatunnelPath, "lib", "seatunnel-core-flink.jar");
        if (StrUtil.isBlank(seatunnelPath)) {
            throw new JobExecutionException(I18nUtil.get("response.error.di.noJar.seatunnel"));
        }
        CliClient client = new DescriptorCliClient();
        //build configuration
        DiClusterConfigDTO clusterConfig = this.diClusterConfigService.selectOne(job.getClusterId());
        Configuration configuration = buildConfiguration(seatunnelPath, seatunnelJarPath, job, clusterConfig.getConfig(), baseDir);
        //build job
        PackageJarJob jarJob = buildJob(seatunnelJarPath.toUri().toString(), tmpJobConfFile, job.getJobAttrList());
        JobID jobInstanceID = client.submit(DeploymentTarget.STANDALONE_SESSION, configuration, jarJob);
        job.setRuntimeState(DictVO.toVO(DictConstants.RUNTIME_STATE, JobRuntimeStateEnum.RUNNING.getValue()));
        //write log
        DiJobLogDTO jobLogInfo = new DiJobLogDTO();
        jobLogInfo.setProjectId(job.getProjectId());
        jobLogInfo.setJobId(job.getId());
        jobLogInfo.setJobCode(job.getJobCode());
        jobLogInfo.setClusterId(job.getClusterId());
        jobLogInfo.setJobInstanceId(jobInstanceID.toString());
        String clusterHost = configuration.getString(JobManagerOptions.ADDRESS);
        int clusterPort = configuration.getInteger(RestOptions.PORT);
        String jobLogUrl = "http://" + clusterHost + ":" + clusterPort + "/#/job/" + jobInstanceID + "/overview";
        jobLogInfo.setJobLogUrl(jobLogUrl);
        jobLogInfo.setJobInstanceState(DictVO.toVO(DictConstants.JOB_INSTANCE_STATE, JobStatus.INITIALIZING.toString()));
        jobLogInfo.setStartTime(new Date());
        this.diJobService.update(job);
        this.diJobLogService.insert(jobLogInfo);
    }

    private Configuration buildConfiguration(String seatunnelPath, Path seatunnelJarPath, DiJobDTO job, Map<String, String> clusterConf, File baseDir) throws MalformedURLException {
        Configuration configuration = new Configuration();
        configuration.setString(PipelineOptions.NAME, job.getJobCode());
        configuration.setString(JobManagerOptions.ADDRESS, clusterConf.get(JobManagerOptions.ADDRESS.key()));
        configuration.setInteger(JobManagerOptions.PORT, Integer.parseInt(clusterConf.get(JobManagerOptions.PORT.key())));
        configuration.setInteger(RestOptions.PORT, Integer.parseInt(clusterConf.get(RestOptions.PORT.key())));
        List<DiResourceFileDTO> resourceList = this.diJobResourceFileService.listJobResources(job.getId());
        Set<String> jars = new TreeSet<>();
        Path seatunnelConnectorsPath = Paths.get(seatunnelPath, "connectors", "flink");
        File seatunnelConnectorDir = seatunnelConnectorsPath.toFile();
        for (DiJobStepDTO step : job.getJobStepList()) {
            String pluginTag = this.jobConfigHelper.getSeatunnelPluginTag(step.getStepType().getValue(), step.getStepName());
            FileFilter fileFilter = new RegexFileFilter(".*" + pluginTag + ".*");
            File[] pluginJars = seatunnelConnectorDir.listFiles(fileFilter);
            if (pluginJars != null) {
                for (File jar : pluginJars) {
                    jars.add(jar.toURI().toString());
                }
            }
        }
        jars.add(seatunnelJarPath.toUri().toString());
        StorageService localStorageService = new NioFileServiceImpl(baseDir.getAbsolutePath());
        for (DiResourceFileDTO file : resourceList) {
            Long fileSize = this.storageService.getFileSize(file.getFilePath(), file.getFileName());
            if (localStorageService.exists(file.getFileName()) && fileSize.equals(localStorageService.getFileSize("", file.getFileName()))) {
                File localFile = FileUtil.file(baseDir, file.getFileName());
                jars.add(localFile.toURI().toString());
            } else {
                InputStream is = this.storageService.get(file.getFilePath(), file.getFileName());
                File localFile = FileUtil.file(baseDir, file.getFileName());
                FileUtil.writeFromStream(is, localFile);
                jars.add(localFile.toURI().toString());
            }
        }
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        configuration.setString(DeploymentOptions.TARGET, RemoteExecutor.NAME);
        return configuration;
    }

    private PackageJarJob buildJob(String seatunnelPath, File file, List<DiJobAttrDTO> jobAttrList) throws FileNotFoundException, MalformedURLException {
        PackageJarJob jarJob = new PackageJarJob();
        jarJob.setJarFilePath(seatunnelPath);
        jarJob.setEntryPointClass("org.apache.seatunnel.core.flink.SeatunnelFlink");
        Path filePath = Paths.get(file.toURI());
        List<String> variables = new ArrayList<String>() {{
            add("--config");
            add(filePath.toString());
        }};
        jobAttrList.stream()
                .filter(attr -> JobAttrTypeEnum.JOB_ATTR.getValue().equals(attr.getJobAttrType().getValue()))
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
