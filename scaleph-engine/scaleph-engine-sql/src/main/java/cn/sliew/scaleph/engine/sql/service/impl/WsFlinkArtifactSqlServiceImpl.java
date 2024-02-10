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

package cn.sliew.scaleph.engine.sql.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkArtifactSqlMapper;
import cn.sliew.scaleph.engine.sql.service.WsFlinkArtifactSqlService;
import cn.sliew.scaleph.engine.sql.service.convert.WsFlinkArtifactSqlConvert;
import cn.sliew.scaleph.engine.sql.service.dto.WsFlinkArtifactSqlDTO;
import cn.sliew.scaleph.engine.sql.service.param.*;
import cn.sliew.scaleph.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkArtifactSqlServiceImpl implements WsFlinkArtifactSqlService {

    @Autowired
    private WsFlinkArtifactService wsFlinkArtifactService;
    @Autowired
    private WsFlinkArtifactSqlMapper wsFlinkArtifactSqlMapper;

    @Override
    public Page<WsFlinkArtifactSqlDTO> list(WsFlinkArtifactSqlListParam param) {
        Page<WsFlinkArtifactSql> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsFlinkArtifactSql> sqlPage = wsFlinkArtifactSqlMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsFlinkArtifactSqlDTO> result =
                new Page<>(sqlPage.getCurrent(), sqlPage.getSize(), sqlPage.getTotal());
        result.setRecords(WsFlinkArtifactSqlConvert.INSTANCE.toDto(sqlPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsFlinkArtifactSqlDTO> listByArtifact(WsFlinkArtifactSqlHistoryParam param) {
        Page<WsFlinkArtifactSql> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkArtifactSql> queryWrapper = Wrappers.lambdaQuery(WsFlinkArtifactSql.class)
                .eq(WsFlinkArtifactSql::getFlinkArtifactId, param.getFlinkArtifactId())
                .orderByDesc(WsFlinkArtifactSql::getId);
        Page<WsFlinkArtifactSql> wsFlinkArtifactSqlPage = wsFlinkArtifactSqlMapper.selectPage(page, queryWrapper);
        Page<WsFlinkArtifactSqlDTO> result =
                new Page<>(wsFlinkArtifactSqlPage.getCurrent(), wsFlinkArtifactSqlPage.getSize(), wsFlinkArtifactSqlPage.getTotal());
        result.setRecords(WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSqlPage.getRecords()));
        return result;
    }

    @Override
    public List<WsFlinkArtifactSqlDTO> listAll(WsFlinkArtifactSqlSelectListParam param) {
        List<WsFlinkArtifactSql> wsFlinkArtifactSqls = wsFlinkArtifactSqlMapper.listAll(param.getProjectId(), param.getName());
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSqls);
    }

    @Override
    public List<WsFlinkArtifactSqlDTO> listAllByArtifact(Long artifactId) {
        List<WsFlinkArtifactSql> list = wsFlinkArtifactSqlMapper.selectList(
                Wrappers.lambdaQuery(WsFlinkArtifactSql.class)
                        .eq(WsFlinkArtifactSql::getFlinkArtifactId, artifactId)
                        .orderByDesc(WsFlinkArtifactSql::getId)
        );
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsFlinkArtifactSqlDTO selectOne(Long id) {
        WsFlinkArtifactSql record = wsFlinkArtifactSqlMapper.selectOne(id);
        checkState(record != null, () -> "flink artifact sql not exists for id: " + id);
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsFlinkArtifactSqlDTO selectCurrent(Long artifactId) {
        WsFlinkArtifactSql wsFlinkArtifactSql = wsFlinkArtifactSqlMapper.selectCurrent(artifactId);
        return WsFlinkArtifactSqlConvert.INSTANCE.toDto(wsFlinkArtifactSql);
    }

    @Override
    public void insert(WsFlinkArtifactSqlInsertParam param) {
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setProjectId(param.getProjectId());
        flinkArtifact.setType(FlinkJobType.JAR);
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        flinkArtifact = wsFlinkArtifactService.insert(flinkArtifact);
        WsFlinkArtifactSql record = new WsFlinkArtifactSql();
        record.setFlinkArtifactId(flinkArtifact.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setCurrent(YesOrNo.YES);
        wsFlinkArtifactSqlMapper.insert(record);
    }

    @Override
    public int update(WsFlinkArtifactSqlUpdateParam param) {
        WsFlinkArtifactSqlDTO wsFlinkArtifactSqlDTO = selectOne(param.getId());
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setId(wsFlinkArtifactSqlDTO.getWsFlinkArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsFlinkArtifactService.update(flinkArtifact);

        WsFlinkArtifactSql record = new WsFlinkArtifactSql();
        record.setId(param.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setCurrent(YesOrNo.YES);
        return wsFlinkArtifactSqlMapper.updateById(record);
    }

    @Override
    public int updateScript(WsFlinkArtifactSqlScriptUpdateParam param) {
        WsFlinkArtifactSql record = new WsFlinkArtifactSql();
        record.setId(param.getId());
        record.setScript(param.getScript());
        return wsFlinkArtifactSqlMapper.updateById(record);
    }

    @Override
    public int deleteOne(Long id) throws ScalephException {
        WsFlinkArtifactSqlDTO wsFlinkArtifactJarDTO = selectOne(id);
        if (wsFlinkArtifactJarDTO.getCurrent() == YesOrNo.YES) {
            throw new ScalephException("Unsupport delete current sql script");
        }
        return wsFlinkArtifactSqlMapper.deleteById(id);
    }

    @Override
    public int deleteAll(Long flinkArtifactId) throws ScalephException {
        List<WsFlinkArtifactSqlDTO> wsFlinkArtifactSqlDTOS = listAllByArtifact(flinkArtifactId);
        for (WsFlinkArtifactSqlDTO sql : wsFlinkArtifactSqlDTOS) {
            wsFlinkArtifactSqlMapper.deleteById(sql.getId());
        }
        return wsFlinkArtifactService.deleteById(flinkArtifactId);
    }
}
