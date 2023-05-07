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

package cn.sliew.scaleph.engine.flink.kubernetes.service.impl;

import cn.sliew.scaleph.engine.flink.kubernetes.factory.FlinkDeploymentFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.kubernetes.service.KuberenetesService;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.flink.kubernetes.operator.api.FlinkDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlinkKubernetesOperatorServiceImpl implements FlinkKubernetesOperatorService {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;
    @Autowired
    private KuberenetesService kuberenetesService;

    @Override
    public void deploySessionCluster(Long sessionClusterId) throws Exception {
        WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = wsFlinkKubernetesSessionClusterService.selectOne(sessionClusterId);
        KubernetesClient client = getClient(sessionClusterDTO);
        FlinkDeployment deployment = getFlinkDeployment(sessionClusterDTO);
        client.resource(deployment).createOrReplace();
    }

    @Override
    public void shutdownSessionCluster(Long sessionClusterId) throws Exception {
        WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = wsFlinkKubernetesSessionClusterService.selectOne(sessionClusterId);
        KubernetesClient client = getClient(sessionClusterDTO);
        FlinkDeployment deployment = getFlinkDeployment(sessionClusterDTO);
        client.resource(deployment).delete();
    }

    private KubernetesClient getClient(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) {
        return kuberenetesService.getClient(sessionClusterDTO.getClusterCredentialId());
    }

    private FlinkDeployment getFlinkDeployment(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) {
        FlinkSessionCluster sessionCluster = wsFlinkKubernetesSessionClusterService.asYAML(sessionClusterDTO);
        return FlinkDeploymentFactory.fromSessionCluster(sessionCluster);
    }
}
