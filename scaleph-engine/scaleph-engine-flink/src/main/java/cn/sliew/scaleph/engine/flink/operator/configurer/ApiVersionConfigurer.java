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

import static cn.sliew.milky.common.check.Ensures.notBlank;

public class ApiVersionConfigurer
        extends AbstractFlinkDeploymentConfigurer<ApiVersionConfigurer, FlinkDeploymentBuilder> {

    private String apiVersion = "flink.apache.org/v1beta1";

    public ApiVersionConfigurer apiVersion(String apiVersion) {
        notBlank(apiVersion, () -> "apiVersion cannot be blank");
        this.apiVersion = apiVersion;
        return this;
    }

    @Override
    public void configure(FlinkDeploymentBuilder flinkDeployment) throws Exception {
        flinkDeployment.setApiVersion(apiVersion);
    }
}
