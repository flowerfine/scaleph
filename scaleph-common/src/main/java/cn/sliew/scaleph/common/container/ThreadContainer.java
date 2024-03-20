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

package cn.sliew.scaleph.common.container;

import cn.sliew.milky.common.filter.ActionListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;

public class ThreadContainer extends AbstractContainer {

    private final Executor executor;

    private transient CompletableFuture<Void> future;

    public ThreadContainer(Executor executor) {
        this.executor = checkNotNull(executor);
    }

    @Override
    protected void doExecute(Runnable task, ActionListener<Void> listener) {
        future = CompletableFuture.runAsync(task, executor);
        future.whenComplete((unused, throwable) -> {
            if (throwable != null) {
                listener.onFailure(new Exception(throwable));
            } else {
                listener.onResponse(unused);
            }
        });
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {
        if (future.isDone() == false) {
            future.cancel(true);
        }
    }
}
