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

package cn.sliew.scaleph.application.flink.watch;

import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.kubernetes.watch.watch.shared.KubernetesSharedInformer;
import cn.sliew.scaleph.kubernetes.watch.watch.shared.KubernetesSharedWatcher;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.Informable;
import org.springframework.util.CollectionUtils;

import java.util.Map;

import static cn.sliew.milky.common.check.Ensures.checkArgument;

public class FlinkDeploymentShardWatcher extends KubernetesSharedInformer<FlinkDeployment> implements KubernetesSharedWatcher<FlinkDeployment> {

    public FlinkDeploymentShardWatcher(NamespacedKubernetesClient client, Map<String, String> labels) {
        super(client, getInformableConfigMaps(client, labels));
    }

    private static Informable<FlinkDeployment> getInformableConfigMaps(
            NamespacedKubernetesClient client, Map<String, String> labels) {
        checkArgument(CollectionUtils.isEmpty(labels) == false, () -> "Labels must not be null or empty");
        return client.resources(FlinkDeployment.class).withLabels(labels);
    }
}
