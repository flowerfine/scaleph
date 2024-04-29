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

package cn.sliew.scaleph.kubernetes.watch.watch.shared;

import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.Informable;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;
import static cn.sliew.milky.common.check.Ensures.checkState;

/**
 * Base class for shared watcher based on {@link SharedIndexInformer}.
 */
@Slf4j
public abstract class KubernetesSharedInformer<T extends HasMetadata>
        implements KubernetesSharedWatcher<T> {

    private final NamespacedKubernetesClient client;
    private final SharedIndexInformer<T> sharedIndexInformer;

    private final ExecutorService informerExecutor;

    private final AggregatedEventHandler aggregatedEventHandler;

    public KubernetesSharedInformer(NamespacedKubernetesClient client, Informable<T> informable) {
        this.client = client;

        informerExecutor = Executors.newSingleThreadExecutor();
        this.aggregatedEventHandler = new AggregatedEventHandler(informerExecutor);
        this.sharedIndexInformer = informable.inform(aggregatedEventHandler, Duration.ofMillis(100).toMillis());
    }

    @Override
    public Watch watch(String name, WatchCallbackHandler<T> handler, Executor executor) {
        return aggregatedEventHandler.watch(name, new WatchCallback<>(handler, executor));
    }

    @Override
    public void close() {
        this.sharedIndexInformer.stop();
        this.informerExecutor.shutdown();
    }

    private String getResourceKey(String name) {
        return client.getNamespace() + "/" + name;
    }

    private class AggregatedEventHandler implements ResourceEventHandler<T> {
        private final Map<String, EventHandler> handlers = new HashMap<>();
        private final ExecutorService executorService;

        private AggregatedEventHandler(ExecutorService executorService) {
            this.executorService = executorService;
        }

        @Override
        public void onAdd(T obj) {
            executorService.execute(
                    () -> findHandler(obj).ifPresent(EventHandler::handleResourceEvent));
        }

        @Override
        public void onUpdate(T oldObj, T newObj) {
            executorService.execute(
                    () -> findHandler(newObj).ifPresent(EventHandler::handleResourceEvent));
        }

        @Override
        public void onDelete(T obj, boolean deletedFinalStateUnknown) {
            executorService.execute(
                    () -> findHandler(obj).ifPresent(EventHandler::handleResourceEvent));
        }

        private Watch watch(String name, WatchCallback<T> watch) {
            final String resourceKey = getResourceKey(name);
            final String watchId = UUID.randomUUID().toString();
            final CompletableFuture<Void> closeFuture = new CompletableFuture<>();
            executorService.execute(
                    () -> {
                        final EventHandler eventHandler =
                                handlers.computeIfAbsent(
                                        resourceKey, key -> new EventHandler(resourceKey));
                        eventHandler.addWatch(watchId, watch);
                    });
            closeFuture.whenCompleteAsync(
                    (ignored, error) -> {
                        if (error != null) {
                            log.error("Unhandled error while closing watcher.", error);
                        }
                        final boolean removeHandler =
                                handlers.get(resourceKey).removeWatch(watchId);
                        if (removeHandler) {
                            handlers.remove(resourceKey);
                        }
                    },
                    executorService);
            return () -> closeFuture.complete(null);
        }

        private Optional<EventHandler> findHandler(T obj) {
            final String resourceKey = getResourceKey(obj.getMetadata().getName());
            return Optional.ofNullable(handlers.get(resourceKey));
        }
    }

    private class EventHandler {
        private final String resourceKey;
        private final Map<String, WatchCallback<T>> callbacks = new HashMap<>();

        private T resource;

        private EventHandler(String resourceKey) {
            this.resourceKey = resourceKey;
            this.resource = sharedIndexInformer.getIndexer().getByKey(resourceKey);
        }

        private void addWatch(String id, WatchCallback<T> callback) {
            log.info("Starting to watch for {}, watching id:{}", resourceKey, id);
            callbacks.put(id, callback);
            if (resource != null) {
                final List<T> resources = Collections.singletonList(resource);
                callback.run(h -> h.onAdded(resources));
            }
        }

        private boolean removeWatch(String id) {
            callbacks.remove(id);
            log.info("Stopped to watch for {}, watching id:{}", resourceKey, id);
            return callbacks.isEmpty();
        }

        private void handleResourceEvent() {
            T newResource = sharedIndexInformer.getIndexer().getByKey(resourceKey);
            T oldResource = this.resource;
            if (newResource == null) {
                if (oldResource != null) {
                    onDeleted(oldResource);
                }
            } else {
                if (oldResource == null) {
                    onAdded(newResource);
                } else if (!oldResource
                        .getMetadata()
                        .getResourceVersion()
                        .equals(newResource.getMetadata().getResourceVersion())) {
                    onModified(newResource);
                }
            }
            this.resource = newResource;
        }

        private void onAdded(T obj) {
            this.callbacks.forEach((id, callback) -> callback.run(h -> h.onAdded(Collections.singletonList(obj))));
        }

        private void onModified(T obj) {
            this.callbacks.forEach(
                    (id, callback) -> callback.run(h -> h.onModified(Collections.singletonList(obj))));
        }

        private void onDeleted(T obj) {
            this.callbacks.forEach(
                    (id, callback) -> callback.run(h -> h.onDeleted(Collections.singletonList(obj))));
        }
    }

    private static final class WatchCallback<T> {
        private final Object callbackLock = new Object();
        private final BlockingQueue<Consumer<WatchCallbackHandler<T>>> callbackQueue = new LinkedBlockingQueue<>();

        private final WatchCallbackHandler<T> handler;
        private final Executor executor;

        private WatchCallback(WatchCallbackHandler<T> handler, @Nullable Executor executor) {
            this.handler = handler;
            this.executor = executor;
        }

        private void run(Consumer<WatchCallbackHandler<T>> handlerConsumer) {
            if (executor == null) {
                handlerConsumer.accept(handler);
                return;
            }
            checkState(callbackQueue.add(handlerConsumer), () -> "Unable to put callback into a queue.");
            executor.execute(
                    () -> {
                        synchronized (callbackLock) {
                            checkNotNull(callbackQueue.poll(), () -> "Callback queue is empty")
                                    .accept(handler);
                        }
                    });
        }
    }
}
