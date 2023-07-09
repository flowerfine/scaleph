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

package cn.sliew.scaleph.engine.sql;

import org.junit.jupiter.api.Test;

import java.util.List;

public class SqlFormatterTest {

    @Test
    void testSqlFormat() {
        String script = "CREATE TEMPORARY TABLE source_table (\n" +
                "  `id` bigint,\n" +
                "  `name` string,\n" +
                "  `age` int,\n" +
                "  `address` string,\n" +
                "  `create_time`TIMESTAMP(3),\n" +
                "  `update_time`TIMESTAMP(3),\n" +
                "  WATERMARK FOR `update_time` AS update_time - INTERVAL '1' MINUTE\n" +
                ")\n" +
                "COMMENT ''\n" +
                "WITH (\n" +
                "  'connector' = 'datagen',\n" +
                "  'number-of-rows' = '100'\n" +
                ");\n" +
                "\n" +
                "CREATE TEMPORARY TABLE `sink_table` (\n" +
                "  `id` BIGINT,\n" +
                "  `name` VARCHAR(2147483647),\n" +
                "  `age` INT,\n" +
                "  `address` VARCHAR(2147483647),\n" +
                "  `create_time` TIMESTAMP(3),\n" +
                "  `update_time` TIMESTAMP(3)\n" +
                ")\n" +
                "COMMENT ''\n" +
                "WITH (\n" +
                "  'connector' = 'print'\n" +
                ");\n" +
                "\n" +
                "insert into sink_table\n" +
                "select id, name, age, address, create_time, update_time from source_table;";

        List<String> statements = SqlFormatter.parseStatements(script);
        for (String statement : statements) {
            System.out.println("Executing:\n" + statement);
        }
    }
}
