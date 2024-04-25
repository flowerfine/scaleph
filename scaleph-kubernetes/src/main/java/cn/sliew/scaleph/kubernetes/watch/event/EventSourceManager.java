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

package cn.sliew.scaleph.kubernetes.watch.event;

import cn.sliew.scaleph.kubernetes.watch.event.source.EventSource;
import io.fabric8.kubernetes.api.model.GroupVersionKind;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class EventSourceManager implements InitializingBean, DisposableBean {

    private List<EventSource> eventSources = new LinkedList<>();

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void destroy() throws Exception {
        eventSources.forEach(eventSource -> {
            if (eventSource.isStopped() == false) {
                eventSource.stop();
            }
        });
    }

    public void registerNamespace(KubernetesClient client, String namespace) {

    }

    public void registerResourceClass(KubernetesClient client, Class<? extends HasMetadata> resourceClass) {

    }

    public void registerGVK(KubernetesClient client, GroupVersionKind gvk) {

    }


}
