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

package cn.sliew.scaleph.engine.flink.kubernetes.watch;

import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.kubernetes.watch.AbstractKubernetesWatcher;
import cn.sliew.scaleph.kubernetes.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class FlinkDeploymentWatcher extends AbstractKubernetesWatcher<FlinkDeployment> {

    public FlinkDeploymentWatcher(WatchCallbackHandler<FlinkDeployment> callbackHandler) {
        super(callbackHandler);
    }

    @Override
    public void eventReceived(Action action, FlinkDeployment flinkDeployment) {
        log.debug(
                "Received {} event for pod {}, details: {}{}",
                action,
                flinkDeployment.getMetadata().getName(),
                System.lineSeparator(),
                Serialization.asYaml(flinkDeployment.getStatus()));
        final List<FlinkDeployment> pods = Collections.singletonList(flinkDeployment);
        switch (action) {
            case ADDED:
                callbackHandler.onAdded(pods);
                break;
            case MODIFIED:
                callbackHandler.onModified(pods);
                break;
            case DELETED:
                callbackHandler.onDeleted(pods);
                break;
            case ERROR:
                callbackHandler.onError(pods);
                break;
            default:
                log.debug(
                        "Ignore handling {} event for pod {}", action, flinkDeployment.getMetadata().getName());
        }
    }
}
