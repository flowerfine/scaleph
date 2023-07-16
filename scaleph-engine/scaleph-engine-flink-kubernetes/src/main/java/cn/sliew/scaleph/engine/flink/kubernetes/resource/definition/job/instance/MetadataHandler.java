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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.instance;

import cn.sliew.scaleph.config.resource.ResourceLabels;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MetadataHandler {

    public ObjectMeta handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, ObjectMeta objectMeta) {
        ObjectMetaBuilder builder = Optional.ofNullable(objectMeta)
                .map(meta -> new ObjectMetaBuilder(meta, true))
                .orElse(new ObjectMetaBuilder());

        builder.withName(jobInstanceDTO.getInstanceId());
        addJobLables(jobInstanceDTO, builder);
        return builder.build();
    }

    private void addJobLables(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, ObjectMetaBuilder builder) {
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_NAME, jobDTO.getName());
        Optional.ofNullable(jobDTO.getFlinkDeployment()).ifPresent(flinkDeployment -> builder.addToLabels(ResourceLabels.SCALEPH_LABEL_DEPLOYMENT_ID, flinkDeployment.getDeploymentId()));
        Optional.ofNullable(jobDTO.getFlinkSessionCluster()).ifPresent(flinkSessionCluster -> builder.addToLabels(ResourceLabels.SCALEPH_LABEL_SESSION_CLUSTER_ID, flinkSessionCluster.getSessionClusterId()));
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_JOB_ID, jobDTO.getJobId());
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_CREATOR, jobInstanceDTO.getCreator());
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_EDITOR, jobInstanceDTO.getEditor());
    }

}
