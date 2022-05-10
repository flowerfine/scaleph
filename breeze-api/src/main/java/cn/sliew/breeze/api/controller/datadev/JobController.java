package cn.sliew.breeze.api.controller.datadev;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.api.annotation.Logging;
import cn.sliew.breeze.api.schedule.FlinkJobStatusSyncJob;
import cn.sliew.breeze.api.schedule.ScheduleService;
import cn.sliew.breeze.api.util.I18nUtil;
import cn.sliew.breeze.api.util.SecurityUtil;
import cn.sliew.breeze.api.vo.DiJobAttrVO;
import cn.sliew.breeze.api.vo.DiJobRunVO;
import cn.sliew.breeze.api.vo.ResponseVO;
import cn.sliew.breeze.common.constant.Constants;
import cn.sliew.breeze.common.constant.DictConstants;
import cn.sliew.breeze.common.enums.*;
import cn.sliew.breeze.engine.util.JobConfigHelper;
import cn.sliew.breeze.service.admin.SystemConfigService;
import cn.sliew.breeze.service.di.*;
import cn.sliew.breeze.service.dto.di.*;
import cn.sliew.breeze.service.param.di.DiJobParam;
import cn.sliew.breeze.service.storage.StorageService;
import cn.sliew.breeze.service.storage.impl.NioFileServiceImpl;
import cn.sliew.breeze.service.vo.DictVO;
import cn.sliew.breeze.service.vo.JobGraphVO;
import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import cn.sliew.flinkful.rest.client.FlinkRestClient;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.JobStatus;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.job.savepoints.stop.StopWithSavepointRequestBody;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author gleiyu
 */
@Slf4j
@Api(tags = "数据开发-作业管理")
@RestController
@RequestMapping(path = "/api/datadev/job")
public class JobController {

    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiJobAttrService diJobAttrService;
    @Autowired
    private DiJobStepService diJobStepService;
    @Autowired
    private DiJobLinkService diJobLinkService;
    @Autowired
    private DiJobStepAttrService diJobStepAttrService;
    @Autowired
    private DiJobStepAttrTypeService diJobStepAttrTypeService;
    @Autowired
    private JobConfigHelper jobConfigHelper;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
    @Autowired
    private DiClusterConfigService diClusterConfigService;
    @Autowired
    private DiJobLogService diJobLogService;
    @Autowired
    private ScheduleService scheduleService;

    @Resource(name = "${app.resource.type}")
    private StorageService storageService;
    @Value("${app.engine.flink.state.savepoints.dir}")
    private String savePointDir;

