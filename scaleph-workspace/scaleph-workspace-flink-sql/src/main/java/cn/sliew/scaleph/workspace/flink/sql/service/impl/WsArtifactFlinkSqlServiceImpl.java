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

package cn.sliew.scaleph.workspace.flink.sql.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkSql;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactFlinkSqlMapper;
import cn.sliew.scaleph.workspace.flink.sql.service.WsArtifactFlinkSqlService;
import cn.sliew.scaleph.workspace.flink.sql.service.convert.WsFlinkArtifactSqlConvert;
import cn.sliew.scaleph.workspace.flink.sql.service.dto.WsArtifactFlinkSqlDTO;
import cn.sliew.scaleph.workspace.flink.sql.service.param.*;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsArtifactFlinkSqlServiceImpl implements WsArtifactFlinkSqlService {

    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private WsArtifactFlinkSqlMapper wsArtifactFlinkSqlMapper;

    @Override
    public Page<WsArtifactFlinkSqlDTO> list(WsArtifactFlinkSqlListParam param) {
        Page<WsArtifactFlinkSql> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactFlinkSql> sqlPage = wsArtifactFlinkSqlMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactFlinkSqlDTO> result =
                new Page<>(sqlPage.getCurrent(), sqlPage.getSize(), sqlPage.getTotal());
        result.setRecords(WsFlinkArtifactSqlConvert.INSTANCE.toDto(sqlPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactFlinkSqlDTO> listByArtifact(WsArtifactFlinkSqlArtifactParam param) {
        Page<WsArtifactFlinkSql> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactFlinkSql> queryWrapper = Wrappers.lambdaQuery(WsArtifactFlinkSql.class)
                .eq(WsArtifactFlinkSql::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactFlinkSql::getId);
        Page<WsArtifactFlinkSql> wsFlinkArtifactSqlPage = wsArtifactFlinkSqlMapper.selectPage(page, queryWrapper);
        Page<WsArtifactFlinkSqlDTO> result =
                new Page<>(wsFlinkArtifactSqlPage.getCurrent(), wsFlinkArtifactSqlPage.getSize(), wsFlinkArtifactSqlPage.getTotal());
        result.setRecords(WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSqlPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactFlinkSqlDTO> listAll(WsArtifactFlinkSqlSelectListParam param) {
        List<WsArtifactFlinkSql> wsFlinkArtifactSqls = wsArtifactFlinkSqlMapper.listAll(param.getProjectId(), param.getName());
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSqls);
    }

    @Override
    public List<WsArtifactFlinkSqlDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactFlinkSql> list = wsArtifactFlinkSqlMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactFlinkSql.class)
                        .eq(WsArtifactFlinkSql::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactFlinkSql::getId)
        );
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsArtifactFlinkSqlDTO selectOne(Long id) {
        WsArtifactFlinkSql record = wsArtifactFlinkSqlMapper.selectOne(id);
        checkState(record != null, () -> "artifact flink-sql not exists for id: " + id);
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactFlinkSqlDTO selectCurrent(Long artifactId) {
        WsArtifactFlinkSql wsFlinkArtifactSql = wsArtifactFlinkSqlMapper.selectCurrent(artifactId);
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSql);
    }

    @Override
    public void insert(WsArtifactFlinkSqlInsertParam param) {
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.SQL);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactFlinkSql record = new WsArtifactFlinkSql();
        record.setArtifactId(artifactDTO.getId());
        record.setFlinkVersion(FlinkVersion.current());
        record.setCurrent(YesOrNo.YES);
        wsArtifactFlinkSqlMapper.insert(record);
    }

    @Override
    public int update(WsArtifactFlinkSqlUpdateParam param) {
        WsArtifactFlinkSqlDTO wsArtifactFlinkSqlDTO = selectOne(param.getId());
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setId(wsArtifactFlinkSqlDTO.getArtifact().getId());
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        wsArtifactService.update(artifactDTO);

        WsArtifactFlinkSql record = new WsArtifactFlinkSql();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return wsArtifactFlinkSqlMapper.updateById(record);
    }

    @Override
    public int updateScript(WsArtifactFlinkSqlScriptUpdateParam param) {
        WsArtifactFlinkSql record = new WsArtifactFlinkSql();
        record.setId(param.getId());
        record.setScript(param.getScript());
        return wsArtifactFlinkSqlMapper.updateById(record);
    }

    @Override
    public int deleteOne(Long id) {
        WsArtifactFlinkSqlDTO wsArtifactFlinkSqlDTO = selectOne(id);
        checkState(wsArtifactFlinkSqlDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current sql");
        return wsArtifactFlinkSqlMapper.deleteById(id);
    }

    @Override
    public int deleteArtifact(Long artifactId) {
        List<WsArtifactFlinkSqlDTO> wsArtifactFlinkSqlDTOS = listAllByArtifact(artifactId);
        for (WsArtifactFlinkSqlDTO sql : wsArtifactFlinkSqlDTOS) {
            wsArtifactFlinkSqlMapper.deleteById(sql.getId());
        }
        return wsArtifactService.deleteById(artifactId);
    }
}
