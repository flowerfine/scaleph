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

package cn.sliew.scaleph.application.flink.resource.definition.job.instance;

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceLabels;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MetadataHandler {

    public ObjectMeta handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, ObjectMeta objectMeta) {
        ObjectMetaBuilder builder = Optional.ofNullable(objectMeta)
                .map(meta -> new ObjectMetaBuilder(meta))
                .orElse(new ObjectMetaBuilder());

        builder.withName(jobInstanceDTO.getInstanceId());
        addJobLables(jobInstanceDTO, builder);
        return builder.build();
    }

    public ObjectMeta handle(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, ObjectMeta objectMeta) {
        ObjectMetaBuilder builder = Optional.ofNullable(objectMeta)
                .map(meta -> new ObjectMetaBuilder(meta))
                .orElse(new ObjectMetaBuilder());

        builder.withName(sessionClusterDTO.getSessionClusterId());
        addSessionClusterLables(sessionClusterDTO, builder);
        return builder.build();
    }

    private void addJobLables(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, ObjectMetaBuilder builder) {
        Map<String, String> lables = generateLables(jobInstanceDTO);
        builder.addToLabels(lables);
    }

    private void addSessionClusterLables(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, ObjectMetaBuilder builder) {
        Map<String, String> lables = generateLables(sessionClusterDTO);
        builder.addToLabels(lables);
    }

    public Map<String, String> generateLables(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) {
        Map<String, String> labels = new HashMap<>();
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();

        labels.put(ResourceLabels.SCALEPH_LABEL_PLATFROM, ResourceLabels.SCALEPH);
        labels.put(ResourceLabels.SCALEPH_LABEL_NAME, jobDTO.getName());
        Optional.ofNullable(jobDTO.getFlinkDeployment()).ifPresent(flinkDeployment -> labels.put(ResourceLabels.SCALEPH_LABEL_DEPLOYMENT_ID, flinkDeployment.getDeploymentId()));
        Optional.ofNullable(jobDTO.getFlinkSessionCluster()).ifPresent(flinkSessionCluster -> labels.put(ResourceLabels.SCALEPH_LABEL_SESSION_CLUSTER_ID, flinkSessionCluster.getSessionClusterId()));
        labels.put(ResourceLabels.SCALEPH_LABEL_JOB_ID, jobDTO.getJobId());
        labels.put(ResourceLabels.SCALEPH_LABEL_JOB_INSTANCE_ID_NUMBER, String.valueOf(jobInstanceDTO.getId()));
        labels.put(ResourceLabels.SCALEPH_LABEL_JOB_INSTANCE_ID, jobInstanceDTO.getInstanceId());
        labels.put(ResourceLabels.SCALEPH_LABEL_CREATOR, jobInstanceDTO.getCreator());
        labels.put(ResourceLabels.SCALEPH_LABEL_EDITOR, jobInstanceDTO.getEditor());
        return labels;
    }

    public Map<String, String> generateLables(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) {
        Map<String, String> labels = new HashMap<>();

        labels.put(ResourceLabels.SCALEPH_LABEL_PLATFROM, ResourceLabels.SCALEPH);
        labels.put(ResourceLabels.SCALEPH_LABEL_NAME, sessionClusterDTO.getName());
        labels.put(ResourceLabels.SCALEPH_LABEL_SESSION_CLUSTER_ID, sessionClusterDTO.getSessionClusterId());
        labels.put(ResourceLabels.SCALEPH_LABEL_CREATOR, sessionClusterDTO.getCreator());
        labels.put(ResourceLabels.SCALEPH_LABEL_EDITOR, sessionClusterDTO.getEditor());
        return labels;
    }

}
