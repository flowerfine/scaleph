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

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsDiJob;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDiJobMapper;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobAttrService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobGraphService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobConvert;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobAttrDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.WsDiJobGraphParam;
import cn.sliew.scaleph.engine.seatunnel.service.param.WsDiJobParam;
import cn.sliew.scaleph.engine.seatunnel.service.param.WsDiJobStepParam;
import cn.sliew.scaleph.engine.seatunnel.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.engine.seatunnel.service.vo.JobGraphVO;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

/**
 * @author gleiyu
 */
@Service
public class WsDiJobServiceImpl implements WsDiJobService {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private WsDiJobMapper diJobMapper;
    @Autowired
    private WsDiJobGraphService wsDiJobGraphService;
    @Autowired
    private WsDiJobAttrService wsDiJobAttrService;

    @Override
    public Page<WsDiJobDTO> listByPage(WsDiJobParam param) {
        Page<WsDiJob> jobs = diJobMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.toDo()
        );
        List<WsDiJobDTO> dtoList = WsDiJobConvert.INSTANCE.toDto(jobs.getRecords());
        Page<WsDiJobDTO> result = new Page<>(jobs.getCurrent(), jobs.getSize(), jobs.getTotal());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<WsDiJobDTO> listById(Collection<Long> ids) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>().in(WsDiJob::getId, ids);
        List<WsDiJob> jobs = diJobMapper.selectList(queryWrapper);
        List<WsDiJobDTO> dtoList = WsDiJobConvert.INSTANCE.toDto(jobs);
        return dtoList;
    }

    @Override
    public WsDiJobDTO selectOne(Long id) {
        WsDiJob record = diJobMapper.selectById(id);
        checkState(record != null, () -> "job not exists for id: " + id);
        WsDiJobDTO dto = WsDiJobConvert.INSTANCE.toDto(record);
        return dto;
    }

    @Override
    public WsDiJobDTO selectOne(Long projectId, Long jobCode, int jobVersion) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .eq(WsDiJob::getProjectId, projectId)
                .eq(WsDiJob::getJobCode, jobCode)
                .eq(WsDiJob::getJobVersion, jobVersion);
        WsDiJob job = diJobMapper.selectOne(queryWrapper);
        return WsDiJobConvert.INSTANCE.toDto(job);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public WsDiJobDTO insert(WsDiJobDTO param) throws UidGenerateException {
        WsDiJob record = WsDiJobConvert.INSTANCE.toDo(param);
        record.setJobCode(defaultUidGenerator.getUID());
        record.setJobStatus(JobStatus.DRAFT);
        record.setJobVersion(1);
        diJobMapper.insert(record);
        return selectOne(record.getId());
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int update(WsDiJobDTO param) {
        WsDiJob record = WsDiJobConvert.INSTANCE.toDo(param);
        return diJobMapper.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int delete(Long id) {
        WsDiJobDTO job = selectOne(id);
        //todo check if there is running job instance
        return deleteByCode(job.getProjectId(), job.getJobCode());
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int deleteByCode(Long projectId, Long jobCode) {
        List<WsDiJob> jobList = diJobMapper.selectList(new LambdaQueryWrapper<WsDiJob>()
                .eq(WsDiJob::getJobCode, jobCode)
                .eq(WsDiJob::getProjectId, projectId)
        );
        List<Long> ids = jobList.stream().map(WsDiJob::getId).collect(Collectors.toList());
        wsDiJobGraphService.deleteBatch(ids);
        wsDiJobAttrService.deleteByJobId(ids);
        return diJobMapper.delete(new LambdaQueryWrapper<WsDiJob>()
                .eq(WsDiJob::getJobCode, jobCode)
                .eq(WsDiJob::getProjectId, projectId)
        );
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int deleteByProjectId(Collection<Long> projectIds) {
        wsDiJobGraphService.deleteByProjectId(projectIds);
        wsDiJobAttrService.deleteByProjectId(projectIds);
        return diJobMapper.delete(new LambdaQueryWrapper<WsDiJob>()
                .in(WsDiJob::getProjectId, projectIds));
    }

    /**
     * 编辑前检查作业的版本，确认是否需要生成新的可编辑版本
     */
    private Long prepareJobVersion(Long id) throws ScalephException {
        WsDiJobDTO job = selectOne(id);
        switch (job.getJobStatus()) {
            case DRAFT:
                return id;
            case RELEASE:
                return riseJobVersion(job);
            case ARCHIVE:
                throw new ScalephException(I18nUtil.get("response.error.di.job.lowVersion"));
            default:
                throw new ScalephException("unknown job status for " + job.getJobStatus());
        }
    }

    private Long riseJobVersion(WsDiJobDTO job) {
        int jobVersion = job.getJobVersion() + 1;
        WsDiJobDTO newVersionJob = selectOne(job.getProjectId(), job.getJobCode(), jobVersion);
        checkState(newVersionJob == null, () -> I18nUtil.get("response.error.di.job.lowVersion"));

        WsDiJob record = WsDiJobConvert.INSTANCE.toDo(job);
        record.setId(null);
        record.setJobVersion(jobVersion);
        record.setJobStatus(JobStatus.DRAFT);
        diJobMapper.insert(record);
        clone(job.getId(), record.getId());
        return record.getId();
    }

    @Override
    public WsDiJobDTO queryJobGraph(Long id) {
        WsDiJobDTO job = selectOne(id);
        wsDiJobGraphService.queryJobGraph(job);
        job.setJobAttrList(wsDiJobAttrService.listJobAttr(id));
        return job;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobStep(WsDiJobStepParam param) throws ScalephException {
        Long editableJobId = prepareJobVersion(param.getJobId());

        JobGraphVO jobGraphVO = JacksonUtil.parseJsonString(param.getJobGraph(), JobGraphVO.class);
        wsDiJobGraphService.saveJobGraph(editableJobId, jobGraphVO);

        WsDiJobStepParam copiedParam = BeanUtil.copy(param, new WsDiJobStepParam());
        copiedParam.setJobId(editableJobId);
        wsDiJobGraphService.updateJobStep(copiedParam);

        return editableJobId;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobGraph(WsDiJobGraphParam param) throws ScalephException {
        Long editableJobId = prepareJobVersion(param.getJobId());
        wsDiJobGraphService.saveJobGraph(editableJobId, param.getJobGraph());
        return editableJobId;
    }

    @Override
    public DiJobAttrVO listJobAttrs(Long id) {
        DiJobAttrVO vo = new DiJobAttrVO();
        vo.setJobId(id);
        List<WsDiJobAttrDTO> list = wsDiJobAttrService.listJobAttr(id);
        for (WsDiJobAttrDTO jobAttr : list) {
            String str = jobAttr.getJobAttrKey().concat("=").concat(jobAttr.getJobAttrValue());
            String tempStr = null;
            switch (jobAttr.getJobAttrType()) {
                case VARIABLE:
                    tempStr = StringUtils.hasText(vo.getJobAttr()) ? vo.getJobAttr() : "";
                    vo.setJobAttr(tempStr + str + "\n");
                    break;
                case ENV:
                    tempStr = StringUtils.hasText(vo.getJobProp()) ? vo.getJobProp() : "";
                    vo.setJobProp(tempStr + str + "\n");
                    break;
                case PROPERTIES:
                    tempStr = StringUtils.hasText(vo.getEngineProp()) ? vo.getEngineProp() : "";
                    vo.setEngineProp(tempStr + str + "\n");
                    break;
                default:
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobAttrs(DiJobAttrVO vo) throws ScalephException {
        Long editableJobId = prepareJobVersion(vo.getJobId());
        Map<String, WsDiJobAttrDTO> map = new HashMap<>();
        parseJobAttr(map, vo.getJobAttr(), JobAttrType.VARIABLE, editableJobId);
        parseJobAttr(map, vo.getJobProp(), JobAttrType.ENV, editableJobId);
        parseJobAttr(map, vo.getEngineProp(), JobAttrType.PROPERTIES, editableJobId);
        wsDiJobAttrService.deleteByJobId(Collections.singletonList(editableJobId));
        for (Map.Entry<String, WsDiJobAttrDTO> entry : map.entrySet()) {
            wsDiJobAttrService.upsert(entry.getValue());
        }
        return editableJobId;
    }

    private void parseJobAttr(Map<String, WsDiJobAttrDTO> map, String str, JobAttrType jobAttrType, Long jobId) {
        if (StringUtils.hasText(str)) {
            String[] lines = str.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StringUtils.hasText(kv[0]) && StringUtils.hasText(kv[1])) {
                    WsDiJobAttrDTO dto = new WsDiJobAttrDTO();
                    dto.setJobId(jobId);
                    dto.setJobAttrType(jobAttrType);
                    dto.setJobAttrKey(kv[0]);
                    dto.setJobAttrValue(kv[1]);
                    map.put(jobId + jobAttrType.getValue() + kv[0], dto);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public void publish(Long id) throws ScalephException {
        WsDiJobDTO job = selectOne(id);
        switch (job.getJobStatus()) {
            case ARCHIVE:
                throw new ScalephException(I18nUtil.get("response.error.di.job.lowVersion"));
            case RELEASE:
                throw new ScalephException(I18nUtil.get("response.error.di.job.published"));
            case DRAFT:
            default:
        }

//        switch (job.getRuntimeState()) {
//            case STOP:
//                DiJob record = new DiJob();
//                record.setId(id);
//                record.setJobStatus(JobStatus.RELEASE);
//                diJobMapper.updateById(record);
//                archive(job.getProjectId(), job.getJobCode());
//                return;
//            case RUNNING:
//            case WAITING:
//                throw new ScalephException(I18nUtil.get("response.error.di.job.publish"));
//            default:
//        }
    }

    /**
     * 归档任务，只保留发布状态中最大版本号的那个，其余发布状态的任务均改为归档状态
     */
    private int archive(Long projectId, Long jobCode) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .eq(WsDiJob::getJobCode, jobCode)
                .eq(WsDiJob::getProjectId, projectId)
                .eq(WsDiJob::getJobStatus, JobStatus.RELEASE)
                .orderByAsc(WsDiJob::getJobVersion);
        List<WsDiJob> jobs = diJobMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(jobs)) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < jobs.size() - 1; i++) {
            WsDiJob job = new WsDiJob();
            job.setId(jobs.get(i).getId());
            job.setJobStatus(JobStatus.ARCHIVE);
            result += diJobMapper.update(job,
                    new LambdaUpdateWrapper<WsDiJob>().eq(WsDiJob::getId, job.getId())
            );
        }
        return result;
    }

    @Override
    public boolean hasValidJob(Collection<Long> projectIds) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .in(WsDiJob::getProjectId, projectIds)
                .last("limit 1");
        WsDiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public boolean hasValidJob(Long projectId, Long dirId) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .eq(WsDiJob::getProjectId, projectId)
                .last("limit 1");
        WsDiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public boolean hasRunningJob(Collection<Long> clusterIds) {
        //todo check if there is running job instances
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .last("limit 1");
        WsDiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public Long totalCnt(String jobType) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = new LambdaQueryWrapper<WsDiJob>()
                .eq(StringUtils.hasText(jobType), WsDiJob::getJobType, jobType);
        return diJobMapper.selectCount(queryWrapper);
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        int result = 0;
        wsDiJobGraphService.clone(sourceJobId, targetJobId);
        result += wsDiJobAttrService.clone(sourceJobId, targetJobId);
        return result;
    }
}
