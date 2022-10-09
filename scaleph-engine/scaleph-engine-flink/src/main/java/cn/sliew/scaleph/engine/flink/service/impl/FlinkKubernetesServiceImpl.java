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
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptions;
import cn.sliew.scaleph.engine.flink.service.enums.FlinkVersionMapping;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.vo.Resource;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;
import org.apache.flink.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        final KubernetesOptions kubernetesOptions = flinkClusterConfigDTO.getKubernetesOptions();
        FlinkDeploymentBuilder builder = new FlinkDeploymentBuilder();
        final SpecConfigurer specConfigurer = builder
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata()
                .name(RandomStringUtils.randomAlphabetic(32).toLowerCase())
                .namespace(kubernetesOptions.getNamespace())
                .and()
                .spec()
                .image(kubernetesOptions.getImage())
                .flinkVersion(FlinkVersionMapping.of(flinkClusterConfigDTO.getFlinkVersion()).getOperatorVersion())
                .jobManager(config -> jobManager(config, kubernetesOptions))
                .taskManager(config -> taskManager(config, kubernetesOptions));
        if (CollectionUtils.isEmpty(flinkClusterConfigDTO.getConfigOptions()) == false) {
            flinkClusterConfigDTO.getConfigOptions().forEach((key, value) -> specConfigurer.flinkConfiguration(key, value));
        }
        final FlinkDeployment flinkDeployment = builder.build();
        try (Resource<Path> clusterCredential = clusterCredentialService.obtain(flinkClusterConfigDTO.getClusterCredential().getId())) {
            Path kubeconfigPath = Files.list(clusterCredential.load()).collect(Collectors.toList()).get(0);
            Config kubeconfig;
            if (StringUtils.hasText(kubernetesOptions.getContext())) {
                kubeconfig = Config.fromKubeconfig(kubernetesOptions.getContext(), FileUtils.readFileUtf8(kubeconfigPath.toFile()), null);
            } else {
                kubeconfig = Config.fromKubeconfig(FileUtils.readFileUtf8(kubeconfigPath.toFile()));
            }
            try (KubernetesClient client = new DefaultKubernetesClient(kubeconfig)) {
                NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicable<FlinkDeployment> resource = client.resource(flinkDeployment);
                final FlinkDeployment orReplace = resource.createOrReplace();
                System.out.println(Serialization.asYaml(orReplace));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Rethrower.throwAs(e);
            }
        }
    }

    private void jobManager(SpecConfigurer.JobManagerSpecConfig config, KubernetesOptions kubernetesOptions) {
        org.apache.flink.kubernetes.operator.crd.spec.Resource resource =
                new org.apache.flink.kubernetes.operator.crd.spec.Resource(kubernetesOptions.getJobManagerCPU(), kubernetesOptions.getJobManagerMemory());
        config.resource(resource);
        config.replicas(kubernetesOptions.getJobManagerReplicas());
    }

    private void taskManager(SpecConfigurer.TaskManagerSpecConfig config, KubernetesOptions kubernetesOptions) {
        org.apache.flink.kubernetes.operator.crd.spec.Resource resource =
                new org.apache.flink.kubernetes.operator.crd.spec.Resource(kubernetesOptions.getTaskManagerCPU(), kubernetesOptions.getTaskManagerMemory());
        config.resource(resource);
        config.replicas(kubernetesOptions.getTaskManagerReplicas());
    }

    @Override
    public void submitSessionJob() throws Exception {

    }

    @Override
    public void submitApplicationJob() throws Exception {

    }
}
