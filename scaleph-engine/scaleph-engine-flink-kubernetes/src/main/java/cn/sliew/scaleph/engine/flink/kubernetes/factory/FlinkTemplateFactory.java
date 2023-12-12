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

import cn.sliew.scaleph.common.dict.flink.*;
import cn.sliew.scaleph.common.dict.image.ImagePullPolicy;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.*;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.template.FlinkTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.template.FlinkTemplateSpec;
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

    public static FlinkTemplate getSessionClusterDefaults() {
        FlinkTemplate defaults = createFlinkTemplateDefaults();
        Map<String, String> flinkConfiguration = new HashMap<>(createSessionClusterConfiguration());
        flinkConfiguration.putAll(defaults.getSpec().getFlinkConfiguration());
        defaults.getSpec().setFlinkConfiguration(flinkConfiguration);
        return create("session-cluster", "default", defaults);
    }

    public static FlinkTemplate getDeploymentDefaults() {
        FlinkTemplate defaults = createFlinkTemplateDefaults();
        Map<String, String> flinkConfiguration = new HashMap<>(createDeploymentServiceConfiguration());
        flinkConfiguration.putAll(defaults.getSpec().getFlinkConfiguration());
        defaults.getSpec().setFlinkConfiguration(flinkConfiguration);
        return create("deployment", "default", defaults);
    }

    private static FlinkTemplate createFlinkTemplateDefaults() {
        FlinkTemplate template = new FlinkTemplate();
        FlinkTemplateSpec spec = new FlinkTemplateSpec();
        spec.setFlinkVersion(FlinkVersion.v1_18);
        spec.setImage("flink:1.18");
        spec.setImagePullPolicy(ImagePullPolicy.IF_NOT_PRESENT.getValue());
        spec.setServiceAccount("flink");
        spec.setMode(KubernetesDeploymentMode.NATIVE);
        spec.setJobManager(createJobManager());
        spec.setTaskManager(createTaskManager());
        spec.setFlinkConfiguration(createFlinkConfiguration());
        spec.setLogConfiguration(createLogConfiguration());
//        spec.setIngress(createIngressSpec());
        template.setSpec(spec);
        return template;
    }

    private static JobManagerSpec createJobManager() {
        return JobManagerSpec.builder()
                .replicas(1)
                .resource(new Resource(1.0, "1G", null))
                .build();
    }

    private static TaskManagerSpec createTaskManager() {
        return TaskManagerSpec.builder()
                .replicas(1)
                .resource(new Resource(1.0, "2G", null))
                .build();
    }

    private static Map<String, String> createFlinkConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("web.cancel.enable", "true");
        flinkConfiguration.put("akka.ask.timeout", "100s");
        flinkConfiguration.put("taskmanager.slot.timeout", "100s");
        flinkConfiguration.putAll(createFailureTolerateConfiguration());
        flinkConfiguration.putAll(createCheckpointConfiguration());
        flinkConfiguration.putAll(createPeriodicSavepointConfiguration());
        flinkConfiguration.putAll(createRestartConfiguration());
        return flinkConfiguration;
    }

    private static Map<String, String> createFailureTolerateConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("restart-strategy", FlinkRestartStrategy.FAILURE_RATE.getValue());
        flinkConfiguration.put("restart-strategy.failure-rate.failure-rate-interval", "10min");
        flinkConfiguration.put("restart-strategy.failure-rate.max-failures-per-interval", "30");
        flinkConfiguration.put("restart-strategy.failure-rate.delay", "10s");
        return flinkConfiguration;
    }

    private static Map<String, String> createCheckpointConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("execution.checkpointing.mode", FlinkSemantic.EXACTLY_ONCE.getValue());
        flinkConfiguration.put("execution.checkpointing.interval", "3min");
        flinkConfiguration.put("execution.checkpointing.max-concurrent-checkpoints", "1");
        flinkConfiguration.put("execution.checkpointing.min-pause", "3min");
        flinkConfiguration.put("execution.checkpointing.timeout", "18min");
        flinkConfiguration.put("execution.checkpointing.externalized-checkpoint-retention", FlinkCheckpointRetain.RETAIN_ON_CANCELLATION.getValue());
        flinkConfiguration.put("state.checkpoints.num-retained", "10");
        return flinkConfiguration;
    }

    private static Map<String, String> createPeriodicSavepointConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.operator.savepoint.format.type", FlinkSavepointType.NATIVE.getValue());
        flinkConfiguration.put("kubernetes.operator.periodic.savepoint.interval", "1h");
        flinkConfiguration.put("kubernetes.operator.savepoint.history.max.count", "24");
        flinkConfiguration.put("kubernetes.operator.savepoint.history.max.age", "72h");
        flinkConfiguration.put("kubernetes.operator.savepoint.trigger.grace-period", "20min");
        return flinkConfiguration;
    }

    private static Map<String, String> createRestartConfiguration() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.enabled", "true");
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.restarts.window", "3d");
        flinkConfiguration.put("kubernetes.operator.cluster.health-check.restarts.threshold", "12");
        return flinkConfiguration;
    }

    public static Map<String, String> createDeploymentServiceConfiguration() {
        Map<String, String> serviceConfiguration = new HashMap<>();
        serviceConfiguration.put("kubernetes.rest-service.exposed.type", ServiceExposedType.NODE_PORT.getValue());
        serviceConfiguration.put("kubernetes.rest-service.exposed.node-port-address-type", NodePortAddressType.EXTERNAL_IP.getValue());
        return serviceConfiguration;
    }

    public static Map<String, String> createSessionClusterConfiguration() {
        Map<String, String> serviceConfiguration = new HashMap<>();
        serviceConfiguration.put("kubernetes.rest-service.exposed.type", ServiceExposedType.LOAD_BALANCER.getValue());
        return serviceConfiguration;
    }

    private static Map<String, String> createLogConfiguration() {
        Map<String, String> logConfiguration = new HashMap<>();
        return logConfiguration;
    }

    public static IngressSpec createIngressSpec() {
        IngressSpec spec = new IngressSpec();
        spec.setTemplate("/{{namespace}}/{{name}}(/|$)(.*)");
        spec.setClassName("nginx");
        Map<String, String> annotations = new HashMap<>();
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");
        spec.setAnnotations(annotations);
        return spec;
    }

}
