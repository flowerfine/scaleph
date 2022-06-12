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

package cn.sliew.scaleph.engine.flink;

import lombok.Getter;

import static cn.sliew.milky.common.check.Ensures.notBlank;

@Getter
public enum FlinkRelease {

//    V_1_13_6(0, "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_13_6(0, "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://dlcdn.apache.org/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_14_4(1, "1.14.4", "flink-1.14.4-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.14.4/flink-1.14.4-bin-scala_2.11.tgz"),
    V_1_15_0(2, "1.15.0", "flink-1.15.0-bin-scala_2.12.tgz", "https://archive.apache.org/dist/flink/flink-1.15.0/flink-1.15.0-bin-scala_2.12.tgz"),
    ;

    private int code;
    private String version;
    private String name;
    private String url;

    FlinkRelease(int code, String version, String name, String url) {
        this.code = code;
        this.version = version;
        this.name = name;
        this.url = url;
    }

    public static FlinkRelease version(String version) {
        notBlank(version, () -> "flink version must not be blank!");
        for (FlinkRelease release : values()) {
            if (release.getVersion().equals(version)) {
                return release;
            }
        }
        throw new IllegalArgumentException("unknown flink release version for " + version);
    }
}
