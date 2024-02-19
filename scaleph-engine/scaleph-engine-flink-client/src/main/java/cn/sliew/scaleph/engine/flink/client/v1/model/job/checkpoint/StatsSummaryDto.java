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

package cn.sliew.scaleph.engine.flink.client.v1.model.job.checkpoint;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public final class StatsSummaryDto {

    @JsonProperty("min")
    private long minimum;
    @JsonProperty("max")
    private long maximum;
    @JsonProperty("avg")
    private long average;
    @JsonProperty("p50")
    private double p50;
    @JsonProperty("p90")
    private double p90;
    @JsonProperty("p95")
    private double p95;
    @JsonProperty("p99")
    private double p99;
    @JsonProperty("p999")
    private double p999;
}
