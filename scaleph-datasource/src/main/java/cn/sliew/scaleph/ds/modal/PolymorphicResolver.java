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

package cn.sliew.scaleph.ds.modal;

import cn.sliew.scaleph.common.dict.job.DataSourceType;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PolymorphicResolver extends TypeIdResolverBase {
    private final BiMap<DataSourceType, Class<?>> subTypes = HashBiMap.create();
    private JavaType superType;
    private Class<?> defaultClass;

    protected void bind(DataSourceType type, Class<?> subClass) {
        this.subTypes.put(type, subClass);
    }

    protected void bindDefault(Class<?> defaultClass) {
        this.defaultClass = defaultClass;
    }

    public void init(JavaType baseType) {
        this.superType = baseType;
    }

    public Id getMechanism() {
        return Id.NAME;
    }

    public String idFromValue(Object obj) {
        return this.kindFromSubtype(obj);
    }

    public String idFromValueAndType(Object obj, Class<?> subType) {
        return this.kindFromSubtype(obj);
    }

    public JavaType typeFromId(DatabindContext context, String id) {
        Class<?> subType = this.subTypeFromKind(id);
        return context.constructSpecializedType(this.superType, subType);
    }

    private String kindFromSubtype(Object obj) {
        return subTypes.inverse().get(obj.getClass()).getValue();
    }

    private Class<?> subTypeFromKind(String id) {
        Class<?> subType = subTypes.get(DataSourceType.of(id));
        return subType != null ? subType : defaultClass;
    }
}
