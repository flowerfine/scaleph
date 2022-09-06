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

package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelPluginType;
import lombok.Getter;

import static cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelEngineType.SEATUNNEL;
import static cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelPluginName.*;
import static cn.sliew.scaleph.system.dict.seatunnel.SeaTunnelPluginType.*;

/**
 * https://github.com/apache/incubator-seatunnel/blob/dev/plugin-mapping.properties
 */
@Getter
public enum SeaTunnelPluginMapping {

    SOURCE_FAKE(SEATUNNEL, SOURCE, FAKESOURCE, "connector-fake"),
    SINK_ASSERT(SEATUNNEL, SINK, ASSERT, "connector-assert"),
    SOURCE_SOCKET(SEATUNNEL, SOURCE, SOCKET, "connector-socket"),
    SINK_CONSOLE(SEATUNNEL, SINK, CONSOLE, "connector-console"),
    SINK_EMAIL(SEATUNNEL, SINK, EMAIL, "connector-email"),
    SOURCE_HTTP(SEATUNNEL, SOURCE, HTTP, "connector-http-base"),
    SINK_HTTP(SEATUNNEL, SINK, HTTP, "connector-http-base"),
    SINK_FEISHU(SEATUNNEL, SINK, FEISHU, "connector-http-feishu"),
    SINK_DINGTALK(SEATUNNEL, SINK, DINGTALK, "connector-dingtalk"),

    SOURCE_LOCAL_FILE(SEATUNNEL, SOURCE, LOCAL_FILE, "connector-file-local"),
    SINK_LOCAL_FILE(SEATUNNEL, SINK, LOCAL_FILE, "connector-file-local"),
    SINK_FTP_FILE(SEATUNNEL, SINK, FTP_FILE, "connector-file-ftp"),
    SOURCE_HDFS_FILE(SEATUNNEL, SOURCE, HDFS_FILE, "connector-file-hadoop"),
    SINK_HDFS_FILE(SEATUNNEL, SINK, HDFS_FILE, "connector-file-hadoop"),
    SOURCE_OSS_FILE(SEATUNNEL, SOURCE, OSS_FILE, "connector-file-oss"),

    SOURCE_KAFKA(SEATUNNEL, SOURCE, KAFKA, "connector-kafka"),
    SINK_KAFKA(SEATUNNEL, SINK, KAFKA, "connector-kafka"),
    SOURCE_PULSAR(SEATUNNEL, SOURCE, PULSAR, "connector-pulsar"),
    SINK_DATAHUB(SEATUNNEL, SINK, DATAHUB, "connector-datahub"),

    SOURCE_JDBC(SEATUNNEL, SOURCE, JDBC, "connector-jdbc"),
    SINK_JDBC(SEATUNNEL, SINK, JDBC, "connector-jdbc"),
    SOURCE_REDIS(SEATUNNEL, SOURCE, REDIS, "connector-redis"),
    SINK_ELASTICSEARCH(SEATUNNEL, SINK, ELASTICSEARCH, "connector-elasticsearch"),

    SOURCE_HIVE(SEATUNNEL, SOURCE, HIVE, "connector-hive"),
    SINK_HIVE(SEATUNNEL, SINK, HIVE, "connector-hive"),
    SOURCE_CLICKHOUSE(SEATUNNEL, SOURCE, CLICKHOUSE, "connector-clickhouse"),
    SINK_CLICKHOUSE(SEATUNNEL, SINK, CLICKHOUSE, "connector-clickhouse"),
    SINK_CLICKHOUSE_FILE(SEATUNNEL, SINK, CLICKHOUSE_FILE, "connector-clickhouse"),
    SOURCE_HUDI(SEATUNNEL, SOURCE, HUDI, "connector-hudi"),
    SOURCE_KUDU(SEATUNNEL, SOURCE, KUDU, "connector-kudu"),
    SINK_KUDU(SEATUNNEL, SINK, KUDU, "connector-kudu"),
    SOURCE_IOTDB(SEATUNNEL, SOURCE, IOTDB, "connector-iotdb"),
    SINK_IOTDB(SEATUNNEL, SINK, IOTDB, "connector-iotdb"),
    SINK_NEO4J(SEATUNNEL, SINK, NEO4J, "connector-neo4j"),
    ;

    private SeaTunnelEngineType engineType;
    private SeaTunnelPluginType pluginType;
    private SeaTunnelPluginName pluginName;
    private String pluginJarPrefix;

    SeaTunnelPluginMapping(SeaTunnelEngineType engineType, SeaTunnelPluginType pluginType, SeaTunnelPluginName pluginName, String pluginJarPrefix) {
        this.engineType = engineType;
        this.pluginType = pluginType;
        this.pluginName = pluginName;
        this.pluginJarPrefix = pluginJarPrefix;
    }
}
