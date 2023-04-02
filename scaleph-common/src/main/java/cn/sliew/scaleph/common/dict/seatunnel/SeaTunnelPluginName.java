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

import cn.sliew.scaleph.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SeaTunnelPluginName implements DictInstance {

    FAKESOURCE("FakeSource", "Fake"),
    ASSERT("Assert", "Assert"),
    SOCKET("Socket", "Socket"),
    CONSOLE("Console", "Console"),
    EMAIL("EmailSink", "Email"),
    SLACK("SlackSink", "Slack"),
    HTTP("Http", "Http"),
    FEISHU("Feishu", "Feishu"),
    WECHAT("WeChat", "WeChat"),
    DINGTALK("DingTalk", "DingTalk"),
    MYHOURS("MyHours", "MyHours"),
    LEMLIST("Lemlist", "Lemlist"),
    KLAVIYO("Klaviyo", "Klaviyo"),
    ONESIGNAL("OneSignal", "OneSignal"),
    JIRA("Jira", "Jira"),
    GITLAB("Gitlab", "Gitlab"),
    GITHUB("Github", "Github"),
    NOTION("Notion", "Notion"),
    PERSISTIQ("Persistiq", "Persistiq"),

    LOCAL_FILE("LocalFile", "LocalFile"),
    FTP_FILE("FtpFile", "FtpFile"),
    SFTP_FILE("SftpFile", "SftpFile"),
    HDFS_FILE("HdfsFile", "HdfsFile"),
    S3_FILE("S3File", "S3File"),
    OSS_FILE("OssFile", "OssFile"),
    OSS_JINDO_FILE("OssJindoFile", "OssJindoFile"),

    KAFKA("Kafka", "Kafka"),
    PULSAR("Pulsar", "Pulsar"),
    DATAHUB("DataHub", "DataHub"),
    RABBITMQ("RabbitMQ", "RabbitMQ"),

    JDBC("Jdbc", "Jdbc"),
    REDIS("Redis", "Redis"),
    ELASTICSEARCH("Elasticsearch", "Elasticsearch"),
    MONGODB("MongoDB", "MongoDB"),
    AMAZON_DYNAMODB("AmazonDynamodb", "AmazonDynamodb"),
    CASSANDRA("Cassandra", "Cassandra"),
    TABLESTORE("Tablestore", "Tablestore"),

    MYSQL_CDC("MySQL-CDC", "MySQL-CDC"),
    SQLSERVER_CDC("SqlServer-CDC", "SqlServer-CDC"),

    HIVE("Hive", "Hive"),
    CLICKHOUSE("Clickhouse", "ClickHouse"),
    CLICKHOUSE_FILE("ClickhouseFile", "ClickHouseFile"),
    DORIS("Doris", "Doris"),
    SELECTDB_CLOUD("SelectDBCloud", "SelectDBCloud"),
    STARROCKS("StarRocks", "StarRocks"),
    HUDI("Hudi", "Hudi"),
    ICEBERG("Iceberg", "Iceberg"),
    S3REDSHIFT("S3Redshift", "S3Redshift"),
    MAXCOMPUTE("Maxcompute", "MaxCompute"),
    HBASE("Hbase", "Hbase"),
    KUDU("Kudu", "Kudu"),
    IOTDB("IoTDB", "IoTDB"),
    OPENMLDB("OpenMldb", "OpenMldb"),
    NEO4J("Neo4j", "Neo4j"),
    INFLUXDB("InfluxDB", "InfluxDB"),
    TDENGINE("TDengine", "TDengine"),

    SENTRY("Sentry", "Sentry"),
    GOOGLE_SHEETS("GoogleSheets", "GoogleSheets"),

    COPY("Copy", "Copy"),
    FIELD_MAPPER("FieldMapper", "FieldMapper"),
    FILTER_ROW_KIND("FilterRowKind", "FilterRowKind"),
    FILTER("Filter", "Filter"),
    REPLACE("Replace", "Replace"),
    SPLIT("Split", "Split"),
    SQL("Sql", "Sql"),
    ;

    @JsonCreator
    public static SeaTunnelPluginName of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(SeaTunnelPluginName.class, value));
    }

    @EnumValue
    private String value;
    private String label;

    SeaTunnelPluginName(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
