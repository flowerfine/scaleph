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

package cn.sliew.scaleph.config.flink;

import java.util.Arrays;

public enum FlinkVersionConfig {
    ;

    public static final FlinkVersionProperties FLINK_JAR_1_16 =
            new FlinkVersionProperties(
                    FlinkImageType.JAR,
                    "1.16",
                    Arrays.asList("1.16.0", "1.16.1", "1.16.2"),
                    "flink:1.16");

    public static final FlinkVersionProperties FLINK_JAR_1_17 =
            new FlinkVersionProperties(
                    FlinkImageType.JAR,
                    "1.17",
                    Arrays.asList("1.17.0", "1.17.1"),
                    "flink:1.17");

    public static final FlinkVersionProperties FLINK_SQL_1_16 =
            new FlinkVersionProperties(
                    FlinkImageType.SQL,
                    "1.16",
                    Arrays.asList("1.16.0", "1.16.1", "1.16.2"),
                    "ghcr.io/flowerfine/scaleph/scaleph-sql-templates:1.16");

    public static final FlinkVersionProperties FLINK_SQL_1_17 =
            new FlinkVersionProperties(
                    FlinkImageType.SQL,
                    "1.17",
                    Arrays.asList("1.17.0", "1.17.1"),
                    "ghcr.io/flowerfine/scaleph/scaleph-sql-templates:1.17");

    public static final FlinkVersionProperties FLINK_SEATUNNEL_1_16 =
            new FlinkVersionProperties(
                    FlinkImageType.SEATUNNEL,
                    "1.16",
                    Arrays.asList("1.16.0", "1.16.1", "1.16.2"),
                    "ghcr.io/flowerfine/scaleph/scaleph-seatunnel_2.3.1:1.16");

    public static final FlinkVersionProperties FLINK_SEATUNNEL_1_17 =
            new FlinkVersionProperties(
                    FlinkImageType.SEATUNNEL,
                    "1.17",
                    Arrays.asList("1.17.0", "1.17.1"),
                    "ghcr.io/flowerfine/scaleph/scaleph-seatunnel_2.3.1:1.17");

    public static String findImage(FlinkImageType type, String flinkVersion) {
        switch (type) {
            case JAR:
                return doFindImage(flinkVersion, FLINK_JAR_1_17.getImage(), FLINK_JAR_1_16, FLINK_JAR_1_17);
            case SQL:
                return doFindImage(flinkVersion, FLINK_SQL_1_17.getImage(), FLINK_SQL_1_16, FLINK_SQL_1_17);
            case SEATUNNEL:
                return doFindImage(flinkVersion, FLINK_SEATUNNEL_1_17.getImage(), FLINK_SEATUNNEL_1_16, FLINK_SEATUNNEL_1_17);
            default:
                return null;
        }
    }

    private static String doFindImage(String flinkVersion, String defaultImage, FlinkVersionProperties... allProperties) {
        return Arrays.stream(allProperties)
                .filter(properties -> flinkVersion.startsWith(properties.getMajorFlinkVersion()))
                .map(FlinkVersionProperties::getImage)
                .findFirst().orElse(defaultImage);
    }
}
