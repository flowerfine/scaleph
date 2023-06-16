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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Getter;

import javax.annotation.Nullable;

/**
 * Cluster overview message including the current Flink version and commit id.
 */
@Getter
public class ClusterOverviewWithVersion extends ClusterOverview {

    private static final long serialVersionUID = 5000058311783413216L;

    public static final String FIELD_NAME_VERSION = "flink-version";
    public static final String FIELD_NAME_COMMIT = "flink-commit";

    @JsonProperty(FIELD_NAME_VERSION)
    private final String version;

    @JsonProperty(FIELD_NAME_COMMIT)
    private final String commitId;

    @JsonCreator
    // numTaskManagersBlocked and numSlotsFreeAndBlocked is Nullable since Jackson will assign null
    // if the field is absent while parsing
    public ClusterOverviewWithVersion(
            @JsonProperty(FIELD_NAME_TASKMANAGERS) int numTaskManagersConnected,
            @JsonProperty(FIELD_NAME_SLOTS_TOTAL) int numSlotsTotal,
            @JsonProperty(FIELD_NAME_SLOTS_AVAILABLE) int numSlotsAvailable,
            @JsonProperty(FIELD_NAME_TASKMANAGERS_BLOCKED) @Nullable Integer numTaskManagersBlocked,
            @JsonProperty(FIELD_NAME_SLOTS_FREE_AND_BLOCKED) @Nullable Integer numSlotsFreeAndBlocked,
            @JsonProperty(FIELD_NAME_JOBS_RUNNING) int numJobsRunningOrPending,
            @JsonProperty(FIELD_NAME_JOBS_FINISHED) int numJobsFinished,
            @JsonProperty(FIELD_NAME_JOBS_CANCELLED) int numJobsCancelled,
            @JsonProperty(FIELD_NAME_JOBS_FAILED) int numJobsFailed,
            @JsonProperty(FIELD_NAME_VERSION) String version,
            @JsonProperty(FIELD_NAME_COMMIT) String commitId) {
        super(
                numTaskManagersConnected,
                numSlotsTotal,
                numSlotsAvailable,
                numTaskManagersBlocked,
                numSlotsFreeAndBlocked,
                numJobsRunningOrPending,
                numJobsFinished,
                numJobsCancelled,
                numJobsFailed);

        this.version = Preconditions.checkNotNull(version);
        this.commitId = Preconditions.checkNotNull(commitId);
    }
}
