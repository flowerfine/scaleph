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

package cn.sliew.scaleph.plugin.datasource.util;

import cn.sliew.scaleph.common.dict.job.DataSourceType;

import java.util.Map;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public enum JdbcUtil {
    ;

    public static String getDriverClassName(DataSourceType dataSourceType) {
        switch (dataSourceType) {
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            default:
                throw new UnsupportedOperationException("unsupport dataSource type for " + dataSourceType);
        }
    }

    public static String formatUrl(DataSourceType dataSourceType, Map<String, Object> properties, Map<String, Object> additionalProperties) {
        switch (dataSourceType) {
            case MYSQL:
                return formatMySQLUrl(properties.get(HOST.getName()).toString(), Integer.parseInt(properties.get(PORT.getName()).toString()), properties.get(DATABASE_NAME.getName()).toString(), formatAdditionalProps(additionalProperties));
            default:
                throw new UnsupportedOperationException("unsupport dataSource type for " + dataSourceType);
        }
    }

    public static String formatMySQLUrl(String host, int port, String database, String additionalProperties) {
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?" + additionalProperties;
    }

    public static String formatAdditionalProps(Map additionalProperties) {
        if (additionalProperties == null || additionalProperties.isEmpty()) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        additionalProperties.forEach((key, value) -> buffer.append(key).append("=").append(value).append("&"));
        return buffer.substring(0, buffer.length() - 1);
    }
}
