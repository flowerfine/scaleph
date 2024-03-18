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

package cn.sliew.scaleph.workflow.queue.spring;


import cn.sliew.scaleph.workflow.queue.Event;
import cn.sliew.scaleph.workflow.queue.EventListener;
import cn.sliew.scaleph.workflow.queue.Queue;

public class SpringApplicationEventQueue<T extends Event> implements Queue<T> {

    private final String name;
    private final SpringApplicationEventPublisher eventPublisher;
    private final SpringApplicationEventListener eventListener;

    public SpringApplicationEventQueue(String name, SpringApplicationEventPublisher eventPublisher, SpringApplicationEventListener eventListener) {
        this.name = name;
        this.eventPublisher = eventPublisher;
        this.eventListener = eventListener;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void register(String consumerGroup, EventListener<T> listener) {
        eventListener.register(consumerGroup, listener);
    }

    @Override
    public void remove(EventListener<T> listener) {
        eventListener.remove(listener);
    }

    @Override
    public void push(T event) {
        eventPublisher.publishEvent(event);
    }
}
