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

import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType;
import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.dag.xflow.dnd.DndPortDTO;
import cn.sliew.scaleph.dag.xflow.dnd.DndPortGroupEnum;
import cn.sliew.scaleph.engine.flink.cdc.dag.dnd.FlinkCDCDagDndDTO;
import cn.sliew.scaleph.engine.flink.cdc.dag.dnd.FlinkCDCDagDndMeta;
import cn.sliew.scaleph.engine.flink.cdc.service.FlinkCDCConnectorService;
import cn.sliew.scaleph.engine.flink.cdc.service.FlinkCDCDagService;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FlinkCDCDagServiceImpl implements FlinkCDCDagService {

    @Autowired
    private DagService dagService;
    @Autowired
    private FlinkCDCConnectorService flinkCDCConnectorService;

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
    public void update(Object param) {

    }

    @Override
    public List<DndDTO> getDnds() {
        List<DndDTO> dnds = new ArrayList();
        for (FlinkCDCPluginType pluginType : FlinkCDCPluginType.values()) {
            dnds.add(buildFlinkCDCPluginType(pluginType));
        }
        return dnds;
    }

    private FlinkCDCDagDndDTO buildFlinkCDCPluginType(FlinkCDCPluginType pluginType) {
        FlinkCDCDagDndDTO category = new FlinkCDCDagDndDTO();
        category.setKey(pluginType.getValue());
        category.setTitle(pluginType.getLabel());
        category.setDocString(pluginType.getRemark());
        category.setIsLeaf(false);
        List<DndDTO> children = new ArrayList();
        Set<FlinkCDCPipilineConnectorPlugin> plugins = flinkCDCConnectorService.getAvailableConnectors(pluginType);
        plugins.stream().sorted(Comparator.comparing(plugin -> plugin.getPluginName().ordinal())).forEachOrdered(plugin -> {
            FlinkCDCDagDndDTO child = buildFlinkCDCConnector(category, plugin);
            FlinkCDCDagDndMeta meta = buildPluginInfo(plugin);
            child.setMeta(meta);
            List<DndPortDTO> ports;
            switch (pluginType) {
                case SOURCE:
                    ports = getSourcePorts(child.getKey());
                    break;
                case SINK:
                    ports = getSinkPorts(child.getKey());
                    break;
                case ROUTE:
                    ports = getRoutePorts(child.getKey());
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

    private FlinkCDCDagDndDTO buildFlinkCDCConnector(FlinkCDCDagDndDTO category, FlinkCDCPipilineConnectorPlugin plugin) {
        PluginInfo pluginInfo = plugin.getPluginInfo();
        FlinkCDCDagDndDTO node = new FlinkCDCDagDndDTO();
        node.setCategory(category.getKey());
        node.setKey(pluginInfo.getName());
        node.setTitle(plugin.getPluginName().getLabel());
        node.setDocString(pluginInfo.getDescription());
        node.setIsLeaf(true);
        return node;
    }

    private FlinkCDCDagDndMeta buildPluginInfo(FlinkCDCPipilineConnectorPlugin connector) {
        FlinkCDCDagDndMeta meta = new FlinkCDCDagDndMeta();
        meta.setName(connector.getPluginName().getValue());
        meta.setType(connector.getPluginType().getValue());
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

    private List<DndPortDTO> getRoutePorts(String key) {
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
