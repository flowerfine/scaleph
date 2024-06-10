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

package cn.sliew.scaleph.common.jackson;

import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import com.flipkart.zjsonpatch.JsonPatch;

import java.util.EnumSet;

public enum JsonMerger {
    ;

    public static <T> T merge(T template, T target, Class<T> clazz) {
        JsonNode templateNode = JacksonUtil.toJsonNode(template);
        JsonNode targetNode = JacksonUtil.toJsonNode(target);
        JsonNode merged = doMerge(templateNode, targetNode);
        return JacksonUtil.toObject(merged, clazz);
    }

    public static JsonNode doMerge(JsonNode source, JsonNode target) {
        if (source == null || source.isNull()) {
            return target;
        }
        if (target == null || target.isNull()) {
            return source;
        }
        EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy().clone();
        JsonNode patch = disableRemove((ArrayNode) JsonDiff.asJson(source, target, flags));
        return JsonPatch.apply(patch, source);
    }

    private static JsonNode disableRemove(ArrayNode patch) {
        if (patch.isEmpty()) {
            return patch;
        }

        ArrayNode arrayNode = JacksonUtil.createArrayNode();
        for (JsonNode op : patch) {
            ObjectNode objectNode = (ObjectNode) op;
            if (objectNode.path("op").asText().equals("remove") == false) {
                arrayNode.add(objectNode);
            }
        }

        return arrayNode;
    }
}
