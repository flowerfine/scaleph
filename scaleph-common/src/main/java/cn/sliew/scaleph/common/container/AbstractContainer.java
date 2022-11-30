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
import cn.sliew.milky.common.lifecycle.AbstractLifeCycle;

public abstract class AbstractContainer extends AbstractLifeCycle implements Container {

    protected Runnable task;
    protected ActionListener<Void> listener;

    @Override
    public void execute(Runnable task, ActionListener<Void> listener) {
        try {
            task.run();
            listener.onResponse(null);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    @Override
    protected abstract void doInitialize();

    @Override
    protected abstract void doStart();

    @Override
    protected abstract void doStop();
}
