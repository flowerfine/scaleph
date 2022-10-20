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

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.enums.*;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.core.di.service.DiJobAttrService;
import cn.sliew.scaleph.core.di.service.DiJobResourceFileService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobAddParam;
import cn.sliew.scaleph.core.di.service.param.DiJobParam;
import cn.sliew.scaleph.core.di.service.param.DiJobStepParam;
import cn.sliew.scaleph.core.di.service.param.DiJobUpdateParam;
import cn.sliew.scaleph.core.di.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.core.di.service.vo.DiJobRunVO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * todo split job to crud and run, stop, schedule
 * job crud
 * job graph
 * job attr
 * job resource
 * job run, stop, schedule
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
    private DiJobAttrService diJobAttrService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
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
    public ResponseEntity<ResponseVO> simpleAddJob(@Validated @RequestBody DiJobAddParam param) {
        DiJobDTO diJobDTO = diJobService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(diJobDTO), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改作业记录", notes = "只修改作业记录属性，相关流程定义不改变")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> simpleEditJob(@Validated @RequestBody DiJobUpdateParam param) {
        diJobService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除作业", notes = "删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@PathVariable("id") Long id) {
        diJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除作业", notes = "批量删除作业")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_DELETE)")
    public ResponseEntity<ResponseVO> deleteJob(@RequestBody List<Long> ids) {
        diJobService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
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
    @ApiOperation(value = "保存作业详情", notes = "保存作业相关流程定义，如果已经有对应版本号的数据，则提醒用户编辑最新版本。")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobDetail(@Validated @RequestBody DiJobDTO diJobDTO) throws ScalephException {
        Long editableJobId = diJobService.saveJobGraph(diJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.CREATED);
    }


    private Long prepareJobVersion(DiJobDTO job) throws ScalephException {
        return job.getId();
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
    @PostMapping(path = "/step")
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @ApiOperation(value = "保存步骤属性信息", notes = "保存步骤属性信息，未触发作业版本号变更")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_EDIT)")
    public ResponseEntity<ResponseVO> saveJobStepInfo(@Valid @RequestBody DiJobStepParam param) throws ScalephException {
        Long editableJobId = diJobService.saveJobStep(param);
        return new ResponseEntity<>(ResponseVO.sucess(editableJobId), HttpStatus.OK);
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
            job.setJobStatus(JobStatus.RELEASE);
//            this.diJobService.update(job);
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
    @GetMapping(path = "/node/meta")
    @ApiOperation(value = "查询DAG节点元信息", notes = "后端统一返回节点信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_JOB_SELECT)")
    public ResponseEntity<List<DagPanelDTO>> loadNodeMeta() {
        List<DagPanelDTO> list = this.seatunnelJobService.loadDndPanelInfo();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}