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

package cn.sliew.scaleph.common.container;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;

import java.io.Serializable;

public class ActorContainer extends AbstractContainer {

    interface Command extends Serializable {
        ActorContainer getContainer();
    }

    public static class InitializeCommand implements Command {

        private final ActorContainer container;

        public InitializeCommand(ActorContainer container) {
            this.container = container;
        }

        @Override
        public ActorContainer getContainer() {
            return container;
        }
    }

    public static class StartCommand implements Command {

        private final ActorContainer container;

        public StartCommand(ActorContainer container) {
            this.container = container;
        }

        @Override
        public ActorContainer getContainer() {
            return container;
        }
    }

    public static class StopCommand implements Command {

        private final ActorContainer container;

        public StopCommand(ActorContainer container) {
            this.container = container;
        }

        @Override
        public ActorContainer getContainer() {
            return container;
        }
    }

    public static Behavior<ActorContainer.Command> create(ActorContext<ActorContainer.Command> context) {
        return Behaviors.receive(Command.class)
                .onMessage(InitializeCommand.class, command -> onInitialize(context, command))
                .onMessage(StartCommand.class, command -> onStart(context, command))
                .onMessage(StopCommand.class, command -> onStop(context, command))
                .build();
    }

    private static Behavior<ActorContainer.Command> onInitialize(ActorContext<ActorContainer.Command> context, InitializeCommand command) {
        final LifeCycleResult result = command.getContainer().initialize();
        if (result.isSuccess()) {
            return Behaviors.same();
        }
        final Throwable throwable = result.getThrowable();
        if (throwable != null) {
            context.getLog().error(throwable.getMessage(), throwable);
        }
        return Behaviors.stopped();
    }

    private static Behavior<ActorContainer.Command> onStart(ActorContext<ActorContainer.Command> context, StartCommand command) {
        final LifeCycleResult result = command.getContainer().start();
        if (result.isSuccess()) {
            return Behaviors.same();
        }
        final Throwable throwable = result.getThrowable();
        if (throwable != null) {
            context.getLog().error(throwable.getMessage(), throwable);
        }
        return Behaviors.stopped();
    }

    private static Behavior<ActorContainer.Command> onStop(ActorContext<ActorContainer.Command> context, StopCommand command) {
        final LifeCycleResult result = command.getContainer().stop();
        if (result.isSuccess() == false && result.getThrowable() != null) {
            final Throwable throwable = result.getThrowable();
            context.getLog().error(throwable.getMessage(), throwable);
        }
        return Behaviors.stopped();
    }

    @Override
    protected void doInitialize() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void doStop() {

    }
}
