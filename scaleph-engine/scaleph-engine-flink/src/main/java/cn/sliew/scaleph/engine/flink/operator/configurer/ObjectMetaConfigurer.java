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

package cn.sliew.scaleph.engine.flink.operator.configurer;

import cn.sliew.scaleph.engine.flink.operator.FlinkDeploymentBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

import static cn.sliew.milky.common.check.Ensures.notBlank;

public class ObjectMetaConfigurer
        extends AbstractFlinkDeploymentConfigurer<ObjectMetaConfigurer, FlinkDeploymentBuilder> {

    private ObjectMetaBuilder builder = new ObjectMetaBuilder(true);

    public ObjectMetaConfigurer name(String name) {
        notBlank(name, () -> "fink deployment builder ObjectMeta.name cannot be blank");
        builder.withName(name);
        return this;
    }

    public ObjectMetaConfigurer namespace(String namespace) {
        notBlank(namespace, () -> "fink deployment builder ObjectMeta.namespace cannot be blank");
        builder.withNamespace(namespace);
        return this;
    }

    public ObjectMetaConfigurer annotations(String annotation, String value) {
        notBlank(annotation, () -> "fink deployment builder ObjectMeta annotation cannot be blank");
        notBlank(value, () -> "fink deployment builder ObjectMeta annotation's value cannot be blank");
        builder.addToAnnotations(annotation, value);
        return this;
    }

    public ObjectMetaConfigurer labels(String label, String value) {
        notBlank(label, () -> "fink deployment builder ObjectMeta label cannot be blank");
        notBlank(value, () -> "fink deployment builder ObjectMeta label's value cannot be blank");
        builder.addToLabels(label, value);
        return this;
    }

    @Override
    public void configure(FlinkDeploymentBuilder flinkDeployment) throws Exception {
        flinkDeployment.setObjectMeta(builder.build());
    }
}
