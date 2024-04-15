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

package cn.sliew.scaleph.workflow.engine.action;

import cn.sliew.milky.common.constant.AttributeKey;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskType;

import java.util.List;

public interface Action {

    default WorkflowTaskType getType() {
        return WorkflowTaskType.JAVA;
    }

    String getName();

    List<AttributeKey> getInputs();

    List<AttributeKey> getOutputs();

    void execute(ActionContext context, ActionListener<ActionResult> listener);

    default void onTimeout(ActionContext context, ActionListener<ActionResult> listener) {

    }

    default void onTerminate(ActionContext context, ActionListener<ActionResult> listener) {

    }
}
