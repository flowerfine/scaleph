/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
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

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.TableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This file was copied from https://github.com/apache/flink-kubernetes-operator
 * under Apache License, Version 2.0 http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Main class for executing SQL scripts.
 */
public class SqlRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRunner.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            throw new Exception("Exactly one argument is expected.");
        }
        var statements = SqlFormatter.parseStatements(args[0]);

        var tableEnv = TableEnvironment.create(new Configuration());

        for (String statement : statements) {
            LOG.info("Executing:\n{}", statement);
            tableEnv.executeSql(statement);
        }
    }
}
