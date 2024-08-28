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

import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelConnectorFeature.*;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelConnectorHealth.*;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType.SEATUNNEL;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName.*;
import static cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType.*;

/**
 * https://github.com/apache/incubator-seatunnel/blob/dev/plugin-mapping.properties
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SeaTunnelPluginMapping {

    SOURCE_FAKE(SEATUNNEL, SOURCE, FAKESOURCE, "connector-fake", GA, BATCH, STREAM, SCHEMA_PROJECTION),
    SINK_ASSERT(SEATUNNEL, SINK, ASSERT, "connector-assert", BETA, SCHEMA_PROJECTION),
    SOURCE_SOCKET(SEATUNNEL, SOURCE, SOCKET, "connector-socket", BETA, BATCH, STREAM),
    SINK_SOCKET(SEATUNNEL, SINK, SOCKET, "connector-socket", BETA),
    SINK_CONSOLE(SEATUNNEL, SINK, CONSOLE, "connector-console", GA),
    SINK_EMAIL(SEATUNNEL, SINK, EMAIL, "connector-email", ALPHA),
    SINK_SLACK(SEATUNNEL, SINK, SLACK, "connector-slack", BETA),
    SOURCE_HTTP(SEATUNNEL, SOURCE, HTTP, "connector-http-base", BETA, BATCH, STREAM, SCHEMA_PROJECTION),
    SINK_HTTP(SEATUNNEL, SINK, HTTP, "connector-http-base", BETA),
    SINK_FEISHU(SEATUNNEL, SINK, FEISHU, "connector-http-feishu", ALPHA),
    @Deprecated
    SINK_WECHAT(SEATUNNEL, SINK, WECHAT, "connector-http-wechat", ALPHA),
    SINK_DINGTALK(SEATUNNEL, SINK, DINGTALK, "connector-dingtalk", ALPHA),
    SOURCE_MYHOURS(SEATUNNEL, SOURCE, MYHOURS, "connector-http-myhours", ALPHA, BATCH, SCHEMA_PROJECTION),
    SOURCE_LEMLIST(SEATUNNEL, SOURCE, LEMLIST, "connector-http-lemlist", BETA, BATCH, SCHEMA_PROJECTION),
    SOURCE_KLAVIYO(SEATUNNEL, SOURCE, KLAVIYO, "connector-http-klaviyo", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SOURCE_ONESIGNAL(SEATUNNEL, SOURCE, ONESIGNAL, "connector-http-onesignal", BETA, BATCH, SCHEMA_PROJECTION),
    SOURCE_JIRA(SEATUNNEL, SOURCE, JIRA, "connector-http-jira", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SOURCE_GITLAB(SEATUNNEL, SOURCE, GITLAB, "connector-http-gitlab", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SOURCE_GITHUB(SEATUNNEL, SOURCE, GITHUB, "connector-http-github", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SOURCE_NOTION(SEATUNNEL, SOURCE, NOTION, "connector-http-notion", ALPHA, BATCH, SCHEMA_PROJECTION),
    SOURCE_PERSISTIQ(SEATUNNEL, SOURCE, PERSISTIQ, "connector-http-persistiq", ALPHA, BATCH, SCHEMA_PROJECTION),

    SOURCE_LOCAL_FILE(SEATUNNEL, SOURCE, LOCAL_FILE, "connector-file-local", GA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_LOCAL_FILE(SEATUNNEL, SINK, LOCAL_FILE, "connector-file-local", GA, EXACTLY_ONCE),
    SOURCE_FTP_FILE(SEATUNNEL, SOURCE, FTP_FILE, "connector-file-ftp", ALPHA, BATCH, SCHEMA_PROJECTION, PARALLELISM),
    SINK_FTP_FILE(SEATUNNEL, SINK, FTP_FILE, "connector-file-ftp", BETA, EXACTLY_ONCE),
    SOURCE_SFTP_FILE(SEATUNNEL, SOURCE, SFTP_FILE, "connector-file-sftp", BETA, BATCH, SCHEMA_PROJECTION, PARALLELISM),
    SINK_SFTP_FILE(SEATUNNEL, SINK, SFTP_FILE, "connector-file-sftp", BETA, EXACTLY_ONCE),
    SOURCE_HDFS_FILE(SEATUNNEL, SOURCE, HDFS_FILE, "connector-file-hadoop", GA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_HDFS_FILE(SEATUNNEL, SINK, HDFS_FILE, "connector-file-hadoop", GA, EXACTLY_ONCE),
    SOURCE_S3_FILE(SEATUNNEL, SOURCE, S3_FILE, "connector-file-s3", GA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_S3_FILE(SEATUNNEL, SINK, S3_FILE, "connector-file-s3", GA, EXACTLY_ONCE),
    SOURCE_OSS_FILE(SEATUNNEL, SOURCE, OSS_FILE, "connector-file-oss", BETA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_OSS_FILE(SEATUNNEL, SINK, OSS_FILE, "connector-file-oss", BETA, EXACTLY_ONCE),
    SOURCE_OSS_JINDO_FILE(SEATUNNEL, SOURCE, OSS_JINDO_FILE, "connector-file-jindo-oss", UNKNOWN, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_OSS_JINDO_FILE(SEATUNNEL, SINK, OSS_JINDO_FILE, "connector-file-jindo-oss", UNKNOWN, EXACTLY_ONCE),
    SOURCE_COS_FILE(SEATUNNEL, SOURCE, COS_FILE, "connector-file-cos", UNKNOWN, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_COS_FILE(SEATUNNEL, SINK, COS_FILE, "connector-file-cos", UNKNOWN, EXACTLY_ONCE),

    SOURCE_KAFKA(SEATUNNEL, SOURCE, KAFKA, "connector-kafka", GA, BATCH, STREAM, EXACTLY_ONCE, PARALLELISM),
    SINK_KAFKA(SEATUNNEL, SINK, KAFKA, "connector-kafka", GA, EXACTLY_ONCE),
    SOURCE_PULSAR(SEATUNNEL, SOURCE, PULSAR, "connector-pulsar", BETA, BATCH, STREAM, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_PULSAR(SEATUNNEL, SINK, PULSAR, "connector-pulsar", BETA, BATCH, STREAM, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SOURCE_ROCKETMQ(SEATUNNEL, SOURCE, ROCKETMQ, "connector-rocketmq", UNKNOWN, BATCH, STREAM, EXACTLY_ONCE, SCHEMA_PROJECTION),
    SINK_ROCKETMQ(SEATUNNEL, SINK, ROCKETMQ, "connector-rocketmq", UNKNOWN, EXACTLY_ONCE),
    SINK_DATAHUB(SEATUNNEL, SINK, DATAHUB, "connector-datahub", ALPHA),
    SOURCE_AMAZON_SQS(SEATUNNEL, SOURCE, AMAZON_SQS, "connector-amazonsqs", UNKNOWN),
    SINK_AMAZON_SQS(SEATUNNEL, SINK, AMAZON_SQS, "connector-amazonsqs", UNKNOWN),
    SOURCE_RABBITMQ(SEATUNNEL, SOURCE, RABBITMQ, "connector-rabbitmq", BETA, STREAM, EXACTLY_ONCE, SCHEMA_PROJECTION),
    SINK_RABBITMQ(SEATUNNEL, SINK, RABBITMQ, "connector-rabbitmq", BETA),
    SINK_ACTIVEMQ(SEATUNNEL, SINK, ACTIVEMQ, "connector-activemq", BETA),

    SOURCE_JDBC(SEATUNNEL, SOURCE, JDBC, "connector-jdbc", GA, BATCH, SCHEMA_PROJECTION, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SINK_JDBC(SEATUNNEL, SINK, JDBC, "connector-jdbc", GA, EXACTLY_ONCE, CDC),
    SOURCE_REDIS(SEATUNNEL, SOURCE, REDIS, "connector-redis", BETA, BATCH, SCHEMA_PROJECTION),
    SINK_REDIS(SEATUNNEL, SINK, REDIS, "connector-redis", BETA),
    SOURCE_ELASTICSEARCH(SEATUNNEL, SOURCE, ELASTICSEARCH, "connector-elasticsearch", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SINK_ELASTICSEARCH(SEATUNNEL, SINK, ELASTICSEARCH, "connector-elasticsearch", GA, CDC),
    SOURCE_EASYSEARCH(SEATUNNEL, SOURCE, EASYSEARCH, "connector-easysearch", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SINK_EASYSEARCH(SEATUNNEL, SINK, EASYSEARCH, "connector-easysearch", UNKNOWN, CDC),
    SOURCE_MONGODB(SEATUNNEL, SOURCE, MONGODB, "connector-mongodb", BETA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SINK_MONGODB(SEATUNNEL, SINK, MONGODB, "connector-mongodb", BETA, EXACTLY_ONCE, CDC),
    SOURCE_AMAZON_DYNAMODB(SEATUNNEL, SOURCE, AMAZON_DYNAMODB, "connector-amazondynamodb", BETA, BATCH, SCHEMA_PROJECTION),
    SINK_AMAZON_DYNAMODB(SEATUNNEL, SINK, AMAZON_DYNAMODB, "connector-amazondynamodb", BETA),
    SOURCE_CASSANDRA(SEATUNNEL, SOURCE, CASSANDRA, "connector-cassandra", BETA, BATCH, SCHEMA_PROJECTION),
    SINK_CASSANDRA(SEATUNNEL, SINK, CASSANDRA, "connector-cassandra", BETA),
    SOURCE_TABLESTORE(SEATUNNEL, SOURCE, TABLESTORE, "connector-tablestore", ALPHA),
    SINK_TABLESTORE(SEATUNNEL, SINK, TABLESTORE, "connector-tablestore", ALPHA),
    SINK_GOOGLE_FIRE_STORE(SEATUNNEL, SINK, GOOGLE_FIRE_STORE, "connector-google-firestore", UNKNOWN),

    SOURCE_MYSQL_CDC(SEATUNNEL, SOURCE, MYSQL_CDC, "connector-cdc-mysql", GA, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SOURCE_SQLSERVER_CDC(SEATUNNEL, SOURCE, SQLSERVER_CDC, "connector-cdc-sqlserver", GA, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SOURCE_ORACLE_CDC(SEATUNNEL, SOURCE, ORACLE_CDC, "connector-cdc-oracle", UNKNOWN, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SOURCE_POSTGRESQL_CDC(SEATUNNEL, SOURCE, POSTGRESQL_CDC, "connector-cdc-postgres", UNKNOWN, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SOURCE_OPENGAUSS_CDC(SEATUNNEL, SOURCE, OPENGAUSS_CDC, "connector-cdc-opengauss", UNKNOWN, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SOURCE_MONGODB_CDC(SEATUNNEL, SOURCE, MONGODB_CDC, "connector-cdc-mongodb", UNKNOWN, STREAM, EXACTLY_ONCE, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),

    SOURCE_HIVE(SEATUNNEL, SOURCE, HIVE, "connector-hive", GA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_HIVE(SEATUNNEL, SINK, HIVE, "connector-hive", GA, EXACTLY_ONCE),
    SOURCE_CLICKHOUSE(SEATUNNEL, SOURCE, CLICKHOUSE, "connector-clickhouse", GA, BATCH, SCHEMA_PROJECTION),
    SINK_CLICKHOUSE(SEATUNNEL, SINK, CLICKHOUSE, "connector-clickhouse", GA, CDC),
    SINK_CLICKHOUSE_FILE(SEATUNNEL, SINK, CLICKHOUSE_FILE, "connector-clickhouse", GA),
    SOURCE_DORIS(SEATUNNEL, SOURCE, DORIS, "connector-doris", BETA, BATCH, SCHEMA_PROJECTION, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SINK_DORIS(SEATUNNEL, SINK, DORIS, "connector-doris", BETA, EXACTLY_ONCE, CDC),
    SINK_SELECTDB_CLOUD(SEATUNNEL, SINK, SELECTDB_CLOUD, "connector-selectdb-cloud", BETA),
    SOURCE_STARROCKS(SEATUNNEL, SOURCE, STARROCKS, "connector-starrocks", UNKNOWN, BATCH, SCHEMA_PROJECTION, PARALLELISM, SUPPORT_USER_DEFINED_SPLIT),
    SINK_STARROCKS(SEATUNNEL, SINK, STARROCKS, "connector-starrocks", ALPHA),
    SOURCE_HUDI(SEATUNNEL, SOURCE, HUDI, "connector-hudi", BETA, BATCH, EXACTLY_ONCE, PARALLELISM),
    SOURCE_ICEBERG(SEATUNNEL, SOURCE, ICEBERG, "connector-iceberg", BETA, BATCH, STREAM, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_ICEBERG(SEATUNNEL, SINK, ICEBERG, "connector-iceberg", UNKNOWN, CDC),
    SOURCE_PAIMON(SEATUNNEL, SOURCE, PAIMON, "connector-paimon", UNKNOWN, BATCH),
    SINK_PAIMON(SEATUNNEL, SINK, PAIMON, "connector-paimon", UNKNOWN, EXACTLY_ONCE),
    SINK_S3REDSHIFT(SEATUNNEL, SINK, S3REDSHIFT, "connector-s3-redshift", GA, EXACTLY_ONCE),
    SOURCE_MAXCOMPUTE(SEATUNNEL, SOURCE, MAXCOMPUTE, "connector-maxcompute", ALPHA, BATCH, PARALLELISM),
    SINK_MAXCOMPUTE(SEATUNNEL, SINK, MAXCOMPUTE, "connector-maxcompute", ALPHA),
    SOURCE_HBASE(SEATUNNEL, SOURCE, HBASE, "connector-hbase", ALPHA),
    SINK_HBASE(SEATUNNEL, SINK, HBASE, "connector-hbase", ALPHA),
    SOURCE_KUDU(SEATUNNEL, SOURCE, KUDU, "connector-kudu", BETA, BATCH),
    SINK_KUDU(SEATUNNEL, SINK, KUDU, "connector-kudu", BETA),
    SINK_DRUID(SEATUNNEL, SINK, DRUID, "connector-druid", BETA),
    SOURCE_IOTDB(SEATUNNEL, SOURCE, IOTDB, "connector-iotdb", GA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_IOTDB(SEATUNNEL, SINK, IOTDB, "connector-iotdb", GA, EXACTLY_ONCE),
    SOURCE_OPENMLDB(SEATUNNEL, SOURCE, OPENMLDB, "connector-openmldb", BETA, BATCH, STREAM),
    SOURCE_NEO4J(SEATUNNEL, SOURCE, NEO4J, "connector-neo4j", UNKNOWN, BATCH, SCHEMA_PROJECTION),
    SINK_NEO4J(SEATUNNEL, SINK, NEO4J, "connector-neo4j", BETA),
    SOURCE_INFLUXDB(SEATUNNEL, SOURCE, INFLUXDB, "connector-influxdb", BETA, BATCH, EXACTLY_ONCE, SCHEMA_PROJECTION, PARALLELISM),
    SINK_INFLUXDB(SEATUNNEL, SINK, INFLUXDB, "connector-influxdb", BETA),
    SOURCE_TDENGINE(SEATUNNEL, SOURCE, TDENGINE, "connector-tdengine", BETA),
    SINK_TDENGINE(SEATUNNEL, SINK, TDENGINE, "connector-tdengine", BETA),
    SOURCE_SLS(SEATUNNEL, SOURCE, SLS, "connector-sls", BETA),

    SOURCE_MILVUS(SEATUNNEL, SOURCE, MILVUS, "connector-milvus", UNKNOWN),
    SINK_MILVUS(SEATUNNEL, SINK, MILVUS, "connector-milvus", UNKNOWN),
    SOURCE_WEB3J(SEATUNNEL, SOURCE, WEB3J, "connector-web3j", UNKNOWN),

    SINK_SENTRY(SEATUNNEL, SINK, SENTRY, "connector-sentry", ALPHA),
    SOURCE_GOOGLE_SHEETS(SEATUNNEL, SOURCE, GOOGLE_SHEETS, "connector-google-sheets", UNKNOWN, BATCH, SCHEMA_PROJECTION),

    TRANSFORM_COPY(SEATUNNEL, TRANSFORM, COPY, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_FIELD_MAPPER(SEATUNNEL, TRANSFORM, FIELD_MAPPER, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_FILTER_ROW_KIND(SEATUNNEL, TRANSFORM, FILTER_ROW_KIND, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_FILTER(SEATUNNEL, TRANSFORM, FILTER, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_REPLACE(SEATUNNEL, TRANSFORM, REPLACE, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_SPLIT(SEATUNNEL, TRANSFORM, SPLIT, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_SQL(SEATUNNEL, TRANSFORM, SQL, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_JSON_PATH(SEATUNNEL, TRANSFORM, JSON_PATH, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_DYNAMIC_COMPILE(SEATUNNEL, TRANSFORM, DYNAMIC_COMPILE, "seatunnel-transforms-v2", UNKNOWN),
    TRANSFORM_LLM(SEATUNNEL, TRANSFORM, LLM, "seatunnel-transforms-v2", UNKNOWN),
    ;

    private SeaTunnelEngineType engineType;
    private SeaTunnelPluginType pluginType;
    private SeaTunnelPluginName pluginName;
    private String pluginJarPrefix;
    private SeaTunnelConnectorHealth health;
    private SeaTunnelConnectorFeature[] features;


    SeaTunnelPluginMapping(SeaTunnelEngineType engineType,
                           SeaTunnelPluginType pluginType,
                           SeaTunnelPluginName pluginName,
                           String pluginJarPrefix,
                           SeaTunnelConnectorHealth health,
                           SeaTunnelConnectorFeature... features) {
        this.engineType = engineType;
        this.pluginType = pluginType;
        this.pluginName = pluginName;
        this.pluginJarPrefix = pluginJarPrefix;
        this.health = health;
        this.features = features;
    }

    public static SeaTunnelPluginMapping of(SeaTunnelPluginName pluginName) {
        return Arrays.stream(values())
                .filter(mapping -> mapping.getPluginName() == pluginName)
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(SeaTunnelPluginMapping.class, pluginName.getValue()));
    }
}
