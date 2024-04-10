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

import cn.sliew.scaleph.queue.Queue;
import cn.sliew.scaleph.queue.QueueFactory;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SpringApplicationQueueFactory implements QueueFactory, ApplicationEventPublisherAware, InitializingBean, DisposableBean {

    private ConcurrentMap<String, Queue> queues = new ConcurrentHashMap<>();

    private Timer timer;
    @Autowired
    private SpringApplicationEventListener eventListener;
    private ApplicationEventPublisher eventPublisher;
    // 设置 executor，变更为异步操作
//    private SimpleApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.timer = new HashedWheelTimer();
    }

    @Override
    public void destroy() throws Exception {
        timer.stop();
    }

    @Override
    public Queue create(String name) {
        return new SpringApplicationEventQueue(name, eventPublisher, eventListener, timer);
    }

    @Override
    public Queue get(String name) {
        return queues.computeIfAbsent(name, key -> create(key));
    }
}
