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
import cn.sliew.scaleph.common.dict.job.JobStatus;
import cn.sliew.scaleph.common.dict.job.RuntimeState;
import cn.sliew.scaleph.common.enums.JobStatusEnum;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.convert.DiJobConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobAddParam;
import cn.sliew.scaleph.core.di.service.param.DiJobParam;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<DiJobDTO> listById(Collection<? extends Serializable> ids) {
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
        DiJob job = diJobMapper.selectById(id);
        DiJobDTO dto = DiJobConvert.INSTANCE.toDto(job);
        Map<Long, DiDirectoryDTO> map =
                diDirectoryService.loadFullPath(Collections.singletonList(job.getDirectoryId()));
        DiDirectoryDTO dir = map.get(job.getDirectoryId());
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
    public DiJobDTO insert(DiJobDTO dto) {
        DiJob job = DiJobConvert.INSTANCE.toDo(dto);
        diJobMapper.insert(job);
        return selectOne(job.getId());
    }

    @Override
    public DiJobDTO insert(DiJobAddParam param) {
        DiJob record = BeanUtil.copy(param, new DiJob());
        diJobMapper.insert(record);
        record.setJobStatus(JobStatus.DRAFT);
        record.setRuntimeState(RuntimeState.STOP);
//        record.setJobOwner(SecurityUtil.getCurrentUserName());
        record.setJobVersion(1);
        return selectOne(record.getId());
    }

    @Override
    public int update(DiJobDTO dto) {
        DiJob job = DiJobConvert.INSTANCE.toDo(dto);
        return diJobMapper.updateById(job);
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
    public int deleteByCode(List<DiJobDTO> list) {
        int result = 0;
        for (DiJobDTO dto : list) {
            result += deleteByCode(dto.getProjectId(), dto.getJobCode());
        }
        return result;
    }


    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        diJobAttrService.deleteByProjectId(projectIds);
        diJobLinkService.deleteByProjectId(projectIds);
        diJobStepService.deleteByProjectId(projectIds);
        return diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds));
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
