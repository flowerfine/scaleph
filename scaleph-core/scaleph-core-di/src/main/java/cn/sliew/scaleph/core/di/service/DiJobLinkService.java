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

import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;

/**
 * <p>
 * 数据集成-作业连线 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiJobLinkService {

    /**
     * 插入
     *
     * @param diJobLink
     * @return
     */
    int insert(DiJobLinkDTO diJobLink);

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
     * 查询作业连线信息
     *
     * @param jobId job id
     * @return job link list
     */
    List<DiJobLinkDTO> listJobLink(Long jobId);

    /**
     * 清理多余的连接
     *
     * @param jobId        jobid
     * @param linkCodeList link list
     * @return int
     */
    int deleteSurplusLink(Long jobId, List<String> linkCodeList);

    /**
     * 插入更新
     *
     * @param diJobLink link
     * @return int
     */
    int upsert(DiJobLinkDTO diJobLink);

    int clone(Long sourceJobId, Long targetJobId);
}
