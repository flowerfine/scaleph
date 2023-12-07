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
 *
 */

package cn.sliew.scaleph.engine.doris.sql.dialect;

import java.sql.Types;

public abstract class SqlDialect {

    public static final String LENGTH_FMT = "%s(%d)";
    public static final String LENGTH_SCALE_FMT = "%s(%d, %d)";

    public abstract boolean accept(String jdbcUrl);

    public String getDataTypeSummaryString(String typeName, int dataType, int length, int scale) {
        switch (dataType) {
            case Types.DECIMAL:
                return String.format(LENGTH_SCALE_FMT, typeName, length, scale);
            case Types.VARCHAR:
            case Types.VARBINARY:
            case Types.BIT:
            case Types.LONGNVARCHAR:
            case Types.LONGVARBINARY:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
                return String.format(LENGTH_FMT, typeName, length);
            default:
                return typeName;
        }
    }

}
