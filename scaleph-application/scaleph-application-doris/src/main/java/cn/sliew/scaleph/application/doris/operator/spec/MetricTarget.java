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

import io.fabric8.kubernetes.api.model.Quantity;
import lombok.Data;

import javax.annotation.Nullable;

/**
 * MetricTarget defines the target value, average value, or average utilization of a specific metric
 */
@Data
public class MetricTarget {

    /**
     * type represents whether the metric type is Utilization, Value, or AverageValue
     */
    private MetricTargetType type;

    /**
     * value is the target value of the metric (as a quantity).
     */
    @Nullable
    private Quantity value;

    /**
     * averageValue is the target value of the average of the metric across all relevant pods (as a quantity)
     */
    @Nullable
    private Quantity averageValue;

    /**
     * averageUtilization is the target value of the average of the resource metric across all relevant pods, represented as a percentage of the requested value of the resource for the pods. Currently only valid for Resource metric source type
     */
    @Nullable
    private Integer averageUtilization;
}
