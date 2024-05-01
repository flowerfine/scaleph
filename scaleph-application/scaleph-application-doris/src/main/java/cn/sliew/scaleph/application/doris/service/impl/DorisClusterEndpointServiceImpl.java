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

package cn.sliew.scaleph.application.doris.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorInstanceService;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.application.doris.service.DorisClusterEndpointService;
import cn.sliew.scaleph.application.doris.service.dto.DorisClusterFeEndpoint;
import cn.sliew.scaleph.kubernetes.service.ServiceService;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Service
public class DorisClusterEndpointServiceImpl implements DorisClusterEndpointService {

    @Autowired
    private WsDorisOperatorInstanceService wsDorisOperatorInstanceService;
    @Autowired
    private ServiceService serviceService;

    @Override
    public DorisClusterFeEndpoint getFEEndpoint(Long dorisInstanceId) {
        WsDorisOperatorInstanceDTO instanceDTO = wsDorisOperatorInstanceService.selectOne(dorisInstanceId);
        if (instanceDTO.getDeployed() == YesOrNo.NO) {
            return null;
        }

        if (instanceDTO.getFeStatus() == null || instanceDTO.getBeStatus() == null) {
            Optional<GenericKubernetesResource> status = wsDorisOperatorInstanceService.getStatusWithoutManagedFields(instanceDTO.getId());
            status.ifPresent(genericKubernetesResource -> {
                String json = JacksonUtil.toJsonString(genericKubernetesResource.get("status"));
                DorisClusterStatus dorisClusterStatus = JacksonUtil.parseJsonString(json, DorisClusterStatus.class);
                instanceDTO.setFeStatus(dorisClusterStatus.getFeStatus());
                instanceDTO.setBeStatus(dorisClusterStatus.getBeStatus());
            });
        }

        Optional<Map<String, URI>> optional = serviceService.getService(instanceDTO.getClusterCredentialId(), instanceDTO.getNamespace(), instanceDTO.getFeStatus().getAccessService());
        if (optional.isEmpty()) {
            return null;
        }
        DorisClusterFeEndpoint result = new DorisClusterFeEndpoint();
        Map<String, URI> uris = optional.get();
        for (Map.Entry<String, URI> entry : uris.entrySet()) {
            switch (entry.getKey()) {
                case "http-port":
                    result.setHttp(entry.getValue());
                    break;
                case "rpc-port":
                    result.setRpc(entry.getValue());
                    break;
                case "query-port":
                    result.setQuery(entry.getValue());
                    break;
                case "edit-log-port":
                    result.setEditLog(entry.getValue());
                    break;
                default:
            }
        }
        return result;
    }
}
