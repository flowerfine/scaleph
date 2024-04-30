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

package cn.sliew.scaleph.application.doris.operator.spec;

import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * ExportService consisting of expose ports for user access to software service.
 */
@Data
public class ExportService {

    /**
     * Annotations for using function on different cloud platform.
     */
    @Nullable
    private Map<String, String> annotations;

    /**
     * fixme k8s 的 type
     * https://github.com/selectdb/doris-operator/blob/master/doc/api.md#exportservice
     * type of service,the possible value for the service type are : ClusterIP, NodePort, LoadBalancer,ExternalName. More info: https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types
     */
    @Nullable
    private String type;

    /**
     * ServicePort config service for NodePort access mode.
     */
    private List<DorisServicePort> servicePorts;

    /**
     * Only applies to Service Type: LoadBalancer. This feature depends on whether the underlying cloud-provider supports specifying the loadBalancerIP when a load balancer is created. This field will be ignored if the cloud-provider does not support the feature. This field was under-specified and its meaning varies across implementations, and it cannot support dual-stack. As of Kubernetes v1.24, users are encouraged to use implementation-specific annotations when available. This field may be removed in a future API version.
     */
    @Nullable
    private String loadBalancerIP;

}
