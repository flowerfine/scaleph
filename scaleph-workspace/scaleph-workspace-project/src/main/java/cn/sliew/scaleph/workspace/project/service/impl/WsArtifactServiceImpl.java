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

package cn.sliew.scaleph.workspace.project.service.impl;

import cn.sliew.scaleph.dao.entity.master.ws.WsArtifact;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactMapper;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.convert.WsArtifactConvert;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import cn.sliew.scaleph.workspace.project.service.param.WsArtifactListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class WsArtifactServiceImpl implements WsArtifactService {

    @Autowired
    private WsArtifactMapper wsArtifactMapper;

    @Override
    public Page<WsArtifactDTO> list(WsArtifactListParam param) {
        Page<WsArtifact> page = wsArtifactMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsArtifact.class)
                        .eq(WsArtifact::getProjectId, param.getProjectId())
                        .like(StringUtils.hasText(param.getName()), WsArtifact::getName, param.getName()));
        Page<WsArtifactDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(WsArtifactConvert.INSTANCE.toDto(page.getRecords()));
        return result;
    }

    @Override
    public WsArtifactDTO selectOne(Long id) {
        WsArtifact record = wsArtifactMapper.selectById(id);
        return WsArtifactConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactDTO insert(WsArtifactDTO dto) {
        WsArtifact record = WsArtifactConvert.INSTANCE.toDo(dto);
        wsArtifactMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsArtifactDTO dto) {
        WsArtifact record = WsArtifactConvert.INSTANCE.toDo(dto);
        return wsArtifactMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return wsArtifactMapper.deleteById(id);
    }

}
