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

package cn.sliew.scaleph.engine.flink.client.v1.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClusterOverviewDTO {

    @JsonProperty("flink-version")
    private String version;
    @JsonProperty("flink-commit")
    private String commitId;

    @JsonProperty("jobs-running")
    private int numJobsRunningOrPending;
    @JsonProperty("jobs-finished")
    private int numJobsFinished;
    @JsonProperty("jobs-cancelled")
    private int numJobsCancelled;
    @JsonProperty("jobs-failed")
    private int numJobsFailed;

    @JsonProperty("taskmanagers")
    private int numTaskManagersConnected;
    @JsonProperty("taskmanagers-blocked")
    private int numTaskManagersBlocked;

    @JsonProperty("slots-total")
    private int numSlotsTotal;
    @JsonProperty("slots-available")
    private int numSlotsAvailable;
    @JsonProperty("slots-free-and-blocked")
    private int numSlotsFreeAndBlocked;
}
