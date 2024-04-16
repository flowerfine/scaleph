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

package cn.sliew.scaleph.dag.service.impl;

import cn.sliew.scaleph.dag.service.DagConfigLinkService;
import cn.sliew.scaleph.dag.service.convert.DagConfigLinkConvert;
import cn.sliew.scaleph.dag.service.dto.DagConfigLinkDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagConfigLink;
import cn.sliew.scaleph.dao.mapper.master.dag.DagConfigLinkMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DagConfigLinkServiceImpl implements DagConfigLinkService {

    @Autowired
    private DagConfigLinkMapper dagConfigLinkMapper;

    @Override
    public List<DagConfigLinkDTO> listLinks(Long dagId) {
        LambdaQueryWrapper<DagConfigLink> queryWrapper = Wrappers.lambdaQuery(DagConfigLink.class)
                .eq(DagConfigLink::getDagId, dagId);
        List<DagConfigLink> dagConfigLinks = dagConfigLinkMapper.selectList(queryWrapper);
        return DagConfigLinkConvert.INSTANCE.toDto(dagConfigLinks);
    }

    @Override
    public int insert(DagConfigLinkDTO linkDTO) {
        DagConfigLink record = DagConfigLinkConvert.INSTANCE.toDo(linkDTO);
        return dagConfigLinkMapper.insert(record);
    }

    @Override
    public int update(DagConfigLinkDTO linkDTO) {
        LambdaUpdateWrapper<DagConfigLink> updateWrapper = Wrappers.lambdaUpdate(DagConfigLink.class)
                .eq(DagConfigLink::getDagId, linkDTO.getDagId())
                .eq(DagConfigLink::getLinkId, linkDTO.getLinkId());
        DagConfigLink record = DagConfigLinkConvert.INSTANCE.toDo(linkDTO);
        return dagConfigLinkMapper.update(record, updateWrapper);
    }

    @Override
    public int upsert(DagConfigLinkDTO linkDTO) {
        LambdaQueryWrapper<DagConfigLink> queryWrapper = Wrappers.lambdaQuery(DagConfigLink.class)
                .eq(DagConfigLink::getDagId, linkDTO.getDagId())
                .eq(DagConfigLink::getLinkId, linkDTO.getLinkId());
        if (dagConfigLinkMapper.exists(queryWrapper)) {
            return update(linkDTO);
        } else {
            return insert(linkDTO);
        }
    }

    @Override
    public int deleteByDag(Long dagId) {
        LambdaUpdateWrapper<DagConfigLink> updateWrapper = Wrappers.lambdaUpdate(DagConfigLink.class)
                .eq(DagConfigLink::getDagId, dagId);
        return dagConfigLinkMapper.delete(updateWrapper);
    }

    @Override
    public int deleteByDag(List<Long> dagIds) {
        LambdaUpdateWrapper<DagConfigLink> updateWrapper = Wrappers.lambdaUpdate(DagConfigLink.class)
                .in(DagConfigLink::getDagId, dagIds);
        return dagConfigLinkMapper.delete(updateWrapper);
    }

    @Override
    public int deleteSurplusLinks(Long dagId, List<String> linkIds) {
        LambdaUpdateWrapper<DagConfigLink> updateWrapper = Wrappers.lambdaUpdate(DagConfigLink.class)
                .eq(DagConfigLink::getDagId, dagId)
                .notIn(CollectionUtils.isEmpty(linkIds) == false, DagConfigLink::getLinkId, linkIds);
        return dagConfigLinkMapper.delete(updateWrapper);
    }

    @Override
    public int clone(Long sourceDagId, Long targetDagId) {
        List<DagConfigLinkDTO> sourceLinks = listLinks(sourceDagId);
        sourceLinks.stream().forEach(linkDTO -> {
            linkDTO.setDagId(targetDagId);
            linkDTO.setId(null);
            linkDTO.setCreator(null);
            linkDTO.setCreateTime(null);
            linkDTO.setEditor(null);
            linkDTO.setUpdateTime(null);
            insert(linkDTO);
        });
        return sourceLinks.size();
    }
}
