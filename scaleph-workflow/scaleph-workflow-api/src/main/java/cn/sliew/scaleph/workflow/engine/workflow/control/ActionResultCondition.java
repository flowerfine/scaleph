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

import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.action.ActionStatus;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

@FunctionalInterface
public interface ActionResultCondition extends Predicate<ActionResult> {

    @Override
    boolean test(ActionResult result);

    ActionResultCondition ALWAYS_TRUE = workReport -> true;
    ActionResultCondition ALWAYS_FALSE = workReport -> false;
    ActionResultCondition SUCCESS = workReport -> workReport.getStatus().equals(ActionStatus.SUCCESS);
    ActionResultCondition FAILURE = workReport -> workReport.getStatus().equals(ActionStatus.FAILURE);

    class TimesPredicate implements ActionResultCondition {

        private final int times;

        private final AtomicInteger counter = new AtomicInteger();

        public TimesPredicate(int times) {
            this.times = times;
        }

        @Override
        public boolean test(ActionResult result) {
            return counter.incrementAndGet() != times;
        }

        public static TimesPredicate times(int times) {
            return new TimesPredicate(times);
        }
    }
}
