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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.core.di.service.DiJobLinkService;
import cn.sliew.scaleph.core.di.service.convert.DiJobLinkConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobLinkDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJobLink;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobLinkMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gleiyu
 */
@Service
public class DiJobLinkServiceImpl implements DiJobLinkService {

    @Autowired
    private DiJobLinkMapper diJobLinkMapper;

    @Override
    public List<DiJobLinkDTO> listJobLink(Long jobId) {
        List<DiJobLink> list = diJobLinkMapper.selectList(
                new LambdaQueryWrapper<DiJobLink>()
                        .eq(DiJobLink::getJobId, jobId)
        );
        return DiJobLinkConvert.INSTANCE.toDto(list);
    }

    @Override
    public int insert(DiJobLinkDTO diJobLink) {
        DiJobLink link = DiJobLinkConvert.INSTANCE.toDo(diJobLink);
        return diJobLinkMapper.insert(link);
    }

    @Override
    public int upsert(DiJobLinkDTO diJobLink) {
        DiJobLink link = diJobLinkMapper.selectOne(
                new LambdaQueryWrapper<DiJobLink>()
                        .eq(DiJobLink::getJobId, diJobLink.getJobId())
                        .eq(DiJobLink::getLinkCode, diJobLink.getLinkCode())
        );
        DiJobLink jobLink = DiJobLinkConvert.INSTANCE.toDo(diJobLink);
        if (link == null) {
            return diJobLinkMapper.insert(jobLink);
        } else {
            return diJobLinkMapper.update(jobLink,
                    new LambdaUpdateWrapper<DiJobLink>()
                            .eq(DiJobLink::getJobId, jobLink.getJobId())
                            .eq(DiJobLink::getLinkCode, jobLink.getLinkCode())
            );
        }
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return diJobLinkMapper.deleteByProjectId(projectIds);
    }

    @Override
    public int deleteByJobId(Collection<? extends Serializable> jobIds) {
        return diJobLinkMapper.deleteByJobId(jobIds);
    }

    @Override
    public int deleteSurplusLink(Long jobId, List<String> linkCodeList) {
        return diJobLinkMapper.delete(
            new LambdaQueryWrapper<DiJobLink>()
                .eq(DiJobLink::getJobId, jobId)
                .notIn(CollectionUtil.isNotEmpty(linkCodeList), DiJobLink::getLinkCode,
                    linkCodeList)
        );
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        return diJobLinkMapper.clone(sourceJobId, targetJobId);
    }
}
