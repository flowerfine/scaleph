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

package cn.sliew.scaleph.kubernetes.watch.generic.informer;

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
    // 切换成 WatchCallbackHandler
    @Autowired(required = false)
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

    /**
     * todo namespace。命名空间级别的
     * todo 事件监听应该是 2 级的，默认是 namespace 级别的，可以做到更进一步资源级别的
     * todo 就比如 eventHandler，可以是单个的，也可以是经过聚合后分发给多个 eventHandler
     * todo 注意 list-watch 和 informer 的区别，informer 是 list-watch 的一个封装，
     * todo 先调用 list 获取全量，在调用 watch 获取增量事件
     */
    private void initCluster(Long clusterCredentialId) {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        SharedInformerFactory informer = client.informers();
        informerMap.put(clusterCredentialId, informer);
        if (watchHandlers != null) {
            watchHandlers.values().forEach(handler -> registerWatchHandler(clusterCredentialId, client, informer, handler));
        }
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
