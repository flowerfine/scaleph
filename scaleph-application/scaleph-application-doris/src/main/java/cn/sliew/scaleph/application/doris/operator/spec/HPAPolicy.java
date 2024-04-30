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

@Data
public class HPAPolicy {

    /**
     * Metrics specifies how to scale based on a single metric the struct copy from k8s.io/api/autoscaling/v2beta2/types.go. the redundancy code will hide the restriction about HorizontalPodAutoscaler version and kubernetes releases matching issue. the splice will have unsafe.Pointer convert, so be careful to edit the struct fileds.
     */
    @Nullable
    private List<MetricSpec> metrics;

    /**
     * HorizontalPodAutoscalerBehavior configures the scaling behavior of the target. the struct copy from k8s.io/api/autoscaling/v2beta2/types.go. the redundancy code will hide the restriction about HorizontalPodAutoscaler version and kubernetes releases matching issue. the
     */
    @Nullable
    private HorizontalPodAutoscalerBehavior behavior;
}
