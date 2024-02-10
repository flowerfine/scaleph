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

package cn.sliew.scaleph.engine.flink.cdc.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCVersion;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactCDC;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkArtifactCDCMapper;
import cn.sliew.scaleph.engine.flink.cdc.service.FlinkCDCDagService;
import cn.sliew.scaleph.engine.flink.cdc.service.FlinkCDCJobService;
import cn.sliew.scaleph.engine.flink.cdc.service.convert.WsFlinkArtifactCDCConvert;
import cn.sliew.scaleph.engine.flink.cdc.service.dto.WsFlinkArtifactCDCDTO;
import cn.sliew.scaleph.engine.flink.cdc.service.param.WsFlinkArtifactCDCAddParam;
import cn.sliew.scaleph.engine.flink.cdc.service.param.WsFlinkArtifactCDCListParam;
import cn.sliew.scaleph.engine.flink.cdc.service.param.WsFlinkArtifactCDCSelectListParam;
import cn.sliew.scaleph.engine.flink.cdc.service.param.WsFlinkArtifactCDCUpdateParam;
import cn.sliew.scaleph.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class FlinkCDCJobServiceImpl implements FlinkCDCJobService {

    @Autowired
    private WsFlinkArtifactCDCMapper wsFlinkArtifactCDCMapper;
    @Autowired
    private WsFlinkArtifactService wsFlinkArtifactService;
    @Autowired
    private FlinkCDCDagService flinkCDCDagService;

    @Override
    public Page<WsFlinkArtifactCDCDTO> listByPage(WsFlinkArtifactCDCListParam param) {
        Page<WsFlinkArtifactCDC> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsFlinkArtifactCDC> cdcPage = wsFlinkArtifactCDCMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsFlinkArtifactCDCDTO> result =
                new Page<>(cdcPage.getCurrent(), cdcPage.getSize(), cdcPage.getTotal());
        result.setRecords(WsFlinkArtifactCDCConvert.INSTANCE.toDto(cdcPage.getRecords()));
        return result;
    }

    @Override
    public List<WsFlinkArtifactCDCDTO> listAll(WsFlinkArtifactCDCSelectListParam param) {
        List<WsFlinkArtifactCDC> cdcs = wsFlinkArtifactCDCMapper.listAll(param.getProjectId(), param.getName());
        return WsFlinkArtifactCDCConvert.INSTANCE.toDto(cdcs);
    }

    @Override
    public List<WsFlinkArtifactCDCDTO> listAllByArtifact(Long artifactId) {
        List<WsFlinkArtifactCDC> list = wsFlinkArtifactCDCMapper.selectList(
                Wrappers.lambdaQuery(WsFlinkArtifactCDC.class)
                        .eq(WsFlinkArtifactCDC::getFlinkArtifactId, artifactId)
                        .orderByDesc(WsFlinkArtifactCDC::getId)
        );
        return WsFlinkArtifactCDCConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsFlinkArtifactCDCDTO selectOne(Long id) {
        WsFlinkArtifactCDC record = wsFlinkArtifactCDCMapper.selectOne(id);
        checkState(record != null, () -> "flink artifact cdc not exists for id: " + id);
        return WsFlinkArtifactCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsFlinkArtifactCDCDTO selectCurrent(Long artifactId) {
        WsFlinkArtifactCDC record = wsFlinkArtifactCDCMapper.selectCurrent(artifactId);
        return WsFlinkArtifactCDCConvert.INSTANCE.toDto(record);
    }

    @Override
    public String preview(Long id) throws Exception {
        return null;
    }

    @Override
    public WsFlinkArtifactCDCDTO insert(WsFlinkArtifactCDCAddParam param) {
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setProjectId(param.getProjectId());
        flinkArtifact.setType(FlinkJobType.FLINK_CDC);
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        flinkArtifact = wsFlinkArtifactService.insert(flinkArtifact);
        WsFlinkArtifactCDC record = new WsFlinkArtifactCDC();
        record.setFlinkArtifactId(flinkArtifact.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setFlinkCDCVersion(FlinkCDCVersion.current());
        record.setDagId(flinkCDCDagService.initialize());
        record.setCurrent(YesOrNo.YES);
        wsFlinkArtifactCDCMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsFlinkArtifactCDCUpdateParam param) {
        WsFlinkArtifactCDCDTO wsFlinkArtifactCDCDTO = selectOne(param.getId());
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setId(wsFlinkArtifactCDCDTO.getWsFlinkArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsFlinkArtifactService.update(flinkArtifact);

        WsFlinkArtifactCDC record = new WsFlinkArtifactCDC();
        record.setId(param.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setCurrent(YesOrNo.YES);
        return wsFlinkArtifactCDCMapper.updateById(record);
    }

    @Override
    public int delete(Long id) throws ScalephException {
        WsFlinkArtifactCDCDTO wsFlinkArtifactCDCDTO = selectOne(id);
        if (wsFlinkArtifactCDCDTO.getCurrent() == YesOrNo.YES) {
            throw new ScalephException("Unsupport delete current flink cdc job");
        }
        flinkCDCDagService.destroy(wsFlinkArtifactCDCDTO.getDagId());
        return wsFlinkArtifactCDCMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) throws ScalephException {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public int deleteAll(Long flinkArtifactId) throws ScalephException {
        List<WsFlinkArtifactCDCDTO> dtos = listAllByArtifact(flinkArtifactId);
        for (WsFlinkArtifactCDCDTO cdc : dtos) {
            wsFlinkArtifactCDCMapper.deleteById(cdc.getId());
        }
        return wsFlinkArtifactService.deleteById(flinkArtifactId);
    }
}
