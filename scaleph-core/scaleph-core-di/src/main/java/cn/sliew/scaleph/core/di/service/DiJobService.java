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

package cn.sliew.scaleph.core.di.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobAddParam;
import cn.sliew.scaleph.core.di.service.param.DiJobParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集成-作业信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobService {

    Page<DiJobDTO> listByPage(DiJobParam param);

    List<DiJobDTO> listById(Collection<? extends Serializable> ids);

    DiJobDTO selectOne(Long id);

    DiJobDTO selectOne(Long projectId, String jobCode, int jobVersion);

    DiJobDTO insert(DiJobDTO dto);

    DiJobDTO insert(DiJobAddParam param);

    int update(DiJobDTO dto);

    int deleteByCode(Long projectId, String jobCode);

    int deleteByCode(List<DiJobDTO> list);

    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    /**
     * 归档任务，只保留发布状态中最大版本号的那个，其余发布状态的任务均改为归档状态
     */
    int archive(Long projectId, String jobCode);

    boolean hasValidJob(Collection<Long> projectIds);

    boolean hasValidJob(Long projectId, Long dirId);

    boolean hasRunningJob(Collection<Long> clusterIds);

    Long totalCnt(String jobType);

    int clone(Long sourceJobId, Long targetJobId);
}
