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

import cn.sliew.scaleph.common.dict.flink.NodePortAddressType;
import cn.sliew.scaleph.common.dict.flink.ServiceExposedType;
import cn.sliew.scaleph.application.flink.operator.spec.IngressSpec;

import java.util.HashMap;
import java.util.Map;

public enum FlinkServiceUtil {
    ;

    static Map<String, String> addNodePortService() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.rest-service.exposed.type", ServiceExposedType.NODE_PORT.getValue());
        flinkConfiguration.put("kubernetes.rest-service.exposed.node-port-address-type", NodePortAddressType.EXTERNAL_IP.getValue());
        return flinkConfiguration;
    }

    static Map<String, String> addLoadBalancerService() {
        Map<String, String> flinkConfiguration = new HashMap<>();
        flinkConfiguration.put("kubernetes.rest-service.exposed.type", ServiceExposedType.LOAD_BALANCER.getValue());
        return flinkConfiguration;
    }

    static IngressSpec createIngressSpec() {
        IngressSpec spec = new IngressSpec();
        spec.setTemplate("/{{namespace}}/{{name}}(/|$)(.*)");
        spec.setClassName("nginx");
        Map<String, String> annotations = new HashMap<>();
        annotations.put("nginx.ingress.kubernetes.io/rewrite-target", "/$2");
        spec.setAnnotations(annotations);
        return spec;
    }

}
