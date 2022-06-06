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

import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnectorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SeatunnelConnectorServiceImpl implements SeatunnelConnectorService {

    private final SeatunnelNativeFlinkConnectorManager connectorManager = new SeatunnelNativeFlinkConnectorManager();

    @Override
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return connectorManager.getSupportedEnvProperties();
    }

    @Override
    public Set<PluginInfo> getAvailableConnectors(JobStepTypeEnum stepType) {
        return connectorManager.getAvailableConnectors(stepType);
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        return connectorManager.getSupportedProperties(pluginInfo);
    }
}
