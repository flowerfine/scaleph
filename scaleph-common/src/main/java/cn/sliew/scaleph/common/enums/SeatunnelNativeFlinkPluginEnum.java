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

package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum SeatunnelNativeFlinkPluginEnum {

    CONSOLE_SINK("ConsoleSink", "ConsoleSink"),
    SOCKET_SOURCE("SocketStream", "SocketStream"),
    JDBC_SINK("JdbcSink", "JdbcSink"),
    JDBC_SOURCE("JdbcSource", "JdbcSource"),
    KAFKA_SINK("Kafka", "Kafka"),
    KAFKA_SOURCE("KafkaTableStream", "KafkaTableStream"),
    INFLUXDB_SINK("InfluxDbSink", "InfluxDbSink"),
    INFLUXDB_SOURCE("InfluxDbSource", "InfluxDbSource"),
    FILE_SOURCE("FileSource", "FileSource"),
    FILE_SINK("FileSink", "FileSink"),
    FAKE_SOURCE("FakeSource", "FakeSource"),
    ELASTICSEARCH_SINK("ElasticSearch", "ElasticSearch"),
    DRUID_SOURCE("DruidSource", "DruidSource"),
    DRUID_SINK("DruidSink", "DruidSink"),
    DORIS_SINK("DorisSink", "DorisSink"),
    CLICKHOUSE_SINK("Clickhouse", "Clickhouse"),
    CLICKHOUSE_FILE_SINK("ClickhouseFile", "ClickhouseFile");

    private String code;
    private String value;

    SeatunnelNativeFlinkPluginEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static SeatunnelNativeFlinkPluginEnum valueOfName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("seatunnel native flink plugin must not be null");
        }
        for (SeatunnelNativeFlinkPluginEnum seatunnelNativeFlinkPluginEnum : values()) {
            if (seatunnelNativeFlinkPluginEnum.getCode().equals(name)) {
                return seatunnelNativeFlinkPluginEnum;
            }
        }
        throw new IllegalArgumentException("unknown seatunnel native flink plugin for " + name);
    }

}
