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

import cn.sliew.flinkful.kubernetes.operator.FlinkDeploymentBuilder;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.dsl.Customizer;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.FlinkKubernetesService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkVersionConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.vo.Resource;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinkKubernetesServiceImpl implements FlinkKubernetesService {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private ClusterCredentialService clusterCredentialService;

    @Override
    public boolean supportOperator() {
        return true;
    }

    @Override
    public void createStandalone() throws Exception {

    }

    @Override
    public void createSession(FlinkSessionClusterAddParam param) throws Exception {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(param.getFlinkClusterConfigId());
        FlinkDeploymentBuilder config = new FlinkDeploymentBuilder();
        final SpecConfigurer specConfigurer = config
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata()
                .name(RandomStringUtils.randomAlphabetic(32).toLowerCase())
                .namespace("default")
                .and()
                .spec()
                .image("flink:1.13") // fixme
                .flinkVersion(FlinkVersionConvert.INSTANCE.toOperatorVersion(flinkClusterConfigDTO.getFlinkVersion()));
        if (CollectionUtils.isEmpty(flinkClusterConfigDTO.getConfigOptions()) == false) {
            flinkClusterConfigDTO.getConfigOptions().forEach((key, value) -> specConfigurer.flinkConfiguration(key, value));
        }
        final FlinkDeployment flinkDeployment = specConfigurer.and().build();
        try (Resource<Path> clusterCredential = clusterCredentialService.obtain(flinkClusterConfigDTO.getClusterCredential().getId())) {
            Path kubeconfig = Files.list(clusterCredential.load()).collect(Collectors.toList()).get(0);
            try (InputStream inputStream = Files.newInputStream(kubeconfig);
                 KubernetesClient client = DefaultKubernetesClient.fromConfig(inputStream)) {
                NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable<FlinkDeployment> resource = client.resource(flinkDeployment);
                final FlinkDeployment orReplace = resource.createOrReplace();
                System.out.println(Serialization.asYaml(orReplace));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Rethrower.throwAs(e);
            }
        }
    }

    @Override
    public void submitSessionJob() throws Exception {

    }

    @Override
    public void submitApplicationJob() throws Exception {

    }
}
