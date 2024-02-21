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

package cn.sliew.scaleph.workspace.flink.cdc.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCVersion;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkCDC;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactFlinkCDCMapper;
import cn.sliew.scaleph.workspace.flink.cdc.service.FlinkCDCDagService;
import cn.sliew.scaleph.workspace.flink.cdc.service.WsArtifactFlinkCDCService;
import cn.sliew.scaleph.workspace.flink.cdc.service.convert.WsArtifactFlinkCDCConvert;
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsArtifactFlinkCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.*;
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
public class WsArtifactFlinkCDCServiceImpl implements WsArtifactFlinkCDCService {

    @Autowired
    private WsArtifactFlinkCDCMapper wsArtifactFlinkCDCMapper;
    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private FlinkCDCDagService flinkCDCDagService;

    @Override
    public Page<WsArtifactFlinkCDCDTO> list(WsArtifactFlinkCDCListParam param) {
        Page<WsArtifactFlinkCDC> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactFlinkCDC> cdcPage = wsArtifactFlinkCDCMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactFlinkCDCDTO> result =
                new Page<>(cdcPage.getCurrent(), cdcPage.getSize(), cdcPage.getTotal());
        result.setRecords(WsArtifactFlinkCDCConvert.INSTANCE.toDto(cdcPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactFlinkCDCDTO> listByArtifact(WsArtifactFlinkCDCArtifactParam param) {
        Page<WsArtifactFlinkCDC> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactFlinkCDC> queryWrapper = Wrappers.lambdaQuery(WsArtifactFlinkCDC.class)
                .eq(WsArtifactFlinkCDC::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactFlinkCDC::getId);
        Page<WsArtifactFlinkCDC> wsArtifactFlinkCDCPage = wsArtifactFlinkCDCMapper.selectPage(page, queryWrapper);
        Page<WsArtifactFlinkCDCDTO> result =
                new Page<>(wsArtifactFlinkCDCPage.getCurrent(), wsArtifactFlinkCDCPage.getSize(), wsArtifactFlinkCDCPage.getTotal());
        result.setRecords(WsArtifactFlinkCDCConvert.INSTANCE.toDto(wsArtifactFlinkCDCPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactFlinkCDCDTO> listAll(WsArtifactFlinkCDCSelectListParam param) {
        List<WsArtifactFlinkCDC> cdcs = wsArtifactFlinkCDCMapper.listAll(param.getProjectId(), param.getName());
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(cdcs);
    }

    @Override
    public List<WsArtifactFlinkCDCDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactFlinkCDC> list = wsArtifactFlinkCDCMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactFlinkCDC.class)
                        .eq(WsArtifactFlinkCDC::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactFlinkCDC::getId)
        );
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsArtifactFlinkCDCDTO selectOne(Long id) {
        WsArtifactFlinkCDC record = wsArtifactFlinkCDCMapper.selectOne(id);
        checkState(record != null, () -> "artifact flink-cdc not exists for id: " + id);
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactFlinkCDCDTO selectCurrent(Long artifactId) {
        WsArtifactFlinkCDC record = wsArtifactFlinkCDCMapper.selectCurrent(artifactId);
        return WsArtifactFlinkCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactFlinkCDCDTO insert(WsArtifactFlinkCDCAddParam param) {
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.FLINK_CDC);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactFlinkCDC record = new WsArtifactFlinkCDC();
        record.setArtifactId(artifactDTO.getId());
        record.setFlinkVersion(FlinkVersion.current());
        record.setFlinkCDCVersion(FlinkCDCVersion.current());
        record.setDagId(flinkCDCDagService.initialize());
        record.setCurrent(YesOrNo.YES);
        wsArtifactFlinkCDCMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsArtifactFlinkCDCUpdateParam param) {
        WsArtifactFlinkCDCDTO wsArtifactFlinkCDCDTO = selectOne(param.getId());
        WsArtifactDTO flinkArtifact = new WsArtifactDTO();
        flinkArtifact.setId(wsArtifactFlinkCDCDTO.getArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsArtifactService.update(flinkArtifact);

        WsArtifactFlinkCDC record = new WsArtifactFlinkCDC();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return wsArtifactFlinkCDCMapper.updateById(record);
    }

    @Override
    public int delete(Long id) throws ScalephException {
        WsArtifactFlinkCDCDTO wsArtifactFlinkCDCDTO = selectOne(id);
        checkState(wsArtifactFlinkCDCDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current flink cdc");
        return doDelete(wsArtifactFlinkCDCDTO);
    }

    @Override
    public int deleteBatch(List<Long> ids) throws ScalephException {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public int deleteArtifact(Long artifactId) {
        List<WsArtifactFlinkCDCDTO> dtos = listAllByArtifact(artifactId);
        for (WsArtifactFlinkCDCDTO cdc : dtos) {
            doDelete(cdc);
        }
        return wsArtifactService.deleteById(artifactId);
    }

    private int doDelete(WsArtifactFlinkCDCDTO cdc) {
        flinkCDCDagService.destroy(cdc.getDagId());
        return wsArtifactFlinkCDCMapper.deleteById(cdc.getId());
    }
}
