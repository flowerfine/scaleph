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

package cn.sliew.scaleph.engine.seatunnel;

import lombok.Getter;

/**
 * todo submit seatunnel job behaves depends on seatunnel version automatically.
 */
@Getter
public enum FlinkRelease {

//    V_1_13_6(0, "1.13", "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_13_6(0, "1.13", "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://dlcdn.apache.org/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_14_4(1, "1.14", "1.14.4", "flink-1.14.4-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.14.4/flink-1.14.4-bin-scala_2.11.tgz"),
    V_1_15_0(2, "1.15", "1.15.0", "flink-1.15.0-bin-scala_2.12.tgz", "https://archive.apache.org/dist/flink/flink-1.15.0/flink-1.15.0-bin-scala_2.12.tgz"),
    ;

    private int code;
    private String majorVersion;
    private String fullVersion;
    private String releaseName;
    private String releaseUrl;

    FlinkRelease(int code, String majorVersion, String fullVersion, String releaseName, String releaseUrl) {
        this.code = code;
        this.majorVersion = majorVersion;
        this.fullVersion = fullVersion;
        this.releaseName = releaseName;
        this.releaseUrl = releaseUrl;
    }
}
