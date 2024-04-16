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

package cn.sliew.scaleph.application.flink.operator.status;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

/** Last observed status of the Flink deployment. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class FlinkDeploymentStatus extends CommonStatus<FlinkDeploymentSpec> {

    /** Information from running clusters. */
    private Map<String, String> clusterInfo = new HashMap<>();

    /** Last observed status of the JobManager deployment. */
    private JobManagerDeploymentStatus jobManagerDeploymentStatus =
            JobManagerDeploymentStatus.MISSING;

    /** Status of the last reconcile operation. */
    private FlinkDeploymentReconciliationStatus reconciliationStatus =
            new FlinkDeploymentReconciliationStatus();

    /** Information about the TaskManagers for the scale subresource. */
    private TaskManagerInfo taskManager;
}
