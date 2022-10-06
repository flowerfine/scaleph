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

package cn.sliew.scaleph.engine.flink.operator.configurer;

import cn.sliew.milky.dsl.Customizer;
import cn.sliew.scaleph.engine.flink.operator.FlinkDeploymentBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.kubernetes.operator.crd.spec.*;

import java.util.HashMap;
import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;
import static cn.sliew.milky.common.check.Ensures.notBlank;

public class SpecConfigurer
        extends AbstractFlinkDeploymentConfigurer<SpecConfigurer, FlinkDeploymentBuilder> {

    private final SpecConfigurer.IngressSpecConfig ingressSpec = new IngressSpecConfig();
    private final SpecConfigurer.JobManagerSpecConfig jobManagerSpec = new JobManagerSpecConfig();
    private final SpecConfigurer.TaskManagerSpecConfig taskManagerSpec = new TaskManagerSpecConfig();
    private final SpecConfigurer.PodTemplateSpecConfig podTemplateSpec = new PodTemplateSpecConfig();
    private final SpecConfigurer.JobSpecConfig jobSpec = new JobSpecConfig();

    private String image = "flink:1.15";
    private KubernetesConfigOptions.ImagePullPolicy imagePullPolicy = KubernetesConfigOptions.ImagePullPolicy.IfNotPresent;
    private String serviceAccount = "flink";
    private FlinkVersion flinkVersion = FlinkVersion.v1_15;
    private Map<String, String> logConfiguration = new HashMap<>();
    private Map<String, String> flinkConfiguration = new HashMap<>();

    public SpecConfigurer image(String image) {
        notBlank(image, () -> "fink deployment builder FlinkDeploymentSpec.image cannot be blank");
        this.image = image;
        return this;
    }

    public SpecConfigurer imagePullPolicy(KubernetesConfigOptions.ImagePullPolicy imagePullPolicy) {
        checkNotNull(imagePullPolicy, () -> "fink deployment builder FlinkDeploymentSpec.imagePullPolicy cannot be null");
        this.imagePullPolicy = imagePullPolicy;
        return this;
    }

    public SpecConfigurer serviceAccount(String serviceAccount) {
        notBlank(serviceAccount, () -> "fink deployment builder FlinkDeploymentSpec.serviceAccount cannot be blank");
        this.serviceAccount = serviceAccount;
        return this;
    }

    public SpecConfigurer flinkVersion(FlinkVersion flinkVersion) {
        checkNotNull(flinkVersion, () -> "fink deployment builder FlinkDeploymentSpec.flinkVersion cannot be null");
        this.flinkVersion = flinkVersion;
        return this;
    }

    public SpecConfigurer logConfiguration(String key, String value) {
        logConfiguration.put(key, value);
        return this;
    }

    public SpecConfigurer flinkConfiguration(String key, String value) {
        flinkConfiguration.put(key, value);
        return this;
    }

    public IngressSpecConfig ingress() {
        return ingressSpec;
    }

    public SpecConfigurer ingress(Customizer<IngressSpecConfig> ingressSpecConfigCustomizer) {
        ingressSpecConfigCustomizer.customize(ingressSpec);
        return this;
    }

    public JobManagerSpecConfig jobManager() {
        return jobManagerSpec;
    }

    public SpecConfigurer jobManager(Customizer<JobManagerSpecConfig> jobManagerSpecConfigCustomizer) {
        jobManagerSpecConfigCustomizer.customize(jobManagerSpec);
        return this;
    }

    public TaskManagerSpecConfig taskManager() {
        return taskManagerSpec;
    }

    public SpecConfigurer taskManager(Customizer<TaskManagerSpecConfig> taskManagerSpecConfigCustomizer) {
        taskManagerSpecConfigCustomizer.customize(taskManagerSpec);
        return this;
    }

    public PodTemplateSpecConfig podTemplate() {
        return podTemplateSpec;
    }

    public SpecConfigurer podTemplate(Customizer<PodTemplateSpecConfig> podTemplateSpecConfigCustomizer) {
        podTemplateSpecConfigCustomizer.customize(podTemplateSpec);
        return this;
    }

    public JobSpecConfig job() {
        return jobSpec;
    }

    public SpecConfigurer job(Customizer<JobSpecConfig> jobSpecConfigCustomizer) {
        jobSpecConfigCustomizer.customize(jobSpec);
        return this;
    }


    @Override
    public void configure(FlinkDeploymentBuilder flinkDeployment) throws Exception {
        FlinkDeploymentSpec spec = new FlinkDeploymentSpec();
        spec.setImage(image);
        spec.setImagePullPolicy(imagePullPolicy.name());
        spec.setServiceAccount(serviceAccount);
        spec.setFlinkVersion(flinkVersion);
        spec.setIngress(ingressSpec.builder.build());
        spec.setJobManager(jobManagerSpec.build());
        spec.setTaskManager(taskManagerSpec.build());
        spec.setLogConfiguration(logConfiguration);
        spec.setFlinkConfiguration(flinkConfiguration);
        spec.setPodTemplate(podTemplateSpec.builder.build());
        spec.setJob(jobSpec.build());
        flinkDeployment.setSpec(spec);
    }

    public final class IngressSpecConfig {

        private IngressSpec.IngressSpecBuilder builder;

        private Map<String, String> annotations;

        private IngressSpecConfig() {
            this.builder = IngressSpec.builder();
            this.annotations = new HashMap<>();
        }

        public IngressSpecConfig template(String template) {
            builder.template(template);
            return this;
        }

        public IngressSpecConfig className(String className) {
            builder.className(className);
            return this;
        }

        public IngressSpecConfig annotations(String annotation, String value) {
            annotations.put(annotation, value);
            builder.annotations(annotations);
            return this;
        }

        public SpecConfigurer and() {
            return SpecConfigurer.this;
        }
    }

    public final class JobManagerSpecConfig {

        private Resource resource;

        /**
         * Number of JobManager replicas. Must be 1 for non-HA deployments.
         */
        private int replicas = 1;

        private JobManagerSpecConfig() {
            this.resource = new Resource(1.0, "2048m");
        }

        public JobManagerSpecConfig resource(Resource resource) {
            this.resource = resource;
            return this;
        }

        public JobManagerSpecConfig replicas(int replicas) {
            this.replicas = replicas;
            return this;
        }

        public JobManagerSpec build() {
            return new JobManagerSpec(resource, replicas, null);
        }

        public SpecConfigurer and() {
            return SpecConfigurer.this;
        }
    }

    public final class TaskManagerSpecConfig {

        private Resource resource;

        /**
         * Number of JobManager replicas. Must be 1 for non-HA deployments.
         */
        private int replicas = 1;

        private TaskManagerSpecConfig() {
            this.resource = new Resource(1.0, "2048m");
        }

        public TaskManagerSpecConfig resource(Resource resource) {
            this.resource = resource;
            return this;
        }

        public TaskManagerSpecConfig replicas(int replicas) {
            this.replicas = replicas;
            return this;
        }

        public TaskManagerSpec build() {
            return new TaskManagerSpec(resource, replicas, null);
        }

        public SpecConfigurer and() {
            return SpecConfigurer.this;
        }
    }

    public final class PodTemplateSpecConfig {

        private PodBuilder builder;

        private PodTemplateSpecConfig() {
            this.builder = new PodBuilder();
        }

        public SpecConfigurer and() {
            return SpecConfigurer.this;
        }
    }

    public final class JobSpecConfig {

        private String jarURI;

        /**
         * Parallelism of the Flink job.
         */
        private int parallelism;

        /**
         * Fully qualified main class name of the Flink job.
         */
        private String entryClass;

        /**
         * Arguments for the Flink job main class.
         */
        private String[] args = new String[0];

        private JobSpecConfig() {

        }

        public JobSpecConfig jarURI(String jarURI) {
            this.jarURI = jarURI;
            return this;
        }

        public JobSpecConfig parallelism(int parallelism) {
            this.parallelism = parallelism;
            return this;
        }

        public JobSpecConfig entryClass(String entryClass) {
            this.entryClass = entryClass;
            return this;
        }

        public JobSpecConfig args(String[] args) {
            this.args = args;
            return this;
        }

        public JobSpec build() {
            final JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI(jarURI);
            jobSpec.setParallelism(parallelism);
            jobSpec.setEntryClass(entryClass);
            jobSpec.setArgs(args);
            return jobSpec;
        }

        public SpecConfigurer and() {
            return SpecConfigurer.this;
        }
    }

}
