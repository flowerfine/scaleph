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

import cn.sliew.scaleph.common.codec.CodecUtil;

public enum SensitiveType {

    /**
     * 脱敏
     */
    DESENSITIZATION {
        @Override
        public String apply(String value, String strategry, String param) {
            return DesensitizationType.of(strategry).getDesensitizer().apply(value);
        }
    },

    /**
     * 加密
     */
    ENCRYPTION {
        @Override
        public String apply(String value, String strategry, String param) {
            return CodecUtil.encodeToBase64(value);
        }
    },

    /**
     * 过滤
     */
    FILTER {
        @Override
        public String apply(String value, String strategry, String param) {
            return null;
        }
    },

    /**
     * 正则表达式
     */
    PATTERN {
        @Override
        public String apply(String value, String strategry, String param) {
            return value.replaceAll(strategry, param);
        }
    }
    ;

    public abstract String apply(String value, String strategry, String param);
}
