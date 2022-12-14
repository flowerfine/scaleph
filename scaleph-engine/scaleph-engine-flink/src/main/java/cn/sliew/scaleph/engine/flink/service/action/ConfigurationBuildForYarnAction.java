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

package cn.sliew.scaleph.engine.flink.service.action;

import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterConfigService;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import org.apache.flink.configuration.*;

import java.io.IOException;
import java.nio.file.Path;

public class ConfigurationBuildForYarnAction extends ConfigurationBuildAction {

    public static final String NAME = ConfigurationBuildForYarnAction.class.getName();

    public ConfigurationBuildForYarnAction(WsFlinkClusterConfigService wsFlinkClusterConfigService) {
        super(NAME, wsFlinkClusterConfigService);
    }

    @Override
    protected Configuration buildForResource(ActionContext context, Path clusterCredentialPath, Configuration dynamicProperties) throws IOException {
        dynamicProperties.set(CoreOptions.FLINK_HADOOP_CONF_DIR, clusterCredentialPath.toAbsolutePath().toString());
        if (dynamicProperties.contains(JobManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        if (dynamicProperties.contains(TaskManagerOptions.TOTAL_PROCESS_MEMORY) == false) {
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
        }
        return dynamicProperties;
    }
}
