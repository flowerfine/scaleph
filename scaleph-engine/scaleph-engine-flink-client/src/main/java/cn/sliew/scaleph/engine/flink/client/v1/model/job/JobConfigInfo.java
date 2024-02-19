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

package cn.sliew.scaleph.engine.flink.client.v1.model.job;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.Map;

@Data
public class JobConfigInfo {

    @JsonProperty("jid")
    private String jobId;
    @JsonProperty("name")
    private String jobName;
    @Nullable
    @JsonProperty("execution-config")
    private ExecutionConfigInfo executionConfigInfo;

    @Data
    public static final class ExecutionConfigInfo {

        @JsonProperty("execution-mode")
        private String executionMode;
        @JsonProperty("restart-strategy")
        private String restartStrategy;
        @JsonProperty("job-parallelism")
        private int parallelism;
        @JsonProperty("object-reuse-mode")
        private boolean isObjectReuse;
        @JsonProperty("user-config")
        private Map<String, String> globalJobParameters;
    }
}