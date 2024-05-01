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

/**
 * MetricSpec specifies how to scale based on a single metric (only type and one other matching field should be set at once).
 */
@Data
public class MetricSpec {

    /**
     * type is the type of metric source. It should be one of “ContainerResource”, “External”, “Object”, “Pods” or “Resource”, each mapping to a matching field in the object. Note: “ContainerResource” type is available on when the feature-gate HPAContainerMetrics is enabled
     */
    private MetricSourceType type;

    /**
     * pods refers to a metric describing each pod in the current scale target (for example, transactions-processed-per-second). The values will be averaged together before being compared to the target value.
     */
    @Nullable
    private ObjectMetricSource object;

    /**
     * resource refers to a resource metric (such as those specified in requests and limits) known to Kubernetes describing each pod in the current scale target (e.g. CPU or memory). Such metrics are built in to Kubernetes, and have special scaling options on top of those available to normal per-pod metrics using the “pods” source.
     */
    @Nullable
    private ResourceMetricSource resource;

    /**
     * container resource refers to a resource metric (such as those specified in requests and limits) known to Kubernetes describing a single container in each pod of the current scale target (e.g. CPU or memory). Such metrics are built in to Kubernetes, and have special scaling options on top of those available to normal per-pod metrics using the “pods” source. This is an alpha feature and can be enabled by the HPAContainerMetrics feature flag.
     */
    @Nullable
    private ContainerResourceMetricSource containerResource;

    /**
     * external refers to a global metric that is not associated with any Kubernetes object. It allows autoscaling based on information coming from components running outside of cluster (for example length of queue in cloud messaging service, or QPS from loadbalancer running outside of cluster).
     */
    @Nullable
    private ExternalMetricSource external;
}
