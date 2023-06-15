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

package cn.sliew.scaleph.engine.flink.kubernetes.service;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.deployment.FlinkDeployment;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesSessionClusterListParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesSessionClusterSelectListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

import java.util.List;

public interface WsFlinkKubernetesSessionClusterService {

    Page<WsFlinkKubernetesSessionClusterDTO> list(WsFlinkKubernetesSessionClusterListParam param);

    List<Long> listAll();

    List<WsFlinkKubernetesSessionClusterDTO> listAll(WsFlinkKubernetesSessionClusterSelectListParam param);

    WsFlinkKubernetesSessionClusterDTO selectOne(Long id);

    WsFlinkKubernetesSessionClusterDTO fromTemplate(Long templateId);

    FlinkSessionCluster asYAML(WsFlinkKubernetesSessionClusterDTO dto);

    int insert(WsFlinkKubernetesSessionClusterDTO dto);

    int update(WsFlinkKubernetesSessionClusterDTO dto);

    int updateStatus(Long id, FlinkDeploymentStatus status);

    int clearStatus(Long id);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    void deploy(Long id) throws Exception;

    void shutdown(Long id) throws Exception;

    GenericKubernetesResource getStatus(Long id);
    GenericKubernetesResource getStatusWithoutManagedFields(Long id);
}
