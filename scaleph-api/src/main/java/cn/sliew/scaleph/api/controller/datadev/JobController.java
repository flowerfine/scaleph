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

package cn.sliew.scaleph.api.controller.datadev;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.SecurityUtil;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.*;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.core.di.service.param.DiJobParam;
import cn.sliew.scaleph.core.di.service.vo.*;
import cn.sliew.scaleph.core.scheduler.service.ScheduleService;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.engine.seatunnel.service.util.QuartzJobUtil;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.primitives.Primitives;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * todo split job to crud and run, stop, schedule
 *
 * @author gleiyu
 */
@Slf4j
@Api(tags = "数据开发-作业管理")
@RestController
@RequestMapping(path = {"/api/datadev/job", "/api/di/job"})
public class JobController {

    @Autowired
    private DiJobService diJobService;
    @Autowired
    private DiProjectService diProjectService;
    @Autowired
    private DiJobAttrService diJobAttrService;
    @Autowired
    private DiJobStepService diJobStepService;
    @Autowired
    private DiJobLinkService diJobLinkService;
    @Autowired
    private DiJobStepAttrService diJobStepAttrService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private SeatunnelJobService seatunnelJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询作业列表", notes = "分页查询作业列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<Page<DiJobDTO>> listJob(@Valid DiJobParam param) {
        Page<DiJobDTO> page = diJobService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增作业记录", notes = "新增一条作业记录，相关流程定义不涉及")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_ADD)")
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody DiJobDTO diJobDTO) {
        String currentUser = SecurityUtil.getCurrentUserName();
        diJobDTO.setJobOwner(currentUser);
        diJobDTO.setJobStatus(
                new DictVO(JobStatusEnum.DRAFT.getValue(), JobStatusEnum.DRAFT.getLabel()));
        diJobDTO.setRuntimeState(
                new DictVO(JobRuntimeStateEnum.STOP.getValue(), JobRuntimeStateEnum.STOP.getLabel()));
        diJobDTO.setJobVersion(1);
        diJobService.insert(diJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(diJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改作业记录", notes = "只修改作业记录属性，相关流程定义不改变。如果作业在运行中，且修改了crontab表达式，则重新配置作业的调度频率")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody DiJobDTO diJobDTO)
            throws SchedulerException {
        DiJobDTO job = this.diJobService.selectOne(diJobDTO.getId());
        boolean flag = StrUtil.isAllEmpty(diJobDTO.getJobCrontab(), job.getJobCrontab())
                || (StrUtil.isAllNotEmpty(diJobDTO.getJobCrontab(), job.getJobCrontab())
                && StrUtil.equals(diJobDTO.getJobCrontab(), job.getJobCrontab()));
        if (!flag) {
            DiProjectDTO project = this.diProjectService.selectOne(job.getProjectId());
            String jobName = project.getProjectCode() + '_' + job.getJobCode();
            JobKey seatunnelJobKey = scheduleService.getJobKey(QuartzJobUtil.getFlinkBatchJobName(jobName), Constants.INTERNAL_GROUP);
            if (scheduleService.checkExists(seatunnelJobKey)) {
                scheduleService.deleteScheduleJob(seatunnelJobKey);
            }
        }
        this.diJobService.update(diJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "删除作业", notes = "删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable(value = "id") Long id) {
        DiJobDTO job = this.diJobService.selectOne(id);
        if (job == null) {
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else if (JobRuntimeStateEnum.STOP.getValue().equals(job.getRuntimeState().getValue())) {
            this.diJobService.deleteByCode(job.getProjectId(), job.getJobCode());
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.job.running"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping(path = "/batch")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "批量删除作业", notes = "批量删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody Map<Integer, String> map) {
        List<DiJobDTO> list = this.diJobService.listById(map.values());
        if (CollectionUtil.isEmpty(list)) {
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        }
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
                    I18nUtil.get("response.error.di.job.running"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
    }

    @Logging
    @GetMapping(path = "/detail")
    @ApiOperation(value = "查询作业详情", notes = "查询作业详情，包含作业流程定义信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<DiJobDTO> getJobDetail(@RequestParam(value = "id") Long id) {
        DiJobDTO job = seatunnelJobService.queryJobInfo(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/detail")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "保存作业详情", notes = "保存作业相关流程定义，如果已经有对应版本号的数据，则提醒用户编辑最新版本。")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@Validated @RequestBody DiJobDTO diJobDTO) {
        DiJobDTO job = this.diJobService.selectOne(diJobDTO.getId());
        try {
            Long editableJobId = prepareJobVersion(job);
            saveJobGraph(diJobDTO.getJobGraph(), editableJobId);
            return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.CREATED);
        } catch (ScalephException e) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    e.getMessage(), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
    }

    /**
     * 编辑前检查作业的版本，确认是否需要生成新的可编辑版本
     *
     * @param job job info
     * @return job id
     */
    private Long prepareJobVersion(DiJobDTO job) throws ScalephException {
        if (JobStatusEnum.ARCHIVE.getValue().equals(job.getJobStatus().getValue())) {
            throw new ScalephException(I18nUtil.get("response.error.di.job.lowVersion"));
        }

        if (JobStatusEnum.RELEASE.getValue().equals(job.getJobStatus().getValue())) {
            Long oldJobId = job.getId();
            int jobVersion = job.getJobVersion() + 1;
            DiJobDTO newVersionJob = diJobService.selectOne(job.getProjectId(), job.getJobCode(), jobVersion);
            if (newVersionJob != null) {
                throw new ScalephException(I18nUtil.get("response.error.di.job.lowVersion"));
            }
            job.setId(null);
            job.setJobVersion(jobVersion);
            job.setJobStatus(DictVO.toVO(DictConstants.JOB_STATUS, JobStatusEnum.DRAFT.getValue()));
            DiJobDTO newJob = diJobService.insert(job);
            diJobService.clone(oldJobId, newJob.getId());
            return newJob.getId();
        }

        return job.getId();
    }


    private void saveJobGraph(JobGraphVO jobGraph, Long jobId) {
        if (jobGraph != null) {
            List<NodeCellVO> nodes = jobGraph.getNodes();
            List<EdgeCellVO> edges = jobGraph.getEdges();
            //清除图中已删除的连线信息
            this.diJobLinkService.deleteSurplusLink(jobId,
                    edges.stream()
                            .map(EdgeCellVO::getId)
                            .collect(Collectors.toList()));
            //插入新的
            for (EdgeCellVO edge : edges) {
                DiJobLinkDTO jobLink = new DiJobLinkDTO();
                jobLink.setLinkCode(edge.getId());
                jobLink.setJobId(jobId);
                jobLink.setFromStepCode(edge.getSource());
                jobLink.setToStepCode(edge.getTarget());
                this.diJobLinkService.upsert(jobLink);
            }
            //清除图中已删除的节点信息及节点属性
            this.diJobStepService.deleteSurplusStep(jobId,
                    nodes.stream()
                            .map(NodeCellVO::getId)
                            .collect(Collectors.toList()));
            //插入新的，更新已有的 这里不处理节点属性信息
            for (NodeCellVO node : nodes) {
                DiJobStepDTO jobStep = new DiJobStepDTO();
                jobStep.setJobId(jobId);
                jobStep.setStepCode(node.getId());
                jobStep.setStepTitle(node.getLabel());
                jobStep.setStepType(DictVO.toVO(DictConstants.JOB_STEP_TYPE, node.getData().get("type")));
                jobStep.setStepName(node.getData().get("name"));
                jobStep.setPositionX(node.getX());
                jobStep.setPositionY(node.getY());
                this.diJobStepService.upsert(jobStep);
            }
        }
    }

    @Logging
    @GetMapping(path = "/attr/{jobId}")
    @ApiOperation(value = "查询作业属性", notes = "查询作业属性列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<DiJobAttrVO> listJobAttr(@PathVariable(value = "jobId") Long jobId) {
        DiJobAttrVO vo = new DiJobAttrVO();
        vo.setJobId(jobId);
        List<DiJobAttrDTO> list = this.diJobAttrService.listJobAttr(jobId);
        for (DiJobAttrDTO jobAttr : list) {
            String str = jobAttr.getJobAttrKey().concat("=").concat(jobAttr.getJobAttrValue());
            if (JobAttrTypeEnum.JOB_ATTR.getValue().equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getJobAttr()) ? "" : vo.getJobAttr();
                vo.setJobAttr(tempStr + str + "\n");
            } else if (JobAttrTypeEnum.JOB_PROP.getValue()
                    .equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getJobProp()) ? "" : vo.getJobProp();
                vo.setJobProp(tempStr + str + "\n");
            } else if (JobAttrTypeEnum.ENGINE_PROP.getValue()
                    .equals(jobAttr.getJobAttrType().getValue())) {
                String tempStr = StrUtil.isEmpty(vo.getEngineProp()) ? "" : vo.getEngineProp();
                vo.setEngineProp(tempStr + str + "\n");
            }
        }
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/attr")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "修改作业属性", notes = "修改作业属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobAttr(@RequestBody DiJobAttrVO jobAttrVO) {
        DiJobDTO jobInfo = this.diJobService.selectOne(jobAttrVO.getJobId());
        try {
            Long editableJobId = prepareJobVersion(jobInfo);
            Map<String, DiJobAttrDTO> map = new HashMap<>();
            DictVO jobAttrtype =
                    DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.JOB_ATTR.getValue());
            DictVO jobProptype =
                    DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.JOB_PROP.getValue());
            DictVO engineProptype =
                    DictVO.toVO(DictConstants.JOB_ATTR_TYPE, JobAttrTypeEnum.ENGINE_PROP.getValue());
            parseJobAttr(map, jobAttrVO.getJobAttr(), jobAttrtype, editableJobId);
            parseJobAttr(map, jobAttrVO.getJobProp(), jobProptype, editableJobId);
            parseJobAttr(map, jobAttrVO.getEngineProp(), engineProptype, editableJobId);
            this.diJobAttrService.deleteByJobId(Collections.singletonList(editableJobId));
            for (Map.Entry<String, DiJobAttrDTO> entry : map.entrySet()) {
                this.diJobAttrService.upsert(entry.getValue());
            }
            return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.OK);
        } catch (ScalephException e) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    e.getMessage(), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }

    }

    private void parseJobAttr(Map<String, DiJobAttrDTO> map, String str, DictVO jobAttrType,
                              Long jobId) {
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
    @GetMapping(path = "/step")
    @ApiOperation(value = "查询步骤属性信息", notes = "查询步骤属性信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<DiJobStepAttrDTO>> listDiJobStepAttr(@NotBlank String jobId,
                                                                    @NotBlank String stepCode) {
        List<DiJobStepAttrDTO> list =
                this.diJobStepAttrService.listJobStepAttr(Long.valueOf(jobId), stepCode);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/step")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "保存步骤属性信息", notes = "保存步骤属性信息，未触发作业版本号变更")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(
            @RequestBody Map<String, Object> stepAttrMap) {
        if (isStepAttrMapValid(stepAttrMap)) {
            Long jobId = Long.valueOf(stepAttrMap.get(Constants.JOB_ID).toString());
            DiJobDTO jobInfo = this.diJobService.selectOne(jobId);
            try {
                Long editableJobId = prepareJobVersion(jobInfo);
                String stepCode = stepAttrMap.get(Constants.JOB_STEP_CODE).toString();
                String jobGraphStr = toJsonStr(stepAttrMap.get(Constants.JOB_GRAPH));
                JobGraphVO jobGraphVO = JSONUtil.toBean(jobGraphStr, JobGraphVO.class);
                saveJobGraph(jobGraphVO, editableJobId);
                if (stepAttrMap.containsKey(Constants.JOB_STEP_TITLE)
                        && StrUtil.isNotEmpty(stepAttrMap.get(Constants.JOB_STEP_TITLE).toString())) {
                    DiJobStepDTO step = new DiJobStepDTO();
                    step.setJobId(editableJobId);
                    step.setStepCode(stepCode);
                    step.setStepTitle(stepAttrMap.get(Constants.JOB_STEP_TITLE).toString());
                    this.diJobStepService.update(step);
                }
                //insert step attrs
                stepAttrMap.forEach((k, v) -> {
                    if (!(k.equals(Constants.JOB_ID)
                            || k.equals(Constants.JOB_GRAPH)
                            || k.equals(Constants.JOB_STEP_CODE)
                            || k.equals(Constants.JOB_STEP_TITLE))
                    ) {
                        DiJobStepAttrDTO stepAttr = new DiJobStepAttrDTO();
                        stepAttr.setJobId(editableJobId);
                        stepAttr.setStepCode(stepCode);
                        stepAttr.setStepAttrKey(k);
                        stepAttr.setStepAttrValue(toJsonStr(v));
                        if (!StringUtils.isEmpty(v)) {
                            this.diJobStepAttrService.upsert(stepAttr);
                        }
                    }
                });
                return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.OK);
            } catch (ScalephException e) {
                return new ResponseEntity<>(
                        ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                                e.getMessage(), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.job.step.attr.illegal"),
                    ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
    }

    private String toJsonStr(Object obj) {
        if (obj == null) {
            return null;
        }
        if (Primitives.isWrapperType(obj.getClass())) {
            return obj.toString();
        }
        if (obj.getClass().isPrimitive()) {
            return obj.toString();
        }
        return JSONUtil.toJsonStr(obj);
    }

    private boolean isPrimitive(Object obj) {
        if (obj == null) {
            return false;
        }
        return Primitives.isWrapperType(obj.getClass()) || obj.getClass().isPrimitive();
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
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "发布任务", notes = "发布任务")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> publishJob(@PathVariable(value = "jobId") Long jobId) {
        DiJobDTO jobInfo = this.diJobService.selectOne(jobId);
        if (JobStatusEnum.ARCHIVE.getValue().equals(jobInfo.getJobStatus().getValue())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.job.lowVersion"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        } else if (JobStatusEnum.RELEASE.getValue().equals(jobInfo.getJobStatus().getValue())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.job.published"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
        if (JobRuntimeStateEnum.STOP.getValue().equals(jobInfo.getRuntimeState().getValue())) {
            DiJobDTO job = new DiJobDTO();
            job.setId(jobId);
            job.setJobStatus(
                    DictVO.toVO(DictConstants.JOB_STATUS, JobStatusEnum.RELEASE.getValue()));
            this.diJobService.update(job);
            this.diJobService.archive(jobInfo.getProjectId(), jobInfo.getJobCode());
            return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.job.publish"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping(path = "/run")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "运行任务", notes = "运行任务，提交至集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> runJob(@RequestBody DiJobRunVO jobRunParam) throws Exception {
        seatunnelJobService.run(jobRunParam);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/stop")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "停止任务", notes = "停止任务,自动创建savepoint,作业可能会正常运行完后停止。任务的日志状态通过定时任务同步")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> stopJob(@RequestParam(value = "jobId") Long jobId) throws Exception {
        seatunnelJobService.stop(jobId);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/resource/{jobId}")
    @ApiOperation(value = "查询作业资源", notes = "查询作业资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<DictVO>> listJobResourceFile(@PathVariable("jobId") Long jobId) {
        List<DictVO> list = new ArrayList<>();
        List<DiResourceFileDTO> resourceList = diJobResourceFileService.listJobResources(jobId);
        for (DiResourceFileDTO dto : resourceList) {
            DictVO dict = new DictVO(String.valueOf(dto.getId()), dto.getFileName());
            list.add(dict);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/cron/next")
    @ApiOperation(value = "查询最近5次运行时间", notes = "查询最近5次运行时间")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<List<Date>> listNext5FireTime(@RequestParam("crontabStr") String crontabStr) {
        List<Date> dates;
        try {
            dates = scheduleService.listNext5FireTime(crontabStr);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(dates, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/node/meta")
    @ApiOperation(value = "查询DAG节点元信息", notes = "后端统一返回节点信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<List<DagPanelDTO>> loadNodeMeta() {
        List<DagPanelDTO> list = this.seatunnelJobService.loadDndPanelInfo();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}