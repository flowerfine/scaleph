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

import cn.sliew.scaleph.dag.service.DagLinkService;
import cn.sliew.scaleph.dag.service.convert.DagLinkConvert;
import cn.sliew.scaleph.dag.service.dto.DagLinkDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagLink;
import cn.sliew.scaleph.dao.mapper.master.dag.DagLinkMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DagLinkServiceImpl implements DagLinkService {

    @Autowired
    private DagLinkMapper dagLinkMapper;

    @Override
    public List<DagLinkDTO> listLinks(Long dagId) {
        LambdaQueryWrapper<DagLink> queryWrapper = Wrappers.lambdaQuery(DagLink.class)
                .eq(DagLink::getDagId, dagId);
        List<DagLink> dagLinks = dagLinkMapper.selectList(queryWrapper);
        return DagLinkConvert.INSTANCE.toDto(dagLinks);
    }

    @Override
    public int insert(DagLinkDTO linkDTO) {
        DagLink record = DagLinkConvert.INSTANCE.toDo(linkDTO);
        return dagLinkMapper.insert(record);
    }

    @Override
    public int update(DagLinkDTO linkDTO) {
        LambdaUpdateWrapper<DagLink> updateWrapper = Wrappers.lambdaUpdate(DagLink.class)
                .eq(DagLink::getDagId, linkDTO.getDagId())
                .eq(DagLink::getLinkId, linkDTO.getLinkId());
        DagLink record = DagLinkConvert.INSTANCE.toDo(linkDTO);
        return dagLinkMapper.update(record, updateWrapper);
    }

    @Override
    public int upsert(DagLinkDTO linkDTO) {
        LambdaQueryWrapper<DagLink> queryWrapper = Wrappers.lambdaQuery(DagLink.class)
                .eq(DagLink::getDagId, linkDTO.getDagId())
                .eq(DagLink::getLinkId, linkDTO.getLinkId());
        if (dagLinkMapper.exists(queryWrapper)) {
            return update(linkDTO);
        } else {
            return insert(linkDTO);
        }
    }

    @Override
    public int deleteByDag(Long dagId) {
        LambdaUpdateWrapper<DagLink> updateWrapper = Wrappers.lambdaUpdate(DagLink.class)
                .eq(DagLink::getDagId, dagId);
        return dagLinkMapper.delete(updateWrapper);
    }

    @Override
    public int deleteByDag(List<Long> dagIds) {
        LambdaUpdateWrapper<DagLink> updateWrapper = Wrappers.lambdaUpdate(DagLink.class)
                .in(DagLink::getDagId, dagIds);
        return dagLinkMapper.delete(updateWrapper);
    }

    @Override
    public int deleteSurplusLinks(Long dagId, List<String> linkIds) {
        LambdaUpdateWrapper<DagLink> updateWrapper = Wrappers.lambdaUpdate(DagLink.class)
                .eq(DagLink::getDagId, dagId)
                .notIn(DagLink::getLinkId, linkIds);
        return dagLinkMapper.delete(updateWrapper);
    }

    @Override
    public int clone(Long sourceDagId, Long targetDagId) {
        List<DagLinkDTO> sourceLinks = listLinks(sourceDagId);
        sourceLinks.stream().forEach(linkDTO -> {
            linkDTO.setDagId(targetDagId);
            linkDTO.setId(null);
            linkDTO.setCreator(null);
            linkDTO.setCreateTime(null);
            linkDTO.setEditor(null);
            linkDTO.setUpdateTime(null);
            dagLinkMapper.insert(DagLinkConvert.INSTANCE.toDo(linkDTO));
        });
        return sourceLinks.size();
    }
}
