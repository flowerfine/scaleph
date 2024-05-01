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

package cn.sliew.scaleph.application.doris.service;

import cn.sliew.scaleph.application.doris.operator.DorisCluster;
import cn.sliew.scaleph.application.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceAddParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceListParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

import java.util.List;
import java.util.Optional;

public interface WsDorisOperatorInstanceService {

    Page<WsDorisOperatorInstanceDTO> list(WsDorisOperatorInstanceListParam param);

    List<Long> listAll();

    WsDorisOperatorInstanceDTO selectOne(Long id);

    WsDorisOperatorInstanceDTO fromTemplate(Long templateId);

    DorisCluster asYaml(WsDorisOperatorInstanceDTO dto);

    int insert(WsDorisOperatorInstanceAddParam param);

    int update(WsDorisOperatorInstanceUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    void deploy(Long id);

    void apply(Long id);

    void shutdown(Long id);

    Optional<GenericKubernetesResource> getStatus(Long id);

    Optional<GenericKubernetesResource> getStatusWithoutManagedFields(Long id);

    int updateStatus(Long id, DorisClusterStatus status);

    int clearStatus(Long id);
}
