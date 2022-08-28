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

import akka.Done;
import akka.actor.typed.ActorSystem;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import cn.sliew.milky.common.filter.ActionListener;
import cn.sliew.scaleph.workflow.engine.action.Action;
import cn.sliew.scaleph.workflow.engine.action.ActionContext;
import cn.sliew.scaleph.workflow.engine.action.ActionResult;
import cn.sliew.scaleph.workflow.engine.workflow.AbstractWorkFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class AkkaSequentialFlow extends AbstractWorkFlow {

    private final ActorSystem actorSystem;
    private final List<Action> actions = new ArrayList<>();

    public AkkaSequentialFlow(String name, ActorSystem actorSystem, List<Action> actions) {
        super(name);
        this.actorSystem = actorSystem;
        this.actions.addAll(actions);
    }

    @Override
    public void execute(ActionContext context, ActionListener<ActionResult> listener) {
        final CompletionStage<Done> future = Source.from(actions).runWith(doExecute(context, listener), actorSystem);
        future.whenComplete((done, throwable) -> {
            if (throwable != null) {
                listener.onFailure(new Exception(throwable));
            }
        });
    }

    private Sink<Action, CompletionStage<Done>> doExecute(ActionContext context,
                                                          ActionListener<ActionResult> listener) {
        return Sink.foreach(action -> action.execute(context, listener));
    }
}
