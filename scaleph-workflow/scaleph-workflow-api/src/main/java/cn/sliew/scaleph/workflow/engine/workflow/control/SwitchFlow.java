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

package cn.sliew.scaleph.workflow.engine.workflow.control;

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class SwitchFlow extends AbstractWorkFlow {

    private final Action action;
    private final LinkedHashMap<ActionResultCondition, Action> onConditions;

    public SwitchFlow(String name, Action action, LinkedHashMap<ActionResultCondition, Action> onConditions) {
        super(name);
        this.action = action;
        this.onConditions = onConditions;
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> action.execute(context, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {
                for (Map.Entry<ActionResultCondition, Action> entry : onConditions.entrySet()) {
                    final ActionResultCondition condition = entry.getKey();
                    final Action onCondition = entry.getValue();
                    if (condition.test(result)) {
                        onCondition.execute(result.getContext(), listener);
                        break;
                    }
                }
                listener.onFailure(new IllegalStateException("unmatched condition for " + result));
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public static Builder newSwitchFlow() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Action action;
        private final LinkedHashMap<ActionResultCondition, Action> onConditions;

        public Builder() {
            this.name = RandomStringUtils.randomAlphabetic(5);
            this.onConditions = new LinkedHashMap<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder execute(Action action) {
            this.action = action;
            return this;
        }

        public Builder onCondition(ActionResultCondition condition, Action onCondition) {
            onConditions.put(condition, onCondition);
            return this;
        }

        public WorkFlow build() {
            return new SwitchFlow(name, action, onConditions);
        }
    }

}
