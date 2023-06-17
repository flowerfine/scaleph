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

package cn.sliew.scaleph.engine.flink.client.v1.model.job.metrics;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public final class IOMetricsInfo {

    @JsonProperty("read-bytes")
    private long bytesRead;
    @JsonProperty("read-bytes-complete")
    private boolean bytesReadComplete;
    @JsonProperty("write-bytes")
    private long bytesWritten;
    @JsonProperty("write-bytes-complete")
    private boolean bytesWrittenComplete;
    @JsonProperty("read-records")
    private long recordsRead;
    @JsonProperty("read-records-complete")
    private boolean recordsReadComplete;
    @JsonProperty("write-records")
    private long recordsWritten;
    @JsonProperty("write-records-complete")
    private boolean recordsWrittenComplete;
    @JsonProperty("accumulated-backpressured-time")
    private long accumulatedBackpressured;
    @JsonProperty("accumulated-idle-time")
    private long accumulatedIdle;
    @JsonProperty("accumulated-busy-time")
    private double accumulatedBusy;
}
