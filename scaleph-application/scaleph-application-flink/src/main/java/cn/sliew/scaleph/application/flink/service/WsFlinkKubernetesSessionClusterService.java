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

package cn.sliew.scaleph.application.flink.service;

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesSessionClusterListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesSessionClusterSelectListParam;
import cn.sliew.scaleph.application.flink.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

import java.util.List;
import java.util.Optional;

public interface WsFlinkKubernetesSessionClusterService {

    Page<WsFlinkKubernetesSessionClusterDTO> list(WsFlinkKubernetesSessionClusterListParam param);

    List<Long> listAll();

    List<WsFlinkKubernetesSessionClusterDTO> listAll(WsFlinkKubernetesSessionClusterSelectListParam param);

    WsFlinkKubernetesSessionClusterDTO selectOne(Long id);

    WsFlinkKubernetesSessionClusterDTO fromTemplate(Long templateId);

    FlinkSessionCluster asYaml(WsFlinkKubernetesSessionClusterDTO dto);

    int insert(WsFlinkKubernetesSessionClusterDTO dto);

    int update(WsFlinkKubernetesSessionClusterDTO dto);

    Optional<WsFlinkKubernetesSessionClusterDTO> getSqlGatewaySessionCluster(Long projectId);

    List<WsFlinkKubernetesSessionClusterDTO> getAllSqlGatewaySessionClusters();

    int enableSqlGateway(Long id);

    int disableSqlGateway(Long id);

    int updateStatus(Long id, FlinkDeploymentStatus status);

    int clearStatus(Long id);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    void deploy(Long id) throws Exception;

    void shutdown(Long id) throws Exception;

    Optional<GenericKubernetesResource> getStatus(Long id);

    Optional<GenericKubernetesResource> getStatusWithoutManagedFields(Long id);
}
