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
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class BatchPollEventSource<R, P extends HasMetadata>
        extends AbstractResourceEventSource<R, P> {

    private HashedWheelTimer timer;
    private Timeout timeout;

    private Supplier<Map<ResourceID, R>> resourceFetcher;
    private Duration period;

    public BatchPollEventSource(Class<R> resourceClass, Supplier<Map<ResourceID, R>> resourceFetcher, Duration period) {
        super(resourceClass);
        this.resourceFetcher = resourceFetcher;
        this.period = period;
    }

    @Override
    protected void doInitialize() {
        timer = new HashedWheelTimer();
    }

    @Override
    protected void doStart() {
        timer.start();
        this.timeout = timer.newTimeout(new EventProducerTask(), period.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    protected void doStop() {
        if (timeout != null && timeout.isCancelled() == false) {
            timeout.cancel();
        }
        timer.stop();
    }

    private class EventProducerTask implements TimerTask {

        @Override
        public void run(Timeout timeout) throws Exception {
            Map<ResourceID, R> resources = resourceFetcher.get();
            for (Map.Entry<ResourceID, R> entry : resources.entrySet()) {
                getHandler().handleEvent(new Event(entry.getKey()));
            }
            timeout.task().run(timeout);
        }
    }
}
