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

package cn.sliew.scaleph.common.jackson.sensitive;

import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;

@Getter
public enum DesensitizationType {

    USERNAME("Username", s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    ID_CARD("IdCard", s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),

    PHONE("Phone", s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    ADDRESS("Address", s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****")),
    ;

    public static DesensitizationType of(String name) {
        return Arrays.stream(values())
                .filter(instance -> instance.getName().equals(name))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(DesensitizationType.class, name));
    }

    private final String name;
    private final Function<String, String> desensitizer;

    DesensitizationType(String name, Function<String, String> desensitizer) {
        this.name = name;
        this.desensitizer = desensitizer;
    }


}
