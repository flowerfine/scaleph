package cn.sliew.scaleph.kubernetes.watch.event.source.informer;

import cn.sliew.milky.common.lifecycle.AbstractLifeCycle;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.KubernetesResourceList;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class InformerManager<T extends HasMetadata> extends AbstractLifeCycle {

    private final Map<String, InformerWrapper<T>> cache = new ConcurrentHashMap<>();
    private final MixedOperation<T, KubernetesResourceList<T>, Resource<T>> client;
    private final ResourceEventHandler<T> eventHandler;

    InformerManager(MixedOperation<T, KubernetesResourceList<T>, Resource<T>> client, ResourceEventHandler<T> eventHandler) {
        this.client = client;
        this.eventHandler = eventHandler;
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {
        cache.forEach((ns, source) -> {
            try {
                log.debug("Stopping informer for namespace: {} -> {}", ns, source);
                source.stop();
            } catch (Exception e) {
                log.warn("Error stopping informer for namespace: {} -> {}", ns, source, e);
            }
        });
        cache.clear();
    }

    public InformerWrapper<T> getOrCreateEventSource(String namespace) {
        InformerWrapper<T> source = cache.get(namespace);
        if (source == null) {
            source = createEventSourceForNamespace(namespace);
        }
        return source;
    };

    private InformerWrapper<T> createEventSourceForNamespace(String namespace) {
        LabelSelectorBuilder builder = new LabelSelectorBuilder();
        builder.addToMatchLabels(Collections.emptyMap());
        FilterWatchListDeletable<T, KubernetesResourceList<T>, Resource<T>> selector = client
                .inNamespace(namespace)
                .withLabelSelector(builder.build());
        return createEventSource(selector, namespace);
    }

    private InformerWrapper<T> createEventSource(FilterWatchListDeletable<T, KubernetesResourceList<T>, Resource<T>> selectorClient, String namespace) {
        SharedIndexInformer<T> informer = selectorClient.runnableInformer(0);
        InformerWrapper<T> source = new InformerWrapper<>(informer, namespace);
        source.addEventHandler(eventHandler);
        source.initialize();
        source.start();
        cache.put(namespace, source);
        return source;
    }
}
