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

package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.common.enums.JobStepTypeEnum;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SeatunnelNativeFlinkPluginManagerTest {

    @Test
    void testLoadConnectorPlugins() throws Exception {
        SeatunnelNativeFlinkPluginManager manager = new SeatunnelNativeFlinkPluginManager();
        final Set<PluginInfo> sourceConnectors = manager.getAvailableConnectors(JobStepTypeEnum.SOURCE);
        assertThat(sourceConnectors).isNotEmpty().hasSize(7);

        final Set<PluginInfo> sinkConnectors = manager.getAvailableConnectors(JobStepTypeEnum.SINK);
        assertThat(sinkConnectors).isNotEmpty().hasSize(10);

        final Set<PluginInfo> transforms = manager.getAvailableConnectors(JobStepTypeEnum.TRANSFORM);
        assertThat(transforms).isEmpty();
    }
}
