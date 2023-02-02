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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelJobService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.constant.GraphConstants;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagNodeDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.DagPanelDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.vo.DagPanalVO;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SeatunnelJobServiceImpl implements SeatunnelJobService {

    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Override
    public String preview(Long jobId) throws Exception {
        WsDiJobDTO job = wsDiJobService.queryJobGraph(jobId);
        return seatunnelConfigService.buildConfig(job);
    }

    @Override
    public List<DagPanelDTO> loadDndPanelInfo() throws PluginException {
        List<DagPanelDTO> list = new ArrayList<>();
        for (SeaTunnelPluginType type : SeaTunnelPluginType.values()) {
            Set<SeaTunnelConnectorPlugin> plugins = seatunnelConnectorService.getAvailableConnectors(type);
            DagPanelDTO panel = toDagPanel(type, plugins);
            if (panel != null) {
                list.add(panel);
            }
        }
        return list;
    }

    private DagPanelDTO toDagPanel(SeaTunnelPluginType pluginType, Set<SeaTunnelConnectorPlugin> plugins) {
        if (CollectionUtils.isEmpty(plugins)) {
            return null;
        }
        DagPanelDTO panel = new DagPanelDTO();
        panel.setId(pluginType.getLabel());
        panel.setHeader(pluginType.getLabel());
        List<DagNodeDTO> nodeList = new ArrayList<>();
        plugins.stream().sorted(Comparator.comparing(plugin -> plugin.getPluginName().ordinal()))
                .forEachOrdered(plugin -> {
                    PluginInfo pluginInfo = plugin.getPluginInfo();
                    DagNodeDTO node = new DagNodeDTO();
                    node.setId(pluginInfo.getName());
                    node.setDescription(pluginInfo.getDescription());
                    node.setLabel(plugin.getPluginName().getLabel());
                    node.setRenderKey(GraphConstants.DND_RENDER_ID);
                    node.setHealth(plugin.getPluginHealth());
                    node.setFeatures(plugin.getPluginFeatures());
                    node.setData(buildPluginInfo(plugin));
                    nodeList.add(node);
                });
        panel.setChildren(nodeList);
        return panel;
    }

    private DagPanalVO buildPluginInfo(SeaTunnelConnectorPlugin connector) {
        final DagPanalVO dagPanalVO = new DagPanalVO();
        dagPanalVO.setDisplayName(connector.getPluginName().getLabel() + " " + connector.getPluginType().getLabel());
        dagPanalVO.setName(connector.getPluginName().getValue());
        dagPanalVO.setType(connector.getPluginType().getValue());
        dagPanalVO.setEngine(connector.getEngineType().getValue());
        return dagPanalVO;
    }
}
