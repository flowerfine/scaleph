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

package cn.sliew.scaleph.core.di.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.core.di.service.DiJobAttrService;
import cn.sliew.scaleph.core.di.service.DiJobGraphService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.convert.DiJobConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.param.*;
import cn.sliew.scaleph.core.di.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.core.di.service.vo.JobGraphVO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobMapper;
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
public class DiJobServiceImpl implements DiJobService {

    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private DiJobMapper diJobMapper;
    @Autowired
    private DiJobGraphService diJobGraphService;
    @Autowired
    private DiJobAttrService diJobAttrService;

    @Override
    public Page<DiJobDTO> listByPage(DiJobParam param) {
        Page<DiJob> jobs = diJobMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.toDo()
        );
        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(jobs.getRecords());
        Page<DiJobDTO> result = new Page<>(jobs.getCurrent(), jobs.getSize(), jobs.getTotal());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<DiJobDTO> listById(Collection<Long> ids) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>().in(DiJob::getId, ids);
        List<DiJob> jobs = diJobMapper.selectList(queryWrapper);
        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(jobs);
        return dtoList;
    }

    @Override
    public DiJobDTO selectOne(Long id) {
        DiJob record = diJobMapper.selectById(id);
        checkState(record != null, () -> "job not exists for id: " + id);
        DiJobDTO dto = DiJobConvert.INSTANCE.toDto(record);
        return dto;
    }

    @Override
    public DiJobDTO selectOne(Long projectId, Long jobCode, int jobVersion) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getProjectId, projectId)
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getJobVersion, jobVersion);
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return DiJobConvert.INSTANCE.toDto(job);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public DiJobDTO insert(DiJobAddParam param) throws UidGenerateException {
        DiJob record = BeanUtil.copy(param, new DiJob());
        record.setJobCode(defaultUidGenerator.getUID());
        record.setJobStatus(JobStatus.DRAFT);
        record.setJobVersion(1);
        diJobMapper.insert(record);
        return selectOne(record.getId());
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int update(DiJobUpdateParam param) {
        DiJob record = BeanUtil.copy(param, new DiJob());
        return diJobMapper.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int delete(Long id) {
        DiJobDTO job = selectOne(id);
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
        List<DiJob> jobList = diJobMapper.selectList(new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
        );
        List<Long> ids = jobList.stream().map(DiJob::getId).collect(Collectors.toList());
        diJobGraphService.deleteBatch(ids);
        diJobAttrService.deleteByJobId(ids);
        return diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
        );
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int deleteByProjectId(Collection<Long> projectIds) {
        diJobGraphService.deleteByProjectId(projectIds);
        diJobAttrService.deleteByProjectId(projectIds);
        return diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds));
    }

    /**
     * 编辑前检查作业的版本，确认是否需要生成新的可编辑版本
     */
    private Long prepareJobVersion(Long id) throws ScalephException {
        DiJobDTO job = selectOne(id);
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

    private Long riseJobVersion(DiJobDTO job) {
        int jobVersion = job.getJobVersion() + 1;
        DiJobDTO newVersionJob = selectOne(job.getProjectId(), job.getJobCode(), jobVersion);
        checkState(newVersionJob == null, () -> I18nUtil.get("response.error.di.job.lowVersion"));

        DiJob record = DiJobConvert.INSTANCE.toDo(job);
        record.setId(null);
        record.setJobVersion(jobVersion);
        record.setJobStatus(JobStatus.DRAFT);
        diJobMapper.insert(record);
        clone(job.getId(), record.getId());
        return record.getId();
    }

    @Override
    public DiJobDTO queryJobGraph(Long id) {
        DiJobDTO job = selectOne(id);
        diJobGraphService.queryJobGraph(job);
        job.setJobAttrList(diJobAttrService.listJobAttr(id));
        return job;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobStep(DiJobStepParam param) throws ScalephException {
        Long editableJobId = prepareJobVersion(param.getJobId());

        JobGraphVO jobGraphVO = JacksonUtil.parseJsonString(param.getJobGraph(), JobGraphVO.class);
        diJobGraphService.saveJobGraph(editableJobId, jobGraphVO);

        DiJobStepParam copiedParam = BeanUtil.copy(param, new DiJobStepParam());
        copiedParam.setJobId(editableJobId);
        diJobGraphService.updateJobStep(copiedParam);

        return editableJobId;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobGraph(DiJobGraphParam param) throws ScalephException {
        Long editableJobId = prepareJobVersion(param.getJobId());
        diJobGraphService.saveJobGraph(editableJobId, param.getJobGraph());
        return editableJobId;
    }

    @Override
    public DiJobAttrVO listJobAttrs(Long id) {
        DiJobAttrVO vo = new DiJobAttrVO();
        vo.setJobId(id);
        List<DiJobAttrDTO> list = diJobAttrService.listJobAttr(id);
        for (DiJobAttrDTO jobAttr : list) {
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
        Map<String, DiJobAttrDTO> map = new HashMap<>();
        parseJobAttr(map, vo.getJobAttr(), JobAttrType.VARIABLE, editableJobId);
        parseJobAttr(map, vo.getJobProp(), JobAttrType.ENV, editableJobId);
        parseJobAttr(map, vo.getEngineProp(), JobAttrType.PROPERTIES, editableJobId);
        diJobAttrService.deleteByJobId(Collections.singletonList(editableJobId));
        for (Map.Entry<String, DiJobAttrDTO> entry : map.entrySet()) {
            diJobAttrService.upsert(entry.getValue());
        }
        return editableJobId;
    }

    private void parseJobAttr(Map<String, DiJobAttrDTO> map, String str, JobAttrType jobAttrType, Long jobId) {
        if (StringUtils.hasText(str)) {
            String[] lines = str.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StringUtils.hasText(kv[0]) && StringUtils.hasText(kv[1])) {
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

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public void publish(Long id) throws ScalephException {
        DiJobDTO job = selectOne(id);
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
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
                .eq(DiJob::getJobStatus, JobStatus.RELEASE)
                .orderByAsc(DiJob::getJobVersion);
        List<DiJob> jobs = diJobMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(jobs)) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < jobs.size() - 1; i++) {
            DiJob job = new DiJob();
            job.setId(jobs.get(i).getId());
            job.setJobStatus(JobStatus.ARCHIVE);
            result += diJobMapper.update(job,
                    new LambdaUpdateWrapper<DiJob>().eq(DiJob::getId, job.getId())
            );
        }
        return result;
    }

    @Override
    public boolean hasValidJob(Collection<Long> projectIds) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds)
                .last("limit 1");
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public boolean hasValidJob(Long projectId, Long dirId) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getProjectId, projectId)
                .last("limit 1");
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public boolean hasRunningJob(Collection<Long> clusterIds) {
        //todo check if there is running job instances
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .last("limit 1");
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public Long totalCnt(String jobType) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(StringUtils.hasText(jobType), DiJob::getJobType, jobType);
        return diJobMapper.selectCount(queryWrapper);
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        int result = 0;
        diJobGraphService.clone(sourceJobId, targetJobId);
        result += diJobAttrService.clone(sourceJobId, targetJobId);
        return result;
    }
}
