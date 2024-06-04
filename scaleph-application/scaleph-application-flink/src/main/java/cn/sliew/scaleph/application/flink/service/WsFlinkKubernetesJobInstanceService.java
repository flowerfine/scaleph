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

import cn.sliew.scaleph.application.flink.operator.status.FlinkSessionJobStatus;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceSavepointDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceDeployParam;
import cn.sliew.scaleph.application.flink.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceSavepointListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceShutdownParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

import java.util.Optional;

public interface WsFlinkKubernetesJobInstanceService {

    Page<WsFlinkKubernetesJobInstanceDTO> list(WsFlinkKubernetesJobInstanceListParam param);

    WsFlinkKubernetesJobInstanceDTO selectOne(Long id);

    Optional<WsFlinkKubernetesJobInstanceDTO> selectCurrent(Long wsFlinkKubernetesJobId);

    Page<WsFlinkKubernetesJobInstanceSavepointDTO> selectSavepoint(WsFlinkKubernetesJobInstanceSavepointListParam param);

    String mockYaml(Long wsFlinkKubernetesJobId);

    String asYaml(Long id) throws Exception;

    void deploy(WsFlinkKubernetesJobInstanceDeployParam param) throws Exception;

    void shutdown(WsFlinkKubernetesJobInstanceShutdownParam param) throws Exception;

    void restart(Long id) throws Exception;

    void triggerSavepoint(Long id) throws Exception;

    void suspend(Long id) throws Exception;

    void resume(Long id) throws Exception;

    Optional<GenericKubernetesResource> getStatus(Long id);

    Optional<GenericKubernetesResource> getStatusWithoutManagedFields(Long id);

    Optional<GenericKubernetesResource> getJobWithoutStatus(Long id);

    int updateStatus(Long id, FlinkDeploymentStatus status);

    int updateStatus(Long id, FlinkSessionJobStatus status);

    int clearStatus(Long id);
}
