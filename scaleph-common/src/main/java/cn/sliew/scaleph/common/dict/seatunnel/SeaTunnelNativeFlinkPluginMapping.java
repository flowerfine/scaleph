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

package cn.sliew.scaleph.common.dict.seatunnel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SeaTunnelNativeFlinkPluginMapping {
    ;

    private SeaTunnelEngineType engineType;
    private SeaTunnelPluginType pluginType;
    private SeaTunnelNativeFlinkPluginName pluginName;
    private String pluginJarPrefix;
    private SeaTunnelConnectorHealth health;
    private SeaTunnelConnectorFeature[] features;

    SeaTunnelNativeFlinkPluginMapping(SeaTunnelEngineType engineType,
                                      SeaTunnelPluginType pluginType,
                                      SeaTunnelNativeFlinkPluginName pluginName,
                                      String pluginJarPrefix,
                                      SeaTunnelConnectorHealth health,
                                      SeaTunnelConnectorFeature... features) {
        this.engineType = engineType;
        this.pluginType = pluginType;
        this.pluginName = pluginName;
        this.pluginJarPrefix = pluginJarPrefix;
        this.health = health;
        this.features = features;
    }

    public static SeaTunnelNativeFlinkPluginMapping of(SeaTunnelNativeFlinkPluginName pluginName) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.getPluginName() == pluginName)
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(SeaTunnelNativeFlinkPluginMapping.class, pluginName.getValue()));
    }
}
