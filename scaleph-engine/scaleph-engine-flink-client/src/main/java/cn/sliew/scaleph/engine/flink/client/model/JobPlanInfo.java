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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.base.Preconditions;

import java.io.IOException;

/**
 * Response type of the {@link JobPlanHandler}.
 */
public class JobPlanInfo {

    private static final String FIELD_NAME_PLAN = "plan";

    @JsonProperty(FIELD_NAME_PLAN)
    private final RawJson jsonPlan;

    public JobPlanInfo(String jsonPlan) {
        this(new RawJson(Preconditions.checkNotNull(jsonPlan)));
    }

    @JsonCreator
    public JobPlanInfo(@JsonProperty(FIELD_NAME_PLAN) RawJson jsonPlan) {
        this.jsonPlan = jsonPlan;
    }

    @JsonIgnore
    public String getJsonPlan() {
        return jsonPlan.json;
    }

    /**
     * Simple wrapper around a raw JSON string.
     */
    @JsonSerialize(using = RawJson.Serializer.class)
    @JsonDeserialize(using = RawJson.Deserializer.class)
    public static final class RawJson {
        private final String json;

        public RawJson(String json) {
            this.json = json;
        }

        // ---------------------------------------------------------------------------------
        // Static helper classes
        // ---------------------------------------------------------------------------------

        /**
         * Json serializer for the {@link RawJson}.
         */
        public static final class Serializer extends StdSerializer<RawJson> {

            private static final long serialVersionUID = -1551666039618928811L;

            public Serializer() {
                super(RawJson.class);
            }

            @Override
            public void serialize(
                    RawJson jobPlanInfo,
                    JsonGenerator jsonGenerator,
                    SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeRawValue(jobPlanInfo.json);
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // this ensures the type is documented as "object" in the documentation
                visitor.expectObjectFormat(typeHint);
            }
        }

        /**
         * Json deserializer for the {@link RawJson}.
         */
        public static final class Deserializer extends StdDeserializer<RawJson> {

            private static final long serialVersionUID = -3580088509877177213L;

            public Deserializer() {
                super(RawJson.class);
            }

            @Override
            public RawJson deserialize(
                    JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                final JsonNode rootNode = jsonParser.readValueAsTree();
                return new RawJson(rootNode.toString());
            }
        }
    }
}
