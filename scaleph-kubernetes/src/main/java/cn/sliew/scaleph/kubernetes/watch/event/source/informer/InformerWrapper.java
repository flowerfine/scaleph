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

package cn.sliew.scaleph.kubernetes.watch.event.source.informer;

import cn.sliew.milky.common.lifecycle.AbstractLifeCycle;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InformerWrapper<T extends HasMetadata> extends AbstractLifeCycle {

    private final SharedIndexInformer<T> informer;
    private final String namespace;

    public InformerWrapper(SharedIndexInformer<T> informer, String namespace) {
        this.informer = informer;
        this.namespace = namespace;
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected void doStart() {
        informer.stopped().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                log.error("informer stopped error", throwable);
            }
        });

        Class<T> apiTypeClass = informer.getApiTypeClass();
        String fullResourceName = HasMetadata.getFullResourceName(apiTypeClass);
        String version = HasMetadata.getVersion(apiTypeClass);

        log.debug("Starting informer for namespace: {} resource: {}", namespace, fullResourceName);
        informer.start().whenComplete((unused, throwable) -> {
            if (throwable != null) {
                log.error("Start informer error for namespace: {} resource: {}", namespace, fullResourceName);
            } else {
                log.debug("Started informer for namespace: {} resource: {}", namespace, fullResourceName);
            }
        });
    }

    @Override
    protected void doStop() {
        informer.stop();
    }

    public void addEventHandler(ResourceEventHandler<T> eventHandler) {
        informer.addEventHandler(eventHandler);
    }

    private String versionedFullResourceName() {
        Class apiTypeClass = informer.getApiTypeClass();
        if (apiTypeClass.isAssignableFrom(GenericKubernetesResource.class)) {
            return GenericKubernetesResource.class.getSimpleName();
        }
        return HasMetadata.getFullResourceName(apiTypeClass) + "/" + HasMetadata.getVersion(apiTypeClass);
    }
}