    @PostConstruct
    public void syncJobStatus() throws SchedulerException {
        JobKey syncJobStatusKey = scheduleService.getJobKey("SYNC_JOB_STATUS_FROM_CLUSTER_JOB", Constants.INTERNAL_GROUP);
        JobDetail syncJob = JobBuilder.newJob(FlinkJobStatusSyncJob.class)
                .withIdentity(syncJobStatusKey)
                .storeDurably()
                .build();
        TriggerKey syncJobTriggerKey = scheduleService.getTriggerKey("SYNC_JOB_STATUS_FROM_CLUSTER_TRI", Constants.INTERNAL_GROUP);
        Trigger syncJobTri = TriggerBuilder.newTrigger()
                .withIdentity(syncJobTriggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(Constants.CRON_EVERY_THREE_SECONDS))
                .build();
        if (scheduleService.checkExists(syncJobStatusKey)) {
            scheduleService.deleteScheduleJob(syncJobStatusKey);
        }
        this.scheduleService.addScheduleJob(syncJob, syncJobTri);
    }

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询作业列表", notes = "分页查询作业列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_SELECT)")
    public ResponseEntity<Page<DiJobDTO>> listJob(DiJobParam param) {
        Page<DiJobDTO> page = this.diJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增作业记录", notes = "新增一条作业记录，相关流程定义不涉及")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_ADD)")
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody DiJobDTO diJobDTO) {
        String currentUser = SecurityUtil.getCurrentUserName();
        diJobDTO.setJobOwner(currentUser);
        diJobDTO.setJobStatus(new DictVO(JobStatusEnum.DRAFT.getValue(), JobStatusEnum.DRAFT.getLabel()));
        diJobDTO.setRuntimeState(new DictVO(JobRuntimeStateEnum.STOP.getValue(), JobRuntimeStateEnum.STOP.getLabel()));
        diJobDTO.setJobVersion(1);
        this.diJobService.insert(diJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(diJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改作业记录", notes = "只修改作业记录属性，相关流程定义不改变")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody DiJobDTO diJobDTO) {
        this.diJobService.update(diJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "删除作业", notes = "删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable(value = "id") Long id) {
        DiJobDTO job = this.diJobService.selectOne(id);
        if (job == null) {
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else if (JobRuntimeStateEnum.STOP.getValue().equals(job.getRuntimeState().getValue())) {
            this.diJobService.deleteByCode(job.getJobCode());
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.runningJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping(path = "/batch")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "批量删除作业", notes = "批量删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody Map<Integer, String> map) {
        List<DiJobDTO> list = this.diJobService.listById(map.values());
        if (CollectionUtil.isEmpty(list)) {
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        }
        //任意作业不是停止状态则不能删除
        boolean flag = true;
        for (DiJobDTO dto : list) {
            if (!JobRuntimeStateEnum.STOP.getValue().equals(dto.getRuntimeState().getValue())) {
                flag = false;
            }
        }
        if (flag) {
            this.diJobService.deleteByCode(list);
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.runningJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
    }


    @Logging
    @GetMapping(path = "/detail")
    @ApiOperation(value = "查询作业详情", notes = "查询作业详情，包含作业流程定义信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_SELECT)")
    public ResponseEntity<DiJobDTO> getJobDetail(@RequestParam(value = "id") Long id) {
        DiJobDTO job = queryJobInfo(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }


    @Logging
    @PostMapping(path = "/detail")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "保存作业详情", notes = "保存作业相关流程定义")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@Validated @RequestBody DiJobDTO diJobDTO) {
        DiJobDTO job = this.diJobService.selectOne(diJobDTO.getId());
        if (JobStatusEnum.RELEASE.getValue().equals(job.getJobStatus().getValue())) {
            int jobVersion = job.getJobVersion() + 1;
            job.setId(null);
            job.setJobVersion(jobVersion);
            job.setJobStatus(DictVO.toVO(DictConstants.JOB_STATUS, JobStatusEnum.DRAFT.getValue()));
            DiJobDTO newJob = this.diJobService.insert(job);
            diJobDTO.setId(newJob.getId());

        } else if (JobStatusEnum.ARCHIVE.getValue().equals(job.getJobStatus().getValue())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.archivedJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        saveJobGraph(diJobDTO.getJobGraph(), diJobDTO.getId());
        return new ResponseEntity<>(ResponseVO.sucess(diJobDTO.getId()), HttpStatus.CREATED);
    }

    private String getStepAttrByKey(JobGraphVO graph, String key, String defaultValue) {
        if (graph == null) {
            return defaultValue;
        }
        Map<String, Object> dataList = graph.getData();
        if (CollectionUtil.isNotEmpty(dataList) && dataList.containsKey(key)) {
            return String.valueOf(dataList.get(key));
        } else {
            return defaultValue;
        }
    }

    private Integer getPositionByKey(JobGraphVO graph, String key, Integer defaultValue) {
        if (graph == null) {
            return defaultValue;
        }
        Map<String, Integer> position = graph.getPosition();
        if (CollectionUtil.isNotEmpty(position) && position.containsKey(key)) {
            return position.get(key);
        } else {
            return defaultValue;
        }
    }

    private void saveJobGraph(Map<String, List<JobGraphVO>> jobGraph, Long jobId) {
        String cellKey = "cells";
        String stepShape = "angular-shape";
        String linkShape = "edge";
        if (CollectionUtil.isNotEmpty(jobGraph)) {
            Map<String, List<JobGraphVO>> map = jobGraph;
            if (map.containsKey(cellKey)) {
                List<JobGraphVO> list = map.get(cellKey);
                // 清除途中已删除的连线信息
                List<String> linkList = list.stream()
                        .filter(j -> linkShape.equals(j.getShape()))
                        .map(JobGraphVO::getId)
                        .collect(Collectors.toList());
                this.diJobLinkService.deleteSurplusLink(jobId, linkList);
                //清除图中已删除的节点信息及节点属性
                List<String> stepList = list.stream()
                        .filter(j -> stepShape.equals(j.getShape()))
                        .map(JobGraphVO::getId)
                        .collect(Collectors.toList());
                this.diJobStepService.deleteSurplusStep(jobId, stepList);
                if (CollectionUtil.isNotEmpty(list)) {
                    for (JobGraphVO graph : list) {
                        if (stepShape.equals(graph.getShape())) {
                            //插入新的，更新已有的 这里不处理节点属性信息
                            DiJobStepDTO jobStep = new DiJobStepDTO();
                            jobStep.setJobId(jobId);
                            jobStep.setStepCode(graph.getId());
                            jobStep.setStepTitle(getStepAttrByKey(graph, "title", ""));
                            String type = getStepAttrByKey(graph, "type", "");
                            jobStep.setStepType(DictVO.toVO(DictConstants.JOB_STEP_TYPE, type));
                            jobStep.setStepName(getStepAttrByKey(graph, "name", ""));
                            jobStep.setPositionX(getPositionByKey(graph, "x", 0));
                            jobStep.setPositionY(getPositionByKey(graph, "y", 0));
                            this.diJobStepService.upsert(jobStep);
                        }
                        if (linkShape.equals(graph.getShape())) {
                            //插入新的
                            DiJobLinkDTO jobLink = new DiJobLinkDTO();
                            jobLink.setLinkCode(graph.getId());
                            jobLink.setJobId(jobId);
                            jobLink.setFromStepCode(graph.getSource().getCell());
                            jobLink.setToStepCode(graph.getTarget().getCell());
                            this.diJobLinkService.upsert(jobLink);
                        }
                    }
                }
            }
        }
    }

    @Logging
    @GetMapping(path = "/attr/{jobId}")
    @ApiOperation(value = "查询作业属性", notes = "查询作业属性列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<DiJobAttrVO> listJobAttr(@PathVariable(value = "jobId") Long jobId) {
        DiJobAttrVO vo = new DiJobAttrVO();
        vo.setJobId(jobId);
        List<DiJobAttrDTO> list = this.diJobAttrService.listJobAttr(jobId);
        for (DiJobAttrDTO jobAttr : list) {
            String str = jobAttr.getJobAttrKey().concat("=").concat(jobAttr.getJobAttrValue());
            if (JobAttrTypeEnum.JOB_ATTR.getValue().equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getJobAttr()) ? "" : vo.getJobAttr();
                vo.setJobAttr(tempStr + str + "\n");
            } else if (JobAttrTypeEnum.JOB_PROP.getValue().equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getJobProp()) ? "" : vo.getJobProp();
                vo.setJobProp(tempStr + str + "\n");
            } else if (JobAttrTypeEnum.ENGINE_PROP.getValue().equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getEngineProp()) ? "" : vo.getEngineProp();
                vo.setEngineProp(tempStr + str + "\n");
            }
        }
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/attr")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改作业属性", notes = "修改作业属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobAttr(@RequestBody DiJobAttrVO jobAttrVO) {
        DiJobDTO jobInfo = this.diJobService.selectOne(jobAttrVO.getJobId());
        if (JobStatusEnum.ARCHIVE.getValue().equals(jobInfo.getJobStatus().getValue())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.archivedJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        Map<String, DiJobAttrDTO> map = new HashMap<>();
        DictVO jobAttrtype = DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.JOB_ATTR.getValue());
        DictVO jobProptype = DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.JOB_PROP.getValue());
        DictVO engineProptype = DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.ENGINE_PROP.getValue());
        parseJobAttr(map, jobAttrVO.getJobAttr(), jobAttrtype, jobAttrVO.getJobId());
        parseJobAttr(map, jobAttrVO.getJobProp(), jobProptype, jobAttrVO.getJobId());
        parseJobAttr(map, jobAttrVO.getEngineProp(), engineProptype, jobAttrVO.getJobId());
        this.diJobAttrService.deleteByJobId(new ArrayList<Long>() {{
            add(jobAttrVO.getJobId());
        }});
        for (Map.Entry<String, DiJobAttrDTO> entry : map.entrySet()) {
            this.diJobAttrService.upsert(entry.getValue());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    private void parseJobAttr(Map<String, DiJobAttrDTO> map, String str, DictVO jobAttrType, Long jobId) {
        if (StrUtil.isNotEmpty(str)) {
            String[] lines = str.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StrUtil.isAllNotBlank(kv)) {
                    DiJobAttrDTO dto = new DiJobAttrDTO();
                    dto.setJobId(jobId);
                    dto.setJobAttrType(jobAttrType);
                    dto.setJobAttrKey(kv[0]);
                    dto.setJobAttrValue(kv[1]);
                    map.put(jobId + jobAttrType.getValue() + kv[0], dto);
                }
            }
        }
    }


    @Logging
    @GetMapping(path = "/attrType")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "查询步骤属性列表", notes = "查询步骤属性列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<List<DiJobStepAttrTypeDTO>> listJobStepAttrType(@NotBlank String stepType, @NotBlank String stepName) {
        List<DiJobStepAttrTypeDTO> list = this.diJobStepAttrTypeService.listByType(stepType, stepName);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/step")
    @ApiOperation(value = "查询步骤属性信息", notes = "查询步骤属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<List<DiJobStepAttrDTO>> listDiJobStepAttr(@NotBlank String jobId, @NotBlank String stepCode) {
        List<DiJobStepAttrDTO> list = this.diJobStepAttrService.listJobStepAttr(Long.valueOf(jobId), stepCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/step")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "保存步骤属性信息", notes = "保存步骤属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(@RequestBody Map<String, Object> stepAttrMap) {
        if (isStepAttrMapValid(stepAttrMap)) {
            Long jobId = Long.valueOf(stepAttrMap.get(Constants.JOB_ID).toString());
            String stepCode = stepAttrMap.get(Constants.JOB_STEP_CODE).toString();
            String jobGraphStr = toJsonStr(stepAttrMap.get(Constants.JOB_GRAPH));
            Map<String, List<JobGraphVO>> map = JSONUtil.toBean(jobGraphStr,
                    new TypeReference<Map<String, List<JobGraphVO>>>() {
                    }, false);
            saveJobGraph(map, jobId);
            if (stepAttrMap.containsKey(Constants.JOB_STEP_TITLE)
                    && StrUtil.isNotEmpty(stepAttrMap.get(Constants.JOB_STEP_TITLE).toString())) {
                DiJobStepDTO step = new DiJobStepDTO();
                step.setJobId(jobId);
                step.setStepCode(stepCode);
                step.setStepTitle(stepAttrMap.get(Constants.JOB_STEP_TITLE).toString());
                this.diJobStepService.update(step);
            }
            DiJobStepDTO dto = this.diJobStepService.selectOne(jobId, stepCode);
            if (dto != null) {
                List<DiJobStepAttrTypeDTO> attrTypeList = this.diJobStepAttrTypeService.listByType(dto.getStepType().getValue(), dto.getStepName());
                for (DiJobStepAttrTypeDTO attrType : attrTypeList) {
                    if (stepAttrMap.containsKey(attrType.getStepAttrKey())) {
                        DiJobStepAttrDTO stepAttr = new DiJobStepAttrDTO();
                        stepAttr.setJobId(jobId);
                        stepAttr.setStepCode(stepCode);
                        stepAttr.setStepAttrKey(attrType.getStepAttrKey());
                        stepAttr.setStepAttrValue(toJsonStr(stepAttrMap.get(attrType.getStepAttrKey())));
                        this.diJobStepAttrService.upsert(stepAttr);
                    } else {
                        DiJobStepAttrDTO stepAttr = new DiJobStepAttrDTO();
                        stepAttr.setJobId(jobId);
                        stepAttr.setStepCode(stepCode);
                        stepAttr.setStepAttrKey(attrType.getStepAttrKey());
                        stepAttr.setStepAttrValue(attrType.getStepAttrDefaultValue());
                        this.diJobStepAttrService.upsert(stepAttr);
                    }
                }
            }
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    private String toJsonStr(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return String.valueOf(obj);
        }
        return JSONUtil.toJsonStr(obj);
    }

    /**
     * 判断作业属性信息是否有效，必须要包含JOB_ID JOB_STEP_CODE JOB_GRAPH
     *
     * @param stepAttrMap map
     * @return boolean
     */
    private boolean isStepAttrMapValid(Map<String, Object> stepAttrMap) {
        if (CollectionUtil.isEmpty(stepAttrMap)) {
            return false;
        }
        return stepAttrMap.containsKey(Constants.JOB_ID)
                && StrUtil.isNotEmpty(toJsonStr(stepAttrMap.get(Constants.JOB_ID)))
                && stepAttrMap.containsKey(Constants.JOB_STEP_CODE)
                && StrUtil.isNotEmpty(toJsonStr(stepAttrMap.get(Constants.JOB_STEP_CODE)))
                && stepAttrMap.containsKey(Constants.JOB_GRAPH)
                && StrUtil.isNotEmpty(toJsonStr(stepAttrMap.get(Constants.JOB_GRAPH)))
                ;
    }

    @Logging
    @GetMapping(path = "/publish/{jobId}")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "发布任务", notes = "发布任务")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> publishJob(@PathVariable(value = "jobId") Long jobId) {
        DiJobDTO jobInfo = this.diJobService.selectOne(jobId);
        if (JobStatusEnum.ARCHIVE.getValue().equals(jobInfo.getJobStatus().getValue())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.archivedJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        if (JobRuntimeStateEnum.STOP.getValue().equals(jobInfo.getRuntimeState().getValue())) {
            DiJobDTO job = new DiJobDTO();
            job.setId(jobId);
            job.setJobStatus(DictVO.toVO(DictConstants.JOB_STATUS, JobStatusEnum.RELEASE.getValue()));
            this.diJobService.update(job);
            this.diJobService.archive(jobInfo.getJobCode());
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.publishJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping(path = "/run")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "运行任务", notes = "运行任务，提交至集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> runJob(@RequestBody DiJobRunVO jobRunParam) throws Exception {
        //config cluster and resource
        this.diJobService.update(jobRunParam.toDto());
        this.diJobResourceFileService.bindResource(jobRunParam.getJobId(), jobRunParam.getResources());
        //generate json file
        DiJobDTO job = queryJobInfo(jobRunParam.getJobId());
        String jobJson = jobConfigHelper.buildJob(job);
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File baseDir = FileUtil.mkdir(tempDir.getAbsolutePath() + File.separator + job.getProjectId());
        File tmpJobConfFile = FileUtil.file(baseDir, job.getJobCode() + ".json");
        FileUtil.writeUtf8String(jobJson, tmpJobConfFile);
        String seatunnelPath = this.systemConfigService.getSeatunnelHome();
        Path seatunnelJarPath = Paths.get(seatunnelPath, "lib", "seatunnel-core-flink.jar");
        if (StrUtil.isBlank(seatunnelPath)) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.noJar.seatunnel"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        CliClient client = new DescriptorCliClient();
        //build configuration
        DiClusterConfigDTO clusterConfig = this.diClusterConfigService.selectOne(jobRunParam.getClusterId());
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
        jobLogInfo.setClusterId(jobRunParam.getClusterId());
        jobLogInfo.setJobInstanceId(jobInstanceID.toString());
        jobLogInfo.setJobInstanceState(DictVO.toVO(DictConstants.JOB_INSTANCE_STATE, JobStatus.INITIALIZING.toString()));
        jobLogInfo.setStartTime(new Date());
        this.diJobService.update(job);
        this.diJobLogService.insert(jobLogInfo);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/stop")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "停止任务", notes = "停止任务,自动创建savepoint,作业可能会正常运行完后停止。任务的日志状态通过定时任务同步")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<ResponseVO> stopJob(@RequestParam(value = "jobId") Long jobId) throws Exception {
        DiJobDTO job = this.diJobService.selectOne(jobId);
        List<DiJobLogDTO> list = this.diJobLogService.listRunningJobInstance(job.getJobCode());
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        for (DiJobLogDTO instance : list) {
            DiClusterConfigDTO clusterConfig = this.diClusterConfigService.selectOne(instance.getClusterId());
            String host = clusterConfig.getConfig().get(JobManagerOptions.ADDRESS.key());
            int restPort = Integer.parseInt(clusterConfig.getConfig().get(RestOptions.PORT.key()));
            RestClient client = new FlinkRestClient(host, restPort, configuration);
            JobClient jobClient = client.job();
            if (StrUtil.isBlank(savePointDir)) {
                return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                        I18nUtil.get("response.error.di.cluster.flink.savepoint"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
            }
            if (savePointDir.endsWith("/")) {
                savePointDir = savePointDir.substring(0, savePointDir.length() - 1);
            }
            StopWithSavepointRequestBody requestBody = new StopWithSavepointRequestBody(
                    StrUtil.concat(true, savePointDir, "/", clusterConfig.getClusterName(), "/", instance.getJobInstanceId()),
                    true);
            final CompletableFuture<TriggerResponse> future = jobClient.jobStop(instance.getJobInstanceId(), requestBody);
            future.get();
        }
        job.setRuntimeState(DictVO.toVO(DictConstants.RUNTIME_STATE, JobRuntimeStateEnum.STOP.getValue()));
        this.diJobService.update(job);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/resource/{jobId}")
    @ApiOperation(value = "查询作业资源", notes = "查询作业资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_JOB_EDIT)")
    public ResponseEntity<List<DictVO>> listJobResourceFile(@PathVariable(value = "jobId") Long jobId) {
        List<DictVO> list = new ArrayList<>();
        List<DiResourceFileDTO> resourceList = this.diJobResourceFileService.listJobResources(jobId);
        for (DiResourceFileDTO dto : resourceList) {
            DictVO dict = new DictVO(String.valueOf(dto.getId()), dto.getFileName());
            list.add(dict);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
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

    private DiJobDTO queryJobInfo(Long jobId) {
        DiJobDTO job = this.diJobService.selectOne(jobId);
        List<DiJobAttrDTO> jobAttrList = this.diJobAttrService.listJobAttr(jobId);
        List<DiJobLinkDTO> jobLinkList = this.diJobLinkService.listJobLink(jobId);
        List<DiJobStepDTO> jobStepList = this.diJobStepService.listJobStep(jobId);
        job.setJobAttrList(jobAttrList);
        job.setJobLinkList(jobLinkList);
        job.setJobStepList(jobStepList);
        return job;
    }

}