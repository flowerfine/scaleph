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
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;
import org.apache.commons.lang3.RandomStringUtils;

public class IfFlow extends AbstractCondition {

    private final Action action;
    private final Action onSuccess;
    private final Action onFailure;

    public IfFlow(String name, ActionResultCondition condition, Action action, Action onSuccess, Action onFailure) {
        super(name, condition);
        this.action = action;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> action.execute(context, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {
                if (getCondition().test(result)) {
                    onSuccess.execute(result.getContext(), listener);
                } else {
                    onFailure.execute(result.getContext(), listener);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public static Builder newIfFlow() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private Action action;
        private ActionResultCondition condition;
        private Action onSuccess;
        private Action onFailure;

        public Builder() {
            this.name = RandomStringUtils.randomAlphabetic(5);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder execute(Action action, ActionResultCondition condition) {
            this.action = action;
            this.condition = condition;
            return this;
        }

        public Builder onSuccess(Action onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public Builder onFailure(Action onFailure) {
            this.onFailure = onFailure;
            return this;
        }

        public WorkFlow build() {
            return new IfFlow(name, condition, action, onSuccess, onFailure);
        }
    }

}
