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

import cn.sliew.milky.common.constant.Attribute;
import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkClusterConfigDTO;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;
import cn.sliew.scaleph.workflow.engine.action.DefaultActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import org.apache.flink.configuration.Configuration;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public abstract class ConfigurationBuildAction extends AbstractWorkFlow {

    private AttributeKey<Long> FLINK_CLUSTER_CONFIG_ID = AttributeKey.newInstance("flinkClusterConfigId");
    private AttributeKey<Configuration> CONFIGURATION = AttributeKey.newInstance("configuration");

    private WsFlinkClusterConfigService wsFlinkClusterConfigService;

    public ConfigurationBuildAction(String name, WsFlinkClusterConfigService wsFlinkClusterConfigService) {
        super(name);
        this.wsFlinkClusterConfigService = wsFlinkClusterConfigService;
    }

    @Override
    public List<AttributeKey> getInputs() {
        return Arrays.asList(FLINK_CLUSTER_CONFIG_ID);
    }

    @Override
    public List<AttributeKey> getOutputs() {
        return Arrays.asList(CONFIGURATION);
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> {
            try {
                build(context);
                listener.onResponse(new DefaultActionResult(ActionStatus.SUCCESS, context));
            } catch (IOException e) {
                listener.onFailure(e);
            }
        };
    }

    private void build(ActionContext context) throws IOException {
        Attribute<Long> flinkClusterConfigId = context.attr(FLINK_CLUSTER_CONFIG_ID);
        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(flinkClusterConfigId.get());
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(wsFlinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(wsFlinkClusterConfigDTO.getConfigOptions());
        }

        Attribute<Path> clusterCredentialPath = context.attr(ClusterCredentialLoadAction.CLUSTER_CREDENTIAL_PATH);

        Configuration configuration = buildForResource(context, clusterCredentialPath.get(), dynamicProperties);
        
        Attribute<Configuration> configurationAttr = context.attr(CONFIGURATION);
        configurationAttr.setIfAbsent(configuration);
    }

    protected abstract Configuration buildForResource(ActionContext context, Path clusterCredentialPath, Configuration dynamicProperties) throws IOException;
}
