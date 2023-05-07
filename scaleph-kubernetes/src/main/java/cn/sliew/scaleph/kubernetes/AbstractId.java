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

package cn.sliew.scaleph.kubernetes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;

@JsonSerialize(using = AbstractId.AbstractIdJsonSerializer.class)
public abstract class AbstractId {

    @Getter
    private final UUID uuid;

    public AbstractId() {
        this.uuid = UUID.randomUUID();
    }

    public AbstractId(UUID uuid) {
        this.uuid = checkNotNull(uuid);
    }

    public AbstractId(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    static class AbstractIdJsonSerializer extends StdSerializer<AbstractId> {

        private static final long serialVersionUID = -6975665638538027925L;

        public AbstractIdJsonSerializer() {
            super(AbstractId.class);
        }

        public void serialize(AbstractId id, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(id.getUuid().toString());
        }
    }
}