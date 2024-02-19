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
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;

import java.io.IOException;

@Data
public class JobPlanInfo {

    @JsonProperty("plan")
    private RawJson jsonPlan;

    @Data
    @JsonSerialize(using = RawJson.Serializer.class)
    @JsonDeserialize(using = RawJson.Deserializer.class)
    public static final class RawJson {

        private String json;

        public static final class Deserializer extends StdDeserializer<RawJson> {
            private static final long serialVersionUID = -3580088509877177213L;

            public Deserializer() {
                super(RawJson.class);
            }

            public RawJson deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                JsonNode rootNode = jsonParser.readValueAsTree();
                RawJson rawJson = new RawJson();
                rawJson.setJson(rootNode.toString());
                return rawJson;
            }
        }

        public static final class Serializer extends StdSerializer<RawJson> {
            private static final long serialVersionUID = -1551666039618928811L;

            public Serializer() {
                super(RawJson.class);
            }

            public void serialize(RawJson jobPlanInfo, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeRawValue(jobPlanInfo.json);
            }
        }
    }
}
