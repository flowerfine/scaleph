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
 * AutoScalingPolicy defines the auto scale
 */
@Data
public class AutoScalingPolicy {

    /**
     * the policy of cn autoscale. operator use autoscaling v2.
     */
    private HPAPolicy hpaPolicy;

    /**
     * version represents the autoscaler version for cn service. only support v1,,v2
     */
    private AutoScalerVersion version;

    /**
     * the min numbers of target.
     */
    @Nullable
    private Integer minReplicas;

    /**
     * the max numbers of target.
     */
    @Nullable
    private Integer maxReplicas;
}
