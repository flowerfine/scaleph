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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.stereotype.Component;

@Component
public class PodTemplateHandler {

    public void handle(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentSpec spec) {
        Pod merge = JsonMerger.merge(spec.getPodTemplate(), jobDTO.getFlinkDeployment().getPodTemplate(), Pod.class);
        spec.setPodTemplate(merge);
    }

    public void handle(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        Pod merge = JsonMerger.merge(spec.getPodTemplate(), sessionClusterDTO.getPodTemplate(), Pod.class);
        spec.setPodTemplate(merge);
    }
}
