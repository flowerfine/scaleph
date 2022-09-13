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

package cn.sliew.scaleph.workflow.engine.workflow;

import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.container.Container;
import cn.sliew.scaleph.common.container.pool.ContainerPool;
import cn.sliew.scaleph.common.container.pool.ContainerValue;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;

import java.util.Collections;
import java.util.List;

public abstract class AbstractWorkFlow implements WorkFlow {

    private final String name;

    public AbstractWorkFlow(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<AttributeKey> getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<AttributeKey> getOutputs() {
        return Collections.emptyList();
    }

    @Override
    public void execute(ActionContext context, ActionListener<ActionResult> listener) {
        for (AttributeKey attributeKey : getInputs()) {
            if (context.hasAttr(attributeKey) == false) {
                listener.onFailure(new RuntimeException(getName() + " require [" + attributeKey.name() + "] input"));
                return;
            }
        }

        final ContainerValue containerValue = getContainer(context);
        try {
            Container container = containerValue.value();
            container.execute(doExecute(context, listener), new ActionListener<Void>() {
                @Override
                public void onResponse(Void unused) {

                }

                @Override
                public void onFailure(Exception e) {
                    listener.onFailure(e);
                }
            });

        } finally {
            containerValue.close();
        }
    }

    protected ContainerValue getContainer(ActionContext context) {
        ContainerPool containerPool = context.getContainerPool();
        return containerPool.obtain();
    }

    protected abstract Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener);
}
