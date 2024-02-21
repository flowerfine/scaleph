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

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.dag.xflow.dnd.DndPortDTO;
import cn.sliew.scaleph.dag.xflow.dnd.DndPortGroupEnum;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.workspace.seatunnel.dag.dnd.SeaTunnelDagDndDTO;
import cn.sliew.scaleph.workspace.seatunnel.dag.dnd.SeaTunnelDagDndMeta;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelDagService;
import cn.sliew.scaleph.workspace.seatunnel.service.SeatunnelConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType.SEATUNNEL;

@Service
public class SeaTunnelDagServiceImpl implements SeaTunnelDagService {

    @Autowired
    private DagService dagService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Override
    public Long initialize() {
        return dagService.insert(new DagSimpleAddParam());
    }

    @Override
    public void destroy(Long dagId) {
        dagService.delete(dagId);
    }

    @Override
    public Object getDag(Long dagId) {
        return null;
    }

    @Override
    public void update(Long dagId, DagGraphVO graph) {
        dagService.replace(dagId, graph);
    }

    @Override
    public List<DndDTO> getDnds() {
        return getDnds(SEATUNNEL);
    }

    @Override
    public List<DndDTO> getDnds(SeaTunnelEngineType engineType) {
        switch (engineType) {
            case SEATUNNEL:
                return loadSeaTunnelPanel();
            default:
                return Collections.emptyList();
        }
    }

    private List<DndDTO> loadSeaTunnelPanel() {
        List<DndDTO> dnds = new ArrayList();
        for (SeaTunnelPluginType pluginType : SeaTunnelPluginType.values()) {
            dnds.add(buildSeaTunnelPluginType(pluginType));
        }
        return dnds;
    }

    private SeaTunnelDagDndDTO buildSeaTunnelPluginType(SeaTunnelPluginType pluginType) {
        SeaTunnelDagDndDTO category = new SeaTunnelDagDndDTO();
        category.setKey(pluginType.getValue());
        category.setTitle(pluginType.getLabel());
        category.setDocString(pluginType.getRemark());
        category.setIsLeaf(false);
        List<DndDTO> children = new ArrayList();
        Set<SeaTunnelConnectorPlugin> plugins = seatunnelConnectorService.getAvailableConnectors(pluginType);
        plugins.stream().sorted(Comparator.comparing(plugin -> plugin.getPluginName().ordinal())).forEachOrdered(plugin -> {
            SeaTunnelDagDndDTO child = buildSeaTunnelConnector(category, plugin);
            SeaTunnelDagDndMeta meta = buildPluginInfo(plugin);
            child.setMeta(meta);
            List<DndPortDTO> ports;
            switch (pluginType) {
                case SOURCE:
                    ports = getSourcePorts(child.getKey());
                    break;
                case SINK:
                    ports = getSinkPorts(child.getKey());
                    break;
                case TRANSFORM:
                    ports = getTransformPorts(child.getKey());
                    break;
                default:
                    ports = Collections.emptyList();
            }
            child.setPorts(ports);
            children.add(child);
        });
        category.setChildren(children);
        return category;
    }

    private SeaTunnelDagDndDTO buildSeaTunnelConnector(SeaTunnelDagDndDTO category, SeaTunnelConnectorPlugin plugin) {
        PluginInfo pluginInfo = plugin.getPluginInfo();
        SeaTunnelDagDndDTO node = new SeaTunnelDagDndDTO();
        node.setCategory(category.getKey());
        node.setKey(pluginInfo.getName());
        node.setTitle(plugin.getPluginName().getLabel());
        node.setDocString(pluginInfo.getDescription());
        node.setIsLeaf(true);
        node.setHealth(plugin.getPluginHealth());
        node.setFeatures(plugin.getPluginFeatures());
        return node;
    }

    private SeaTunnelDagDndMeta buildPluginInfo(SeaTunnelConnectorPlugin connector) {
        SeaTunnelDagDndMeta meta = new SeaTunnelDagDndMeta();
        meta.setName(connector.getPluginName().getValue());
        meta.setType(connector.getPluginType().getValue());
        meta.setEngine(connector.getEngineType().getValue());
        return meta;
    }


    private List<DndPortDTO> getSourcePorts(String key) {
        List<DndPortDTO> ports = new ArrayList<>();
        DndPortDTO portDTO = new DndPortDTO();
        portDTO.setId(key + "-" + DndPortGroupEnum.bottom.name());
        portDTO.setGroup(DndPortGroupEnum.bottom.name());
        ports.add(portDTO);
        return ports;
    }

    private List<DndPortDTO> getSinkPorts(String key) {
        List<DndPortDTO> ports = new ArrayList<>();
        DndPortDTO portDTO = new DndPortDTO();
        portDTO.setId(key + "-" + DndPortGroupEnum.top.name());
        portDTO.setGroup(DndPortGroupEnum.top.name());
        ports.add(portDTO);
        return ports;
    }

    private List<DndPortDTO> getTransformPorts(String key) {
        List<DndPortDTO> ports = new ArrayList<>();
        DndPortDTO sourcePort = new DndPortDTO();
        sourcePort.setId(key + "-" + DndPortGroupEnum.top.name());
        sourcePort.setGroup(DndPortGroupEnum.top.name());
        ports.add(sourcePort);

        DndPortDTO sinkPort = new DndPortDTO();
        sinkPort.setId(key + "-" + DndPortGroupEnum.bottom.name());
        sinkPort.setGroup(DndPortGroupEnum.bottom.name());
        ports.add(sinkPort);
        return ports;
    }
}
