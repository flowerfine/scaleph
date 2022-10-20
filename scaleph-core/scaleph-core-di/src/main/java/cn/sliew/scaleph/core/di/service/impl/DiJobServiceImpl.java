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
import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.common.enums.JobStatusEnum;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.convert.DiJobConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.param.*;
import cn.sliew.scaleph.core.di.service.vo.JobGraphVO;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobMapper;
import cn.sliew.scaleph.security.util.SecurityUtil;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private DiJobMapper diJobMapper;
    @Autowired
    private DiJobAttrService diJobAttrService;
    @Autowired
    private DiJobLinkService diJobLinkService;
    @Autowired
    private DiJobStepService diJobStepService;
    @Autowired
    private DiJobGraphService diJobGraphService;
    @Autowired
    private DiJobResourceFileService diJobResourceFileService;
    @Autowired
    private DiDirectoryService diDirectoryService;

    @Override
    public Page<DiJobDTO> listByPage(DiJobParam param) {
        Page<DiJob> jobs = diJobMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.toDo()
        );

        List<Long> directoryIds = jobs.getRecords().stream().map(DiJob::getDirectoryId).collect(Collectors.toList());
        Map<Long, DiDirectoryDTO> directoryMap = diDirectoryService.loadFullPath(directoryIds);

        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(jobs.getRecords());
        for (DiJobDTO job : dtoList) {
            DiDirectoryDTO dir = directoryMap.get(job.getDirectory().getId());
            job.setDirectory(dir);
        }
        Page<DiJobDTO> result = new Page<>(jobs.getCurrent(), jobs.getSize(), jobs.getTotal());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<DiJobDTO> listById(Collection<Long> ids) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>().in(DiJob::getId, ids);
        List<DiJob> jobs = diJobMapper.selectList(queryWrapper);

        List<Long> directoryIds = jobs.stream().map(DiJob::getDirectoryId).collect(Collectors.toList());
        Map<Long, DiDirectoryDTO> directoryMap = diDirectoryService.loadFullPath(directoryIds);
        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(jobs);
        for (DiJobDTO job : dtoList) {
            DiDirectoryDTO dir = directoryMap.get(job.getDirectory().getId());
            job.setDirectory(dir);
        }
        return dtoList;
    }

    @Override
    public DiJobDTO selectOne(Long id) {
        DiJob record = diJobMapper.selectById(id);
        checkState(record != null, () -> "job not exists for id: " + id);
        DiJobDTO dto = DiJobConvert.INSTANCE.toDto(record);
        Map<Long, DiDirectoryDTO> map =
                diDirectoryService.loadFullPath(Collections.singletonList(record.getDirectoryId()));
        DiDirectoryDTO dir = map.get(record.getDirectoryId());
        dto.setDirectory(dir);
        return dto;
    }

    @Override
    public DiJobDTO selectOne(Long projectId, String jobCode, int jobVersion) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getProjectId, projectId)
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getJobVersion, jobVersion);
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return DiJobConvert.INSTANCE.toDto(job);
    }

    @Override
    public DiJobDTO insert(DiJobAddParam param) {
        DiJob record = BeanUtil.copy(param, new DiJob());
        record.setJobCode(RandomStringUtils.random(16, true, true));
        record.setJobStatus(JobStatus.DRAFT);
        record.setRuntimeState(RuntimeState.STOP);
        record.setJobOwner(SecurityUtil.getCurrentUserName());
        record.setJobVersion(1);
        diJobMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(DiJobUpdateParam param) {
        DiJob record = BeanUtil.copy(param, new DiJob());
        return diJobMapper.updateById(record);
    }

    @Override
    public int delete(Long id) {
        DiJobDTO job = selectOne(id);
        checkState(job.getRuntimeState() == RuntimeState.STOP,
                () -> I18nUtil.get("response.error.di.job.running"));
        return deleteByCode(job.getProjectId(), job.getJobCode());
    }

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

    @Override
    public int deleteByCode(Long projectId, String jobCode) {
        List<DiJob> jobList = diJobMapper.selectList(new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
        );
        List<Long> ids = jobList.stream().map(DiJob::getId).collect(Collectors.toList());
        diJobAttrService.deleteByJobId(ids);
        diJobLinkService.deleteByJobId(ids);
        diJobStepService.deleteByJobId(ids);
        return diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
        );
    }

    @Override
    public int deleteByProjectId(Collection<Long> projectIds) {
        diJobAttrService.deleteByProjectId(projectIds);
        diJobLinkService.deleteByProjectId(projectIds);
        diJobStepService.deleteByProjectId(projectIds);
        return diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds));
    }

    @Override
    public Long prepareJobVersion(Long id) throws ScalephException {
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
        job.setJobVersion(jobVersion);
        job.setJobStatus(JobStatus.DRAFT);
        diJobMapper.insert(record);
        clone(job.getId(), record.getId());
        return record.getId();
    }

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

    @Override
    public Long saveJobGraph(DiJobGraphParam param) throws ScalephException {
        Long editableJobId = prepareJobVersion(param.getJobId());
        diJobGraphService.saveJobGraph(editableJobId, param.getJobGraph());
        return editableJobId;
    }

    @Override
    public int archive(Long projectId, String jobCode) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
                .eq(DiJob::getJobStatus, JobStatusEnum.RELEASE.getValue())
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
                    new LambdaUpdateWrapper<DiJob>()
                            .eq(DiJob::getId, job.getId())
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
                .eq(DiJob::getDirectoryId, dirId)
                .last("limit 1");
        DiJob job = diJobMapper.selectOne(queryWrapper);
        return Optional.ofNullable(job).isPresent();
    }

    @Override
    public boolean hasRunningJob(Collection<Long> clusterIds) {
        LambdaQueryWrapper<DiJob> queryWrapper = new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getClusterId, clusterIds)
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
        result += diJobStepService.clone(sourceJobId, targetJobId);
        result += diJobAttrService.clone(sourceJobId, targetJobId);
        result += diJobLinkService.clone(sourceJobId, targetJobId);
        result += diJobResourceFileService.clone(sourceJobId, targetJobId);
        return result;
    }
}
