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

import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SequentialFlow extends AbstractWorkFlow {

    private final List<Action> actions = new ArrayList<>();

    public SequentialFlow(String name, List<Action> actions) {
        super(name);
        this.actions.addAll(actions);
    }

    @Override
    protected Runnable doExecute(ActionContext context, ActionListener<ActionResult> listener) {
        return () -> {
            try {
                sequentialExecute(0, context, listener);
            } catch (Exception e) {
                listener.onFailure(e);
            }
        };
    }

    private void sequentialExecute(int index, ActionContext context, ActionListener<ActionResult> listener) {
        Action action = actions.get(index);
        action.execute(context, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {
                if (index == actions.size()) {
                    listener.onResponse(result);
                } else {
                    sequentialExecute(index + 1, context, listener);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }

    public static Builder newSequentialFlow() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private final List<Action> actions;

        private Builder() {
            this.name = RandomStringUtils.randomAlphabetic(5);
            this.actions = new ArrayList<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder execute(Action... actions) {
            this.actions.addAll(Arrays.asList(actions));
            return this;
        }

        public WorkFlow build() {
            return new SequentialFlow(name, actions);
        }
    }

}
