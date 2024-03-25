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

package cn.sliew.scaleph.api.config;

import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.SpawnProtocol;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.apache.pekko.cluster.ClusterEvent;
import org.apache.pekko.cluster.typed.Cluster;
import org.apache.pekko.cluster.typed.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PekkoConfig {

    @Value("${spring.application.name}")
    private String application;

    @Bean(destroyMethod = "terminate")
    public ActorSystem<SpawnProtocol.Command> actorSystem() {
        ActorSystem<SpawnProtocol.Command> actorSystem = ActorSystem.create(Behaviors.setup(ctx -> SpawnProtocol.create()), application);
        actorSystem.whenTerminated().onComplete(done -> {
            if (done.isSuccess()) {
                actorSystem.log().info("pekko ActorSystem terminate success!");
            } else {
                actorSystem.log().error("pekko ActorSystem terminate failure!", done.failed().get());
            }
            return done.get();
        }, actorSystem.executionContext());
        return actorSystem;
    }

    @Bean
    public Cluster actorCluster(ActorSystem actorSystem) {
        return Cluster.get(actorSystem);
    }

    @Component
    public static class PekkoClusterBootstrap implements ApplicationRunner {

        @Autowired
        private Cluster cluster;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            cluster.manager().tell(Join.create(cluster.selfMember().address()));
            ClusterEvent.CurrentClusterState state = cluster.state();
//            cluster.subscriptions().tell(Subscribe.create(null, ClusterEvent.MemberEvent.class));
        }
    }


}
