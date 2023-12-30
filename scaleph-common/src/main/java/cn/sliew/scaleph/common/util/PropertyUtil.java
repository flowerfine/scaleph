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

package cn.sliew.scaleph.common.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public enum PropertyUtil {
    ;

    public static Map<String, String> formatPropFromStr(String str) {
        return formatPropFromStr(str, "\n", "=");
    }

    public static String mapToFormatProp(Map<String, String> map) {
        return mapToFormatProp(map, "\n", "=");
    }

    public static Map<String, String> formatPropFromStr(String str, String lineSeparator, String kvSeparator) {
        if (StringUtils.hasText(str)) {
            return Splitter.on(lineSeparator).withKeyValueSeparator(kvSeparator).split(str);
        }
        return Collections.emptyMap();
    }

    public static String mapToFormatProp(Map<String, String> map, String lineSeparator, String kvSeparator) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        return Joiner.on(lineSeparator).withKeyValueSeparator(kvSeparator).join(map);
    }

    public static Properties mapToProperties(Map<String, Object> map) {
        Properties props = new Properties();
        if (!CollectionUtils.isEmpty(map)) {
            props.putAll(map);
        }
        return props;
    }
}
