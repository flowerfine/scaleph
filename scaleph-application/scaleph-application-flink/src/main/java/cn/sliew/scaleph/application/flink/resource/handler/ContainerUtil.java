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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodFluent;
import io.fabric8.kubernetes.api.model.PodSpecFluent;

public enum ContainerUtil {
    ;

    public static PodSpecFluent<PodFluent<PodBuilder>.SpecNested<PodBuilder>>.ContainersNested<PodFluent<PodBuilder>.SpecNested<PodBuilder>> findFlinkMainContainer(PodFluent<PodBuilder>.SpecNested<PodBuilder> spec) {
        if (spec.hasMatchingContainer(containerBuilder -> containerBuilder.getName().equals(ResourceNames.FLINK_MAIN_CONTAINER_NAME))) {
            return spec.editMatchingContainer((containerBuilder -> containerBuilder.getName().equals(ResourceNames.FLINK_MAIN_CONTAINER_NAME)));
        } else {
            return spec.addNewContainer()
                    .withName(ResourceNames.FLINK_MAIN_CONTAINER_NAME);
        }
    }
}
