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

package cn.sliew.scaleph.common.dict.seatunnel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Arrays;

import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType.SEATUNNEL;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName.*;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType.SINK;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType.SOURCE;

/**
 * https://github.com/apache/incubator-seatunnel/blob/dev/plugin-mapping.properties
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SeaTunnelPluginMapping {

    SOURCE_FAKE(SEATUNNEL, SOURCE, FAKESOURCE, "connector-fake"),
    SINK_ASSERT(SEATUNNEL, SINK, ASSERT, "connector-assert"),
    SOURCE_SOCKET(SEATUNNEL, SOURCE, SOCKET, "connector-socket"),
    SINK_SOCKET(SEATUNNEL, SINK, SOCKET, "connector-socket"),
    SINK_CONSOLE(SEATUNNEL, SINK, CONSOLE, "connector-console"),
    SINK_EMAIL(SEATUNNEL, SINK, EMAIL, "connector-email"),
    SINK_SLACK(SEATUNNEL, SINK, SLACK, "connector-slack"),
    SOURCE_HTTP(SEATUNNEL, SOURCE, HTTP, "connector-http-base"),
    SINK_HTTP(SEATUNNEL, SINK, HTTP, "connector-http-base"),
    SINK_FEISHU(SEATUNNEL, SINK, FEISHU, "connector-http-feishu"),
    SINK_WECHAT(SEATUNNEL, SINK, WECHAT, "connector-http-wechat"),
    SINK_DINGTALK(SEATUNNEL, SINK, DINGTALK, "connector-dingtalk"),
    SOURCE_MYHOURS(SEATUNNEL, SOURCE, MYHOURS, "connector-http-myhours"),
    SOURCE_LEMLIST(SEATUNNEL, SOURCE, LEMLIST, "connector-http-lemlist"),
    SOURCE_KLAVIYO(SEATUNNEL, SOURCE, KLAVIYO, "connector-http-klaviyo"),
    SOURCE_ONESIGNAL(SEATUNNEL, SOURCE, ONESIGNAL, "connector-http-onesignal"),
    SOURCE_JIRA(SEATUNNEL, SOURCE, JIRA, "connector-http-jira"),
    SOURCE_GITLAB(SEATUNNEL, SOURCE, GITLAB, "connector-http-gitlab"),
    SOURCE_NOTION(SEATUNNEL, SOURCE, NOTION, "connector-http-notion"),

    SOURCE_LOCAL_FILE(SEATUNNEL, SOURCE, LOCAL_FILE, "connector-file-local"),
    SINK_LOCAL_FILE(SEATUNNEL, SINK, LOCAL_FILE, "connector-file-local"),
    SOURCE_FTP_FILE(SEATUNNEL, SOURCE, FTP_FILE, "connector-file-ftp"),
    SINK_FTP_FILE(SEATUNNEL, SINK, FTP_FILE, "connector-file-ftp"),
    SOURCE_SFTP_FILE(SEATUNNEL, SOURCE, SFTP_FILE, "connector-file-sftp"),
    SINK_SFTP_FILE(SEATUNNEL, SINK, SFTP_FILE, "connector-file-sftp"),
    SOURCE_HDFS_FILE(SEATUNNEL, SOURCE, HDFS_FILE, "connector-file-hadoop"),
    SINK_HDFS_FILE(SEATUNNEL, SINK, HDFS_FILE, "connector-file-hadoop"),
    SOURCE_S3_FILE(SEATUNNEL, SOURCE, S3_FILE, "connector-file-s3"),
    SINK_S3_FILE(SEATUNNEL, SINK, S3_FILE, "connector-file-s3"),
    SOURCE_OSS_FILE(SEATUNNEL, SOURCE, OSS_FILE, "connector-file-oss"),
    SINK_OSS_FILE(SEATUNNEL, SINK, OSS_FILE, "connector-file-oss"),
    SOURCE_OSS_JINDO_FILE(SEATUNNEL, SOURCE, OSS_JINDO_FILE, "connector-file-oss-jindo"),
    SINK_OSS_JINDO_FILE(SEATUNNEL, SINK, OSS_JINDO_FILE, "connector-file-oss-jindo"),

    SOURCE_KAFKA(SEATUNNEL, SOURCE, KAFKA, "connector-kafka"),
    SINK_KAFKA(SEATUNNEL, SINK, KAFKA, "connector-kafka"),
    SOURCE_PULSAR(SEATUNNEL, SOURCE, PULSAR, "connector-pulsar"),
    SINK_DATAHUB(SEATUNNEL, SINK, DATAHUB, "connector-datahub"),
    SOURCE_RABBITMQ(SEATUNNEL, SOURCE, RABBITMQ, "connector-rabbitmq"),
    SINK_RABBITMQ(SEATUNNEL, SINK, RABBITMQ, "connector-rabbitmq"),

    SOURCE_JDBC(SEATUNNEL, SOURCE, JDBC, "connector-jdbc"),
    SINK_JDBC(SEATUNNEL, SINK, JDBC, "connector-jdbc"),
    SOURCE_REDIS(SEATUNNEL, SOURCE, REDIS, "connector-redis"),
    SINK_REDIS(SEATUNNEL, SINK, REDIS, "connector-redis"),
    SINK_ELASTICSEARCH(SEATUNNEL, SINK, ELASTICSEARCH, "connector-elasticsearch"),
    SOURCE_MONGODB(SEATUNNEL, SOURCE, MONGODB, "connector-mongodb"),
    SINK_MONGODB(SEATUNNEL, SINK, MONGODB, "connector-mongodb"),
    SOURCE_AMAZON_DYNAMODB(SEATUNNEL, SOURCE, AMAZON_DYNAMODB, "connector-amazondynamodb"),
    SINK_AMAZON_DYNAMODB(SEATUNNEL, SINK, AMAZON_DYNAMODB, "connector-amazondynamodb"),
    SOURCE_CASSANDRA(SEATUNNEL, SOURCE, CASSANDRA, "connector-cassandra"),
    SINK_CASSANDRA(SEATUNNEL, SINK, CASSANDRA, "connector-cassandra"),
    SINK_TABLESTORE(SEATUNNEL, SINK, TABLESTORE, "connector-tablestore"),

    SOURCE_MYSQL_CDC(SEATUNNEL, SOURCE, MYSQL_CDC, "connector-cdc-mysql"),

    SOURCE_HIVE(SEATUNNEL, SOURCE, HIVE, "connector-hive"),
    SINK_HIVE(SEATUNNEL, SINK, HIVE, "connector-hive"),
    SOURCE_CLICKHOUSE(SEATUNNEL, SOURCE, CLICKHOUSE, "connector-clickhouse"),
    SINK_CLICKHOUSE(SEATUNNEL, SINK, CLICKHOUSE, "connector-clickhouse"),
    SINK_CLICKHOUSE_FILE(SEATUNNEL, SINK, CLICKHOUSE_FILE, "connector-clickhouse"),
    SINK_DORIS(SEATUNNEL, SINK, DORIS, "connector-doris"),
    SINK_STARROCKS(SEATUNNEL, SINK, STARROCKS, "connector-starrocks"),
    SOURCE_HUDI(SEATUNNEL, SOURCE, HUDI, "connector-hudi"),
    SOURCE_ICEBERG(SEATUNNEL, SOURCE, ICEBERG, "connector-iceberg"),
    SINK_S3REDSHIFT(SEATUNNEL, SINK, S3REDSHIFT, "connector-s3-redshift"),
    SOURCE_MAXCOMPUTE(SEATUNNEL, SOURCE, MAXCOMPUTE, "connector-maxcompute"),
    SINK_MAXCOMPUTE(SEATUNNEL, SINK, MAXCOMPUTE, "connector-maxcompute"),
    SOURCE_KUDU(SEATUNNEL, SOURCE, KUDU, "connector-kudu"),
    SINK_KUDU(SEATUNNEL, SINK, KUDU, "connector-kudu"),
    SOURCE_IOTDB(SEATUNNEL, SOURCE, IOTDB, "connector-iotdb"),
    SINK_IOTDB(SEATUNNEL, SINK, IOTDB, "connector-iotdb"),
    SOURCE_OPENMLDB(SEATUNNEL, SOURCE, OPENMLDB, "connector-openmldb"),
    SOURCE_NEO4J(SEATUNNEL, SOURCE, NEO4J, "connector-neo4j"),
    SINK_NEO4J(SEATUNNEL, SINK, NEO4J, "connector-neo4j"),
    SOURCE_INFLUXDB(SEATUNNEL, SOURCE, INFLUXDB, "connector-influxdb"),
    SINK_INFLUXDB(SEATUNNEL, SINK, INFLUXDB, "connector-influxdb"),

    SINK_SENTRY(SEATUNNEL, SINK, SENTRY, "connector-sentry"),
    SOURCE_GOOGLE_SHEETS(SEATUNNEL, SOURCE, GOOGLE_SHEETS, "connector-google-sheets"),
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

    public static SeaTunnelPluginMapping of(SeaTunnelPluginName pluginName) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.getPluginName() == pluginName)
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(SeaTunnelPluginMapping.class, pluginName.getValue()));
    }
}
