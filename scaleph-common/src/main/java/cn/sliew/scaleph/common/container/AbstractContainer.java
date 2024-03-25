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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractContainer extends AbstractLifeCycle implements Container {

    protected Runnable task;
    protected ActionListener<Void> listener;

    @Override
    public void execute(Runnable task, ActionListener<Void> listener) {
        LifeCycleResult initializeResult = initialize();
        if (initializeResult.isSuccess() == false) {
            listener.onFailure(new Exception(initializeResult.getThrowable()));
            return;
        }
        LifeCycleResult startResult = start();
        if (startResult.isSuccess() == false) {
            listener.onFailure(new Exception(startResult.getThrowable()));
            return;
        }
        doExecute(task, listener);
    }


    protected abstract void doExecute(Runnable task, ActionListener<Void> listener);

    @Override
    protected abstract void doInitialize();

    @Override
    protected abstract void doStart();

    @Override
    protected abstract void doStop();
}
