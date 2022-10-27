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

package cn.sliew.scaleph.plugin.framework.property;

import cn.sliew.milky.common.primitives.*;
import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;

public enum Parsers {
    ;

    public static final Parser<Boolean> BOOLEAN_PARSER = value -> Booleans.parseBoolean(value);

    public static final Parser<Integer> INTEGER_PARSER = value -> Integers.parseInteger(value);

    public static final Parser<Long> LONG_PARSER = value -> Longs.parseLong(value);

    public static final Parser<Float> FLOAT_PARSER = value -> Floats.parseFloat(value);

    public static final Parser<Double> DOUBLE_PARSER = value -> Doubles.parseDouble(value);

    public static final Parser<String> STRING_PARSER = value -> value;

    public static final Parser<List<String>> STRING_ARRAY_PARSER = value -> Arrays.asList(value.split(","));

    public static final Parser<JsonNode> JSON_PARSER = value -> JacksonUtil.toJsonNode(value);
}
