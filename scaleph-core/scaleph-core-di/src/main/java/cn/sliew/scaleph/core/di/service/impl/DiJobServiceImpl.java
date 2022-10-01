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
import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.enums.JobStatusEnum;
import cn.sliew.scaleph.core.di.service.*;
import cn.sliew.scaleph.core.di.service.convert.DiJobConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobParam;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public DiJobDTO insert(DiJobDTO dto) {
        DiJob job = DiJobConvert.INSTANCE.toDo(dto);
        this.diJobMapper.insert(job);
        dto.setId(job.getId());
        return dto;
    }

    @Override
    public int archive(Long projectId, String jobCode) {
        int result = 0;
        List<DiJob> list = this.diJobMapper.selectList(
                new LambdaQueryWrapper<DiJob>()
                        .eq(DiJob::getJobCode, jobCode)
                        .eq(DiJob::getProjectId, projectId)
                        .eq(DiJob::getJobStatus, JobStatusEnum.RELEASE.getValue())
                        .orderByAsc(DiJob::getJobVersion)
        );
        if (CollectionUtil.isEmpty(list)) {
            return 0;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            DiJob job = new DiJob();
            job.setId(list.get(i).getId());
            job.setJobStatus(JobStatusEnum.ARCHIVE.getValue());
            result += this.diJobMapper.update(job,
                    new LambdaUpdateWrapper<DiJob>()
                            .eq(DiJob::getId, job.getId())
            );
        }
        return result;
    }

    @Override
    public int update(DiJobDTO dto) {
        DiJob job = DiJobConvert.INSTANCE.toDo(dto);
        return this.diJobMapper.updateById(job);
    }

    @Override
    public int deleteByCode(Long projectId, String jobCode) {
        List<DiJob> jobList = this.diJobMapper.selectList(new LambdaQueryWrapper<DiJob>()
                .eq(DiJob::getJobCode, jobCode)
                .eq(DiJob::getProjectId, projectId)
        );
        List<Long> ids = jobList.stream().map(DiJob::getId).collect(Collectors.toList());
        this.diJobAttrService.deleteByJobId(ids);
        this.diJobLinkService.deleteByJobId(ids);
        this.diJobStepService.deleteByJobId(ids);
        return this.diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
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
        this.diJobAttrService.deleteByProjectId(projectIds);
        this.diJobLinkService.deleteByProjectId(projectIds);
        this.diJobStepService.deleteByProjectId(projectIds);
        return this.diJobMapper.delete(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds));
    }


    @Override
    public Page<DiJobDTO> listByPage(DiJobParam param) {
        Page<DiJobDTO> result = new Page<>();
        Page<DiJob> list = this.diJobMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.toDo()
        );
        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(list.getRecords());
        Map<Long, DiDirectoryDTO> map = this.diDirectoryService.loadFullPath(
                list.getRecords().stream().map(DiJob::getDirectoryId).collect(Collectors.toList()));
        for (DiJobDTO job : dtoList) {
            DiDirectoryDTO dir = map.get(job.getDirectory().getId());
            job.setDirectory(dir);
        }
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DiJobDTO> listById(Collection<? extends Serializable> ids) {
        List<DiJob> list = this.diJobMapper.selectList(
                new LambdaQueryWrapper<DiJob>().in(DiJob::getId, ids)
        );
        List<DiJobDTO> dtoList = DiJobConvert.INSTANCE.toDto(list);
        Map<Long, DiDirectoryDTO> map = this.diDirectoryService.loadFullPath(
                list.stream().map(DiJob::getDirectoryId).collect(Collectors.toList()));
        for (DiJobDTO job : dtoList) {
            DiDirectoryDTO dir = map.get(job.getDirectory().getId());
            job.setDirectory(dir);
        }
        return dtoList;
    }

    @Override
    public DiJobDTO selectOne(Long id) {
        DiJob job = this.diJobMapper.selectById(id);
        DiJobDTO dto = DiJobConvert.INSTANCE.toDto(job);
        Map<Long, DiDirectoryDTO> map =
                this.diDirectoryService.loadFullPath(Collections.singletonList(job.getDirectoryId()));
        DiDirectoryDTO dir = map.get(job.getDirectoryId());
        dto.setDirectory(dir);
        return dto;
    }

    @Override
    public DiJobDTO selectOne(Long projectId, String jobCode, int jobVersion) {
        DiJob job = this.diJobMapper.selectOne(
                new LambdaQueryWrapper<DiJob>()
                        .eq(DiJob::getProjectId, projectId)
                        .eq(DiJob::getJobCode, jobCode)
                        .eq(DiJob::getJobVersion, jobVersion)
        );
        return DiJobConvert.INSTANCE.toDto(job);
    }


    @Override
    public boolean hasValidJob(Collection<Long> projectIds) {
        DiJob job = this.diJobMapper.selectOne(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getProjectId, projectIds)
                .last("limit 1")
        );
        if (job == null || job.getId() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasValidJob(Long projectId, Long dirId) {
        DiJob job = this.diJobMapper.selectOne(
                new LambdaQueryWrapper<DiJob>()
                        .eq(DiJob::getProjectId, projectId)
                        .eq(DiJob::getDirectoryId, dirId)
                        .last("limit 1")

        );
        if (job == null || job.getId() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean hasRunningJob(Collection<Long> clusterIds) {
        DiJob job = this.diJobMapper.selectOne(new LambdaQueryWrapper<DiJob>()
                .in(DiJob::getClusterId, clusterIds)
                .last("limit 1")
        );
        if (job == null || job.getId() == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Long totalCnt(String jobType) {
        return this.diJobMapper.selectCount(new LambdaQueryWrapper<DiJob>()
                .eq(StrUtil.isNotEmpty(jobType), DiJob::getJobType, jobType)
        );
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        int result = 0;
        result += this.diJobStepService.clone(sourceJobId, targetJobId);
        result += this.diJobAttrService.clone(sourceJobId, targetJobId);
        result += this.diJobLinkService.clone(sourceJobId, targetJobId);
        result += this.diJobResourceFileService.clone(sourceJobId, targetJobId);
        return result;
    }
}
