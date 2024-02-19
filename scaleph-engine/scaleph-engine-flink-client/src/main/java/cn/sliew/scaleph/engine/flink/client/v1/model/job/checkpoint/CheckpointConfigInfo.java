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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

@Data
public class CheckpointConfigInfo {

    @JsonProperty("mode")
    private ProcessingMode processingMode;
    @JsonProperty("interval")
    private long checkpointInterval;
    @JsonProperty("timeout")
    private long checkpointTimeout;
    @JsonProperty("min_pause")
    private long minPauseBetweenCheckpoints;
    @JsonProperty("max_concurrent")
    private long maxConcurrentCheckpoints;
    @JsonProperty("externalization")
    private ExternalizedCheckpointInfo externalizedCheckpointInfo;
    @JsonProperty("state_backend")
    private String stateBackend;
    @JsonProperty("checkpoint_storage")
    private String checkpointStorage;
    @JsonProperty("unaligned_checkpoints")
    private boolean unalignedCheckpoints;
    @JsonProperty("tolerable_failed_checkpoints")
    private int tolerableFailedCheckpoints;
    @JsonProperty("aligned_checkpoint_timeout")
    private long alignedCheckpointTimeout;
    @JsonProperty("checkpoints_after_tasks_finish")
    private boolean checkpointsWithFinishedTasks;
    @JsonProperty("state_changelog_enabled")
    private boolean stateChangelog;
    @JsonProperty("changelog_periodic_materialization_interval")
    private long periodicMaterializationInterval;
    @JsonProperty("changelog_storage")
    private String changelogStorage;

    @Data
    public static final class ExternalizedCheckpointInfo {

        @JsonProperty("enabled")
        private boolean enabled;
        @JsonProperty("delete_on_cancellation")
        private boolean deleteOnCancellation;
    }

    @JsonSerialize(using = ProcessingModeSerializer.class)
    @JsonDeserialize(using = ProcessingModeDeserializer.class)
    public enum ProcessingMode {
        AT_LEAST_ONCE, EXACTLY_ONCE;
    }

    public static class ProcessingModeDeserializer extends StdDeserializer<ProcessingMode> {
        public ProcessingModeDeserializer() {
            super(ProcessingMode.class);
        }

        public ProcessingMode deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return CheckpointConfigInfo.ProcessingMode.valueOf(jsonParser.getValueAsString().toUpperCase());
        }
    }

    public static class ProcessingModeSerializer extends StdSerializer<ProcessingMode> {
        public ProcessingModeSerializer() {
            super(ProcessingMode.class);
        }

        public void serialize(ProcessingMode mode, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
            generator.writeString(mode.name().toLowerCase());
        }
    }
}
