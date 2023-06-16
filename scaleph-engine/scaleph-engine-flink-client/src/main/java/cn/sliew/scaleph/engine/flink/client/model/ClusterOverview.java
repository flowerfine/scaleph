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

package cn.sliew.scaleph.engine.flink.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * Response to the {@link RequestStatusOverview} message, carrying a description of the Flink
 * cluster status.
 */
@Getter
public class ClusterOverview extends JobsOverview {

    private static final long serialVersionUID = -729861859715105265L;

    public static final String FIELD_NAME_TASKMANAGERS = "taskmanagers";
    public static final String FIELD_NAME_SLOTS_TOTAL = "slots-total";
    public static final String FIELD_NAME_SLOTS_AVAILABLE = "slots-available";
    public static final String FIELD_NAME_TASKMANAGERS_BLOCKED = "taskmanagers-blocked";
    public static final String FIELD_NAME_SLOTS_FREE_AND_BLOCKED = "slots-free-and-blocked";

    @JsonProperty(FIELD_NAME_TASKMANAGERS)
    private final int numTaskManagersConnected;

    @JsonProperty(FIELD_NAME_SLOTS_TOTAL)
    private final int numSlotsTotal;

    @JsonProperty(FIELD_NAME_SLOTS_AVAILABLE)
    private final int numSlotsAvailable;

    @JsonProperty(FIELD_NAME_TASKMANAGERS_BLOCKED)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final int numTaskManagersBlocked;

    @JsonProperty(FIELD_NAME_SLOTS_FREE_AND_BLOCKED)
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final int numSlotsFreeAndBlocked;

    @JsonCreator
    // numTaskManagersBlocked and numSlotsFreeAndBlocked is Nullable since Jackson will assign null
    // if the field is absent while parsing
    public ClusterOverview(
            @JsonProperty(FIELD_NAME_TASKMANAGERS) int numTaskManagersConnected,
            @JsonProperty(FIELD_NAME_SLOTS_TOTAL) int numSlotsTotal,
            @JsonProperty(FIELD_NAME_SLOTS_AVAILABLE) int numSlotsAvailable,
            @JsonProperty(FIELD_NAME_TASKMANAGERS_BLOCKED) @Nullable Integer numTaskManagersBlocked,
            @JsonProperty(FIELD_NAME_SLOTS_FREE_AND_BLOCKED) @Nullable Integer numSlotsFreeAndBlocked,
            @JsonProperty(FIELD_NAME_JOBS_RUNNING) int numJobsRunningOrPending,
            @JsonProperty(FIELD_NAME_JOBS_FINISHED) int numJobsFinished,
            @JsonProperty(FIELD_NAME_JOBS_CANCELLED) int numJobsCancelled,
            @JsonProperty(FIELD_NAME_JOBS_FAILED) int numJobsFailed) {

        super(numJobsRunningOrPending, numJobsFinished, numJobsCancelled, numJobsFailed);

        this.numTaskManagersConnected = numTaskManagersConnected;
        this.numSlotsTotal = numSlotsTotal;
        this.numSlotsAvailable = numSlotsAvailable;
        this.numTaskManagersBlocked = numTaskManagersBlocked == null ? 0 : numTaskManagersBlocked;
        this.numSlotsFreeAndBlocked = numSlotsFreeAndBlocked == null ? 0 : numSlotsFreeAndBlocked;
    }
}
