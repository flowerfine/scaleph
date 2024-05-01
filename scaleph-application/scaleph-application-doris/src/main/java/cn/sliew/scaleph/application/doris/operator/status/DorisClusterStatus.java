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

package cn.sliew.scaleph.application.doris.operator.status;

import lombok.Data;

/**
 * DorisClusterStatus defines the observed state of DorisCluster
 */
@Data
public class DorisClusterStatus {

    /**
     * describe fe cluster status, record running, creating and failed pods.
     */
    private ComponentStatus feStatus;

    /**
     * describe be cluster status, recode running, creating and failed pods.
     */
    private ComponentStatus beStatus;

    /**
     * describe cn cluster status, record running, creating and failed pods.
     */
    private CnStatus cnStatus;

    /**
     * describe broker cluster status, record running, creating and failed pods.
     */
    private ComponentStatus brokerStatus;
}
