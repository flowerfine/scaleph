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

import cn.sliew.milky.common.chain.ContextMap;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractConditionFlow;
import cn.sliew.scaleph.workflow.engine.workflow.ActionResultCondition;

public class WhileFlow extends AbstractConditionFlow {

    private final Action action;

    public WhileFlow(String name, ActionResultCondition condition, Action action) {
        super(name, condition);
        this.action = action;
    }

    @Override
    public void execute(ContextMap<String, Object> context, ActionListener<ActionResult> listener) {
        action.execute(context, new ActionListener<ActionResult>() {
            @Override
            public void onResponse(ActionResult result) {
                if (getCondition().test(result)) {
                    listener.onResponse(result);
                } else {
                    action.execute(result.getContext(), listener);
                }
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure(e);
            }
        });
    }
}
