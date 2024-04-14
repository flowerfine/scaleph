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

package cn.sliew.scaleph.kubernetes.watch.informer;

import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Component
public class InformerManager implements InitializingBean, DisposableBean {

    private final ConcurrentMap<Long, SharedInformerFactory> informerMap = new ConcurrentHashMap<>();

    @Autowired
    private ClusterCredentialService clusterCredentialService;
    @Autowired
    private KubernetesService kubernetesService;
    @Autowired
    private Map<String, KubernetesInformerWatchHandler> watchHandlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Long> clusterCredentialIds = clusterCredentialService.listAll();
        for (Long clusterCredentialId : clusterCredentialIds) {
            initCluster(clusterCredentialId);
        }
    }

    @Override
    public void destroy() throws Exception {
        informerMap.forEach(this::stopCluster);
    }

    private void initCluster(Long clusterCredentialId) {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        informerMap.put(clusterCredentialId, client.informers());
        SharedInformerFactory informer = client.informers();
        watchHandlers.values().forEach(handler -> registerWatchHandler(clusterCredentialId, client, informer, handler));
        informer.startAllRegisteredInformers();
        log.info("shared informer start success, clusterCredentialId: {}", clusterCredentialId);
    }

    private void registerWatchHandler(Long clusterCredentialId, KubernetesClient client, SharedInformerFactory informer, KubernetesInformerWatchHandler watchHandler) {
        watchHandler.register(clusterCredentialId, client, informer);
    }

    private void stopCluster(Long clusterCredentialId, SharedInformerFactory informer) {
        informer.stopAllRegisteredInformers();
        log.info("shared informer stop success, clusterCredentialId {}", clusterCredentialId);
    }
}
