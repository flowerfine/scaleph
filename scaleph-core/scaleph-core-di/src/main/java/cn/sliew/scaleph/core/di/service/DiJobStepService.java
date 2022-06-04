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

import cn.sliew.scaleph.core.di.service.dto.DiJobStepDTO;

/**
 * <p>
 * 数据集成-作业步骤信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobStepService {

    /**
     * 更新
     *
     * @param diJobStepDTO job step info
     * @return int
     */
    int update(DiJobStepDTO diJobStepDTO);

    /**
     * 插入更新
     *
     * @param diJobStep job step info
     * @return int
     */
    int upsert(DiJobStepDTO diJobStep);

    /**
     * 按项目id删除
     *
     * @param projectIds project id
     * @return int
     */
    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    /**
     * 按job id 删除
     *
     * @param jobIds job id
     * @return int
     */
    int deleteByJobId(Collection<? extends Serializable> jobIds);

    /**
     * 删除多余的步骤信息
     *
     * @param jobId        job id
     * @param stepCodeList step code list
     * @return int
     */
    int deleteSurplusStep(Long jobId, List<String> stepCodeList);

    /**
     * 查询作业步骤信息
     *
     * @param jobId job id
     * @return job step list
     */
    List<DiJobStepDTO> listJobStep(Long jobId);

    /**
     * 查询步骤信息
     *
     * @param jobId    job id
     * @param stepCode stepCode
     * @return DiJobStepDTO
     */
    DiJobStepDTO selectOne(Long jobId, String stepCode);

    int clone(Long sourceJobId, Long targetJobId);

}
