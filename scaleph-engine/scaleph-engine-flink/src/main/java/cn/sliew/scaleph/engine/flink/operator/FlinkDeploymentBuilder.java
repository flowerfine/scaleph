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

package cn.sliew.scaleph.engine.flink.operator;

import cn.sliew.milky.dsl.AbstractConfiguredBuilder;
import cn.sliew.milky.dsl.AbstractConfigurer;
import cn.sliew.milky.dsl.Builder;
import cn.sliew.milky.dsl.Customizer;
import cn.sliew.scaleph.engine.flink.operator.configurer.ApiVersionConfigurer;
import cn.sliew.scaleph.engine.flink.operator.configurer.KindConfigurer;
import cn.sliew.scaleph.engine.flink.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.scaleph.engine.flink.operator.configurer.SpecConfigurer;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.Setter;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;
import org.apache.flink.kubernetes.operator.crd.spec.FlinkDeploymentSpec;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;
import static cn.sliew.milky.common.check.Ensures.notBlank;

@Setter
public class FlinkDeploymentBuilder extends AbstractConfiguredBuilder<FlinkDeployment, FlinkDeploymentBuilder>
        implements Builder<FlinkDeployment> {

    private String apiVersion;
    private String kind;
    private ObjectMeta objectMeta;
    private FlinkDeploymentSpec spec;

    @Override
    protected FlinkDeployment performBuild() throws Exception {
        notBlank(apiVersion, () -> "apiVersion can't be blank");
        notBlank(kind, () -> "kind can't be blank");
        checkNotNull(objectMeta, () -> "metadata can't be null");
        checkNotNull(spec, () -> "spec can't be null");

        FlinkDeployment flinkDeployment = new FlinkDeployment();
        flinkDeployment.setApiVersion(apiVersion);
        flinkDeployment.setKind(kind);
        flinkDeployment.setMetadata(objectMeta);
        flinkDeployment.setSpec(spec);
        return flinkDeployment;
    }

    public ApiVersionConfigurer apiVersion() throws Exception {
        return getOrApply(new ApiVersionConfigurer());
    }

    public FlinkDeploymentBuilder apiVersion(Customizer<ApiVersionConfigurer> apiVersionConfigurerCustomizer) throws Exception {
        apiVersionConfigurerCustomizer.customize(getOrApply(new ApiVersionConfigurer()));
        return this;
    }

    public KindConfigurer kind() throws Exception {
        return getOrApply(new KindConfigurer());
    }

    public FlinkDeploymentBuilder kind(Customizer<KindConfigurer> kindConfigurerCustomizer) throws Exception {
        kindConfigurerCustomizer.customize(getOrApply(new KindConfigurer()));
        return this;
    }

    public ObjectMetaConfigurer metadata() throws Exception {
        return getOrApply(new ObjectMetaConfigurer());
    }

    public FlinkDeploymentBuilder metadata(Customizer<ObjectMetaConfigurer> objectMetaConfigurerCustomizer) throws Exception {
        objectMetaConfigurerCustomizer.customize(getOrApply(new ObjectMetaConfigurer()));
        return this;
    }

    public SpecConfigurer spec() throws Exception {
        return getOrApply(new SpecConfigurer());
    }

    public FlinkDeploymentBuilder spec(Customizer<SpecConfigurer> specConfigurerCustomizer) throws Exception {
        specConfigurerCustomizer.customize(getOrApply(new SpecConfigurer()));
        return this;
    }

    private <C extends AbstractConfigurer<FlinkDeployment, FlinkDeploymentBuilder>> C getOrApply(C configurer) throws Exception {
        C existingConfig = (C) getConfigurer(configurer.getClass());
        if (existingConfig != null) {
            return existingConfig;
        }
        return apply(configurer);
    }

}
