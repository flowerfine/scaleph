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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.milky.dsl.Customizer;
import cn.sliew.scaleph.engine.flink.operator.FlinkDeploymentBuilder;
import cn.sliew.scaleph.engine.flink.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.scaleph.engine.flink.operator.configurer.SpecConfigurer;
import cn.sliew.scaleph.engine.flink.service.FlinkKubernetesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;
import org.springframework.stereotype.Service;

@Service
public class FlinkKubernetesServiceImpl implements FlinkKubernetesService {

    @Override
    public void createSession() throws Exception {
        final FlinkDeployment flinkDeployment = build();
        final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        System.out.println(mapper.writeValueAsString(flinkDeployment));
    }

    @Override
    public FlinkDeployment build() throws Exception {
        FlinkDeploymentBuilder config = new FlinkDeploymentBuilder();
        config
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata(this::metadata)
                .spec(this::spec);

        return config.getOrBuild();
    }

    private void metadata(ObjectMetaConfigurer objectMetaConfigurer) {
        objectMetaConfigurer.name("Basic");
    }

    private void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .ingress(this::ingress)
                .jobManager(this::jobManager)
                .taskManager(this::taskManager)
                .podTemplate(this::podTemplate)
                .job(this::job);
    }

    private void ingress(SpecConfigurer.IngressSpecConfig config) {

    }

    private void jobManager(SpecConfigurer.JobManagerSpecConfig config) {

    }

    private void taskManager(SpecConfigurer.TaskManagerSpecConfig config) {

    }

    private void podTemplate(SpecConfigurer.PodTemplateSpecConfig config) {

    }

    private void job(SpecConfigurer.JobSpecConfig config) {

    }

}
