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
public enum SeatunnelRelease {

    V_2_1_0(0, "2.1.0", "apache-seatunnel-incubating-2.1.0-bin.tar.gz","org.apache.seatunnel.SeatunnelFlink", "https://archive.apache.org/dist/incubator/seatunnel/2.1.0/apache-seatunnel-incubating-2.1.0-bin.tar.gz"),
    V_2_1_1(1, "2.1.1", "apache-seatunnel-incubating-2.1.1-bin.tar.gz","org.apache.seatunnel.SeatunnelFlink", "https://archive.apache.org/dist/incubator/seatunnel/2.1.1/apache-seatunnel-incubating-2.1.1-bin.tar.gz"),
    V_2_1_2(2, "2.1.2", "apache-seatunnel-incubating-2.1.2-bin.tar.gz","org.apache.seatunnel.SeatunnelFlink", "https://archive.apache.org/dist/incubator/seatunnel/2.1.2/apache-seatunnel-incubating-2.1.2-bin.tar.gz"),

    DEV(Integer.MAX_VALUE, "dev", "unsupported","org.apache.seatunnel.core.flink.SeatunnelFlink", "unsupported"),
    ;

    private int code;
    private String version;
    private String releaseName;
    private String entryClass;
    private String releaseUrl;

    SeatunnelRelease(int code, String version, String releaseName, String entryClass, String releaseUrl) {
        this.code = code;
        this.version = version;
        this.releaseName = releaseName;
        this.entryClass = entryClass;
        this.releaseUrl = releaseUrl;
    }
}
