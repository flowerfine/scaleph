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
import cn.sliew.scaleph.queue.Queue;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SpringApplicationEventQueue implements Queue {

    private final String name;
    private final ApplicationEventPublisher eventPublisher;
    private final SpringApplicationEventListener eventListener;
    private final Timer timer;

    public SpringApplicationEventQueue(String name, ApplicationEventPublisher eventPublisher, SpringApplicationEventListener eventListener, Timer timer) {
        this.name = name;
        this.eventPublisher = eventPublisher;
        this.eventListener = eventListener;
        this.timer = timer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void register(String consumerGroup, MessageHandler handler) {
        eventListener.register(name, consumerGroup, handler);
    }

    @Override
    public void remove(String consumerGroup, MessageHandler handler) {
        eventListener.remove(name, consumerGroup, handler);
    }

    @Override
    public void push(Message message, Duration delay) {
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                eventPublisher.publishEvent(new SpringApplicationEvent(this, message));
            }
        }, delay.toMillis(), TimeUnit.MILLISECONDS);
    }
}
