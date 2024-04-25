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

import cn.sliew.scaleph.kubernetes.watch.event.Event;
import cn.sliew.scaleph.kubernetes.watch.event.ResourceID;
import cn.sliew.scaleph.kubernetes.watch.event.source.AbstractResourceEventSource;
import io.fabric8.kubernetes.api.model.GroupVersionKind;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;

public class InfomerEventSource<R extends HasMetadata, P extends HasMetadata>
        extends AbstractResourceEventSource<R, P> implements ResourceEventHandler<R> {

    private InformerManager<R> manager;
    private MixedOperation client;

    public InfomerEventSource(Class<R> resourceClass, GroupVersionKind gvk, KubernetesClient client) {
        super(resourceClass);
        this.client = getMixedOperation(client, gvk, resourceClass);
    }

    private MixedOperation getMixedOperation(KubernetesClient client, GroupVersionKind gvk, Class<R> resourceClass) {
        if (gvk != null) {
            return client.genericKubernetesResources(gvk.getGroup() + "/" + gvk.getVersion(), gvk.getKind());
        } else {
            return client.resources(resourceClass);
        }
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected void doStart() {
        // fixme namespace or gvk ?
        this.manager = new InformerManager<>(client, this);
        this.manager.start();
    }

    @Override
    protected void doStop() {
        this.manager.stop();
    }

    @Override
    public void onAdd(R resource) {
        getHandler().handleEvent(new Event(ResourceID.fromResource(resource)));
    }

    @Override
    public void onUpdate(R olsResource, R newResource) {
        getHandler().handleEvent(new Event(ResourceID.fromResource(newResource)));
    }

    @Override
    public void onDelete(R resource, boolean deletedFinalStateUnknown) {
        getHandler().handleEvent(new Event(ResourceID.fromResource(resource)));
    }
}
