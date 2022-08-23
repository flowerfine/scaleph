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

package cn.sliew.scaleph.workflow.akka;

import akka.actor.typed.ActorSystem;
import akka.stream.javadsl.Source;
import cn.sliew.milky.common.chain.ContextMap;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.Engine;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.WorkFlow;

public class AkkaEngine implements Engine {

    private final ActorSystem actorSystem;

    public AkkaEngine(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public void run(WorkFlow workflow, ContextMap<String, Object> context, ActionListener<ActionResult> listener) {
        Source.single(workflow).runForeach(flow -> flow.execute(context, listener), actorSystem);
    }
}
