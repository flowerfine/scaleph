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

package cn.sliew.scaleph.kubernetes.watch.event.source.poll;

import cn.sliew.scaleph.kubernetes.watch.event.Event;
import cn.sliew.scaleph.kubernetes.watch.event.ResourceID;
import cn.sliew.scaleph.kubernetes.watch.event.source.AbstractResourceEventSource;
import cn.sliew.scaleph.kubernetes.watch.event.source.ResourceEventAware;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class PerPollEventSource<R, P extends HasMetadata>
        extends AbstractResourceEventSource<R, P>
        implements ResourceEventAware<P> {

    private HashedWheelTimer timer;
    private Map<ResourceID, Timeout> tasks = new ConcurrentHashMap<>();
    private Duration period;

    public PerPollEventSource(Class<R> resourceClass, Duration period) {
        super(resourceClass);
        this.period = period;
    }

    @Override
    protected void doInitialize() {
        timer = new HashedWheelTimer();
    }

    @Override
    protected void doStart() {
        timer.start();
    }

    @Override
    protected void doStop() {
        timer.stop();
    }

    @Override
    public void onResourceCreated(P resource) {
        checkAndRegister(resource);
    }

    @Override
    public void onResourceUpdated(P newResource, P oldResource) {
        checkAndRegister(newResource);
    }

    @Override
    public void onResourceDeleted(P resource) {
        cancel(ResourceID.fromResource(resource));
    }

    private void checkAndRegister(P resource) {
        ResourceID resourceID = ResourceID.fromResource(resource);
        if (tasks.containsKey(resourceID) == false) {
            schedule(resource, period);
        }
    }

    public void schedule(P resource, Duration delay) {
        ResourceID resourceID = ResourceID.fromResource(resource);
        if (tasks.containsKey(resourceID)) {
            cancel(resourceID);
        }
        EventProducerTask task = new EventProducerTask(resourceID);
        Timeout timeout = timer.newTimeout(task, delay.toMillis(), TimeUnit.MILLISECONDS);
        tasks.put(resourceID, timeout);
    }

    public void cancel(ResourceID resourceID) {
        Timeout taskTimeout = tasks.remove(resourceID);
        if (taskTimeout != null && taskTimeout.isCancelled() == false) {
            taskTimeout.cancel();
        }
    }

    private class EventProducerTask implements TimerTask {

        private final ResourceID resourceID;

        public EventProducerTask(ResourceID resourceID) {
            this.resourceID = resourceID;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            getHandler().handleEvent(new Event(resourceID));
            timeout.task().run(timeout);
        }
    }
}
