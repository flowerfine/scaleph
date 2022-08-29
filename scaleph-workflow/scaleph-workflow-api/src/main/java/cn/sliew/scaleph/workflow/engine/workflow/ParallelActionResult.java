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

import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParallelActionResult implements ActionResult {

    private final ActionContext context;
    private final List<ActionResult> results = new ArrayList<>();

    public ParallelActionResult(ActionContext context, List<ActionResult> results) {
        this.context = context;
        this.results.addAll(results);
    }

    @Override
    public ActionStatus getStatus() {
        return results.stream()
                .map(ActionResult::getStatus)
                .filter(status -> status == ActionStatus.FAILURE)
                .findAny().orElse(ActionStatus.SUCCESS);
    }

    @Override
    public Throwable getCause() {
        Optional<ActionResult> optional = results.stream()
                .filter(result -> result.getCause() != null)
                .findAny();
        if (optional.isPresent()) {
            return optional.get().getCause();
        }
        return null;
    }

    @Override
    public ActionContext getContext() {
        return context;
    }
}
