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

package cn.sliew.scaleph.application.flink.factory;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import org.springframework.beans.BeanUtils;

public enum FlinkDeploymentFactory {
    ;

    public static FlinkDeployment fromSessionCluster(FlinkSessionCluster sessionCluster) {
        FlinkDeployment flinkDeployment = new FlinkDeployment();
        flinkDeployment.setMetadata(buildMetadata(sessionCluster.getMetadata()));
        flinkDeployment.setSpec(buildSpec(sessionCluster.getSpec()));
        return flinkDeployment;
    }

    private static ObjectMeta buildMetadata(ObjectMeta objectMeta) {
        ObjectMeta meta = new ObjectMeta();
        BeanUtils.copyProperties(objectMeta, meta);
        return meta;
    }

    private static FlinkDeploymentSpec buildSpec(FlinkSessionClusterSpec flinkSessionClusterSpec) {
        FlinkDeploymentSpec spec = new FlinkDeploymentSpec();
        BeanUtils.copyProperties(flinkSessionClusterSpec, spec);
        return spec;
    }
}
