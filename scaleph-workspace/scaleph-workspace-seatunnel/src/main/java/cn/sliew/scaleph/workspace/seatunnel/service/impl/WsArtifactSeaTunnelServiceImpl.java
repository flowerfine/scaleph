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

package cn.sliew.scaleph.workspace.seatunnel.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactSeaTunnel;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactSeaTunnelMapper;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelDagService;
import cn.sliew.scaleph.workspace.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.workspace.seatunnel.service.WsArtifactSeaTunnelService;
import cn.sliew.scaleph.workspace.seatunnel.service.convert.WsArtifactSeaTunnelConvert;
import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsArtifactSeaTunnelDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.param.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsArtifactSeaTunnelServiceImpl implements WsArtifactSeaTunnelService {

    @Autowired
    private WsArtifactSeaTunnelMapper wsArtifactSeaTunnelMapper;
    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private SeaTunnelDagService seaTunnelDagService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;

    @Override
    public Page<WsArtifactSeaTunnelDTO> list(WsArtifactSeaTunnelListParam param) {
        Page<WsArtifactSeaTunnel> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactSeaTunnel> seaTunnelPage = wsArtifactSeaTunnelMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactSeaTunnelDTO> result =
                new Page<>(seaTunnelPage.getCurrent(), seaTunnelPage.getSize(), seaTunnelPage.getTotal());
        result.setRecords(WsArtifactSeaTunnelConvert.INSTANCE.toDto(seaTunnelPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactSeaTunnelDTO> listByArtifact(WsArtifactSeaTunnelArtifactParam param) {
        Page<WsArtifactSeaTunnel> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactSeaTunnel> queryWrapper = Wrappers.lambdaQuery(WsArtifactSeaTunnel.class)
                .eq(WsArtifactSeaTunnel::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactSeaTunnel::getId);
        Page<WsArtifactSeaTunnel> wsArtifactSeaTunnelPage = wsArtifactSeaTunnelMapper.selectPage(page, queryWrapper);
        Page<WsArtifactSeaTunnelDTO> result =
                new Page<>(wsArtifactSeaTunnelPage.getCurrent(), wsArtifactSeaTunnelPage.getSize(), wsArtifactSeaTunnelPage.getTotal());
        result.setRecords(WsArtifactSeaTunnelConvert.INSTANCE.toDto(wsArtifactSeaTunnelPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactSeaTunnelDTO> listAll(WsArtifactSeaTunnelSelectListParam param) {
        List<WsArtifactSeaTunnel> seaTunnels = wsArtifactSeaTunnelMapper.listAll(param.getProjectId(), param.getName());
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(seaTunnels);
    }

    @Override
    public List<WsArtifactSeaTunnelDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactSeaTunnel> list = wsArtifactSeaTunnelMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactSeaTunnel.class)
                        .eq(WsArtifactSeaTunnel::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactSeaTunnel::getId)
        );
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsArtifactSeaTunnelDTO selectOne(Long id) {
        WsArtifactSeaTunnel record = wsArtifactSeaTunnelMapper.selectOne(id);
        checkState(record != null, () -> "artifact seatunnel not exists for id: " + id);
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactSeaTunnelDTO selectCurrent(Long artifactId) {
        WsArtifactSeaTunnel record = wsArtifactSeaTunnelMapper.selectCurrent(artifactId);
        return WsArtifactSeaTunnelConvert.INSTANCE.toDto(record);
    }

    @Override
    public String buildConfig(Long id) throws Exception {
        return null;
    }

    @Override
    public WsArtifactSeaTunnelDTO insert(WsArtifactSeaTunnelAddParam param) {
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.SEATUNNEL);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactSeaTunnel record = new WsArtifactSeaTunnel();
        record.setArtifactId(artifactDTO.getId());
        record.setSeaTunnelEngine(SeaTunnelEngineType.SEATUNNEL);
        record.setFlinkVersion(FlinkVersion.V_1_15_4);
        record.setSeaTunnelVersion(SeaTunnelVersion.V_2_3_3);
        record.setDagId(seaTunnelDagService.initialize());
        record.setCurrent(YesOrNo.YES);
        wsArtifactSeaTunnelMapper.insert(record);
        return selectOne(record.getId());
    }

    @Override
    public int update(WsArtifactSeaTunnelUpdateParam param) {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(param.getId());
        WsArtifactDTO flinkArtifact = new WsArtifactDTO();
        flinkArtifact.setId(wsArtifactSeaTunnelDTO.getArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsArtifactService.update(flinkArtifact);

        WsArtifactSeaTunnel record = new WsArtifactSeaTunnel();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return wsArtifactSeaTunnelMapper.updateById(record);
    }

    @Override
    public void updateGraph(WsArtifactSeaTunnelGraphParam param) {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(param.getId());
        seaTunnelDagService.update(wsArtifactSeaTunnelDTO.getDagId(), param.getJobGraph());
    }

    @Override
    public int delete(Long id) throws ScalephException {
        WsArtifactSeaTunnelDTO wsArtifactSeaTunnelDTO = selectOne(id);
        checkState(wsArtifactSeaTunnelDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current seatunnel");
        return doDelete(wsArtifactSeaTunnelDTO);
    }

    @Override
    public int deleteBatch(List<Long> ids) throws ScalephException {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public int deleteArtifact(Long artifactId) throws ScalephException {
        List<WsArtifactSeaTunnelDTO> dtos = listAllByArtifact(artifactId);
        for (WsArtifactSeaTunnelDTO seaTunnelDTO : dtos) {
            doDelete(seaTunnelDTO);
        }
        return wsArtifactService.deleteById(artifactId);
    }

    private int doDelete(WsArtifactSeaTunnelDTO cdc) {
        seaTunnelDagService.destroy(cdc.getDagId());
        return wsArtifactSeaTunnelMapper.deleteById(cdc.getId());
    }
}
