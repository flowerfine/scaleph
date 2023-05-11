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

package cn.sliew.scaleph.engine.flink.kubernetes.operator.util;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.AbstractFlinkResource;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.AbstractFlinkSpec;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.annotation.Nullable;

/**
 * Spec utilities.
 */
public class SpecUtils {

    public static final String INTERNAL_METADATA_JSON_KEY = "resource_metadata";

    /**
     * Deserializes the spec and custom metadata object from JSON.
     *
     * @param specWithMetaString JSON string.
     * @param specClass          Spec class for deserialization.
     * @param <T>                Spec type.
     * @return SpecWithMeta of spec and meta.
     */
    public static <T extends AbstractFlinkSpec> SpecWithMeta<T> deserializeSpecWithMeta(@Nullable String specWithMetaString, Class<T> specClass) {
        if (specWithMetaString == null) {
            return null;
        }

        ObjectNode wrapper = (ObjectNode) JacksonUtil.toJsonNode(specWithMetaString);
        ObjectNode internalMeta = (ObjectNode) wrapper.remove(INTERNAL_METADATA_JSON_KEY);
        if (internalMeta == null) {
            // migrating from old format
            wrapper.remove("apiVersion");
            return new SpecWithMeta<>(JacksonUtil.toObject(wrapper, specClass), null);
        } else {
            return new SpecWithMeta<>(JacksonUtil.toObject(wrapper.get("spec"), specClass), JacksonUtil.toObject(internalMeta, ReconciliationMetadata.class));
        }
    }

    /**
     * Serializes the spec and custom meta information into a JSON string.
     *
     * @param spec            Flink resource spec.
     * @param relatedResource Related Flink resource for creating the meta object.
     * @return Serialized json.
     */
    public static String writeSpecWithMeta(AbstractFlinkSpec spec, AbstractFlinkResource<?, ?> relatedResource) {
        return writeSpecWithMeta(spec, ReconciliationMetadata.from(relatedResource));
    }

    /**
     * Serializes the spec and custom meta information into a JSON string.
     *
     * @param spec     Flink resource spec.
     * @param metadata Reconciliation meta object.
     * @return Serialized json.
     */
    public static String writeSpecWithMeta(AbstractFlinkSpec spec, ReconciliationMetadata metadata) {
        ObjectNode wrapper = JacksonUtil.createObjectNode();
        wrapper.set("spec", JacksonUtil.toJsonNode(checkNotNull(spec)));
        wrapper.set(INTERNAL_METADATA_JSON_KEY, JacksonUtil.toJsonNode(checkNotNull(metadata)));
        return wrapper.toString();
    }

    // We do not have access to  Flink's Preconditions from here
    private static <T> T checkNotNull(T object) {
        if (object == null) {
            throw new NullPointerException();
        } else {
            return object;
        }
    }

    public static <T> T clone(T object) {
        if (object == null) {
            return null;
        }
        return (T) JacksonUtil.parseJsonString(JacksonUtil.toJsonString(object), object.getClass());
    }
}
