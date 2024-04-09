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

package cn.sliew.scaleph.queue.spring;

import cn.sliew.scaleph.queue.Message;
import cn.sliew.scaleph.queue.MessageHandler;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SpringApplicationEventListener implements ApplicationListener<SpringApplicationEvent> {

    // row, column, values
    private Table<String, String, List<MessageHandler>> registry = HashBasedTable.create();

    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent) {
        Message message = springApplicationEvent.getMessage();

        Map<String, List<MessageHandler>> handlerGroup = registry.row(message.getTopic());
        for (Map.Entry<String, List<MessageHandler>> entry : handlerGroup.entrySet()) {
            String consumerGroup = entry.getKey();
            List<MessageHandler> handlers = entry.getValue();
            if (CollectionUtils.isEmpty(handlers) == false) {
                MessageHandler handler = handlers.get(0);
                try {
                    handler.handler(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void register(String topic, String consumerGroup, MessageHandler handler) {
        Map<String, List<MessageHandler>> handlerGroup = registry.row(topic);
        List<MessageHandler> handlers = handlerGroup.computeIfAbsent(consumerGroup, key -> new ArrayList<>());
        if (handlers.contains(handler) == false) {
            handlers.add(handler);
        }
    }

    public void remove(String topic, String consumerGroup, MessageHandler handler) {
        Map<String, List<MessageHandler>> handlerGroup = registry.row(topic);
        List<MessageHandler> handlers = handlerGroup.computeIfAbsent(consumerGroup, key -> new ArrayList<>());
        if (handlers.contains(handler)) {
            handlers.remove(handler);
        }
    }
}
