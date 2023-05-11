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

package cn.sliew.scaleph.engine.flink.kubernetes.factory;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.*;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplateSpec;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

import java.util.HashMap;
import java.util.Map;

public enum FlinkTemplateFactory {
    ;

    public static FlinkTemplate create(String name, String namespace, FlinkTemplate defaultTemplate) {
        FlinkTemplate template = new FlinkTemplate();
        ObjectMeta metadata = defaultTemplate.getMetadata();
        if (metadata == null) {
            metadata = new ObjectMetaBuilder()
                    .withName(name)
                    .withNamespace(namespace)
                    .build();
        }
        template.setMetadata(metadata);

        FlinkTemplateSpec spec = defaultTemplate.getSpec();
        if (spec == null) {
            spec = new FlinkTemplateSpec();
        }
        template.setSpec(spec);
        return template;
    }

    public static FlinkTemplate getDefaults() {
        FlinkTemplate defaults = createFlinkTemplateDefaults();
        return create("default", "default", defaults);
    }

    private static FlinkTemplate createFlinkTemplateDefaults() {
        FlinkTemplate template = new FlinkTemplate();
        FlinkTemplateSpec spec = new FlinkTemplateSpec();
        spec.setFlinkVersion(FlinkVersion.v1_16);
        spec.setImage("flink:1.16");
        spec.setImagePullPolicy("IfNotPresent");
        spec.setServiceAccount("flink");
        spec.setMode(KubernetesDeploymentMode.NATIVE);
        spec.setJobManager(createJobManager());
        spec.setTaskManager(createTaskManager());
        spec.setFlinkConfiguration(createFlinkConfiguration());
        spec.setLogConfiguration(createLogConfiguration());
        template.setSpec(spec);
        return template;
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
