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
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SpringApplicationEventListener implements ApplicationListener<SpringApplicationEvent> {

    private ConcurrentMap<String, List<EventListener>> registry = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent) {
        Event event = springApplicationEvent.getEvent();
        List<EventListener> listeners = registry.computeIfAbsent(event.getTopic(), key -> new ArrayList<>());
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public void register(String topic, String consumerGroup, EventListener listener) {
        List<EventListener> listeners = registry.computeIfAbsent(topic, key -> new ArrayList<>());
        if (listeners.contains(listeners) == false) {
            listeners.add(listener);
        }
    }

    public void remove(String topic, EventListener listener) {
        List<EventListener> listeners = registry.computeIfAbsent(topic, key -> new ArrayList<>());
        listeners.remove(listener);
    }
}
