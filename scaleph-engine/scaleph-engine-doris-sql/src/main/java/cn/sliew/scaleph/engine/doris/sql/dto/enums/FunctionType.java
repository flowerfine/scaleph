/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.doris.sql.dto.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum FunctionType {
    FUNCTION_RESULT_UNKNOWN((short) 0),
    FUNCTION_NO_TABLE((short) 1),
    FUNCTION_RETURNS_TABLE((short) 2);

    private static final Map<Short, FunctionType> FUNCTION_TYPE_MAP =
            Arrays.stream(FunctionType.values())
                    .collect(Collectors.toMap(FunctionType::getCode, f -> f));
    private final short code;

    FunctionType(short code) {
        this.code = code;
    }

    public static FunctionType fromFunctionType(short typeCode) {
        if (FUNCTION_TYPE_MAP.containsKey(typeCode)) {
            return FUNCTION_TYPE_MAP.get(typeCode);
        } else {
            throw new IllegalArgumentException("No such function type: " + typeCode);
        }
    }

}
