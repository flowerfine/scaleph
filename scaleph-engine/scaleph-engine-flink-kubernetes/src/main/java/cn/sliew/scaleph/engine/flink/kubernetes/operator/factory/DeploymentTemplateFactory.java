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

package cn.sliew.scaleph.engine.flink.kubernetes.operator.factory;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.entity.DeploymentTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.*;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentTemplateDTO;

import java.util.HashMap;
import java.util.Map;

public enum DeploymentTemplateFactory {
    ;

    public static DeploymentTemplate create(String name, String namespace, DeploymentTemplate defaultTemplate) {
        DeploymentTemplate template = new DeploymentTemplate();
        DeploymentTemplate.DeploymentTemplateMetadata metadata = defaultTemplate.getMetadata();
        if (metadata == null) {
            metadata = new DeploymentTemplate.DeploymentTemplateMetadata();
            metadata.setName(name);
            metadata.setNamespace(namespace);
        }
        template.setMetadata(metadata);

        FlinkDeploymentSpec spec = defaultTemplate.getSpec();
        if (spec == null) {
            spec = FlinkDeploymentSpec.builder().build();
        }
        template.setSpec(spec);
        return template;
    }

    public static DeploymentTemplate getGlobal() {
        return create("default", "default", createGlobalTemplate());
    }

    public static DeploymentTemplate from(WsFlinkKubernetesDeploymentTemplateDTO dto) {
        DeploymentTemplate template = new DeploymentTemplate();
        template.setMetadata(JacksonUtil.toObject(dto.getMetadata(), DeploymentTemplate.DeploymentTemplateMetadata.class));
        template.setSpec(JacksonUtil.toObject(dto.getSpec(), FlinkDeploymentSpec.class));
        return template;
    }

    public static WsFlinkKubernetesDeploymentTemplateDTO to(DeploymentTemplate template) {
        WsFlinkKubernetesDeploymentTemplateDTO dto = new WsFlinkKubernetesDeploymentTemplateDTO();
        dto.setName(template.getMetadata().getName());
        dto.setMetadata(JacksonUtil.toJsonNode(template.getMetadata()));
        dto.setSpec(JacksonUtil.toJsonNode(template.getSpec()));
        return dto;
    }

    private static DeploymentTemplate createGlobalTemplate() {
        DeploymentTemplate template = new DeploymentTemplate();
        FlinkDeploymentSpec spec = new FlinkDeploymentSpec();
        spec.setFlinkVersion(FlinkVersion.v1_13);
        spec.setImage("flink:1.13");
        spec.setImagePullPolicy("IfNotPresent");
        spec.setServiceAccount("flink");
        spec.setMode(KubernetesDeploymentMode.NATIVE);
        spec.setJob(createJob());
        spec.setJobManager(createJobManager());
        spec.setTaskManager(createTaskManager());
        spec.setFlinkConfiguration(createFlinkConfiguration());
        spec.setLogConfiguration(createLogConfiguration());
        template.setSpec(spec);
        return template;
    }

    private static JobSpec createJob() {
        return JobSpec.builder()
                .state(JobState.RUNNING)
                .upgradeMode(UpgradeMode.LAST_STATE)
                .allowNonRestoredState(false)
                .build();
    }

    private static JobManagerSpec createJobManager() {
        return JobManagerSpec.builder()
                .replicas(1)
                .resource(new Resource(1.0, "1G"))
                .build();
    }

    private static TaskManagerSpec createTaskManager() {
        return TaskManagerSpec.builder()
                .replicas(1)
                .resource(new Resource(1.0, "2G"))
                .build();
    }

    private static Map<String, String> createFlinkConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("web.cancel.enable", "false");
        flinkConfiguration.put("execution.checkpointing.externalized-checkpoint-retention", "RETAIN_ON_CANCELLATION");
        flinkConfiguration.put("execution.checkpointing.interval", "10s");
        return flinkConfiguration;
    }

    private static Map<String, String> createLogConfiguration() {
        Map<String, String> logConfiguration = new HashMap<>();
        return logConfiguration;
    }
}
