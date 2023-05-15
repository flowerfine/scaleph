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

package cn.sliew.scaleph;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import javax.sql.DataSource;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DbTool {
    public static void main(String[] args) throws Exception {
        Map<String, String> options = parseArgs(args);
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                .driverClassName(options.get("driver"))
                .url(options.get("url"));
        if (options.containsKey("username")) {
            dataSourceBuilder.username(options.get("username"));
        }
        if (options.containsKey("password")) {
            dataSourceBuilder.password(options.get("password"));
        }
        DataSource dataSource = dataSourceBuilder.build();
        Resource[] sqls = new Resource[0];
        if (options.containsKey("sql-files")) {
            String sqlFilesDir = options.get("sql-files");
            Path path = Paths.get(sqlFilesDir);
            if (Files.exists(path) && Files.isDirectory(path)) {
                sqls = Files.list(path).filter(f -> f.toString().endsWith(".sql"))
                        .map(PathResource::new)
                        .toArray(PathResource[]::new);
            }
        } else {
            System.err.println("No sql files directory specified!");
            return;
        }
        if (sqls.length > 0) {
            try (Connection connection = dataSource.getConnection()) {
                for (Resource sql : sqls) {
                    System.out.println("Executing sql: " + sql.getFilename());
                    EncodedResource encodedResource = new EncodedResource(sql, StandardCharsets.UTF_8);
                    try {
                        ScriptRunner scriptRunner = new ScriptRunner(connection);
                        try (Reader reader = encodedResource.getReader()) {
                            scriptRunner.runScript(reader);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static Map<String, String> parseArgs(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--")) {
                arg = arg.substring(2);
                int j = i + 1;
                if (j >= args.length) {
                    map.put(arg, "true");
                } else {
                    String nextArg = args[j];
                    if (nextArg.startsWith("--")) {
                        map.put(arg, "true");
                    } else {
                        map.put(arg, nextArg);
                    }
                    i++;
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

}
