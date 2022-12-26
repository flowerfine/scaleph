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

package cn.sliew.scaleph.common.param;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public enum PropertyUtil {
    ;

    public static Map<String, Object> formatPropFromStr(String str, String lineSeparator, String kvSeparator) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.hasText(str)) {
            String[] arr = str.split(lineSeparator);
            for (int i = 0; i < arr.length; i++) {
                String[] kv = arr[i].split(kvSeparator);
                if (kv.length == 2) {
                    map.put(kv[0], kv[1]);
                }
            }
        }
        return map;
    }

    public static String mapToFormatProp(Map<String, Object> map, String lineSeparator, String kvSeparator) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        map.forEach((k, v) -> {
            buffer.append(k).append(kvSeparator).append(v).append(lineSeparator);
        });
        return buffer.toString();
    }

    public static Properties mapToProperties(Map<String, Object> map) {
        Properties props = new Properties();
        if (!CollectionUtils.isEmpty(map)) {
            props.putAll(map);
        }
        return props;
    }
}
