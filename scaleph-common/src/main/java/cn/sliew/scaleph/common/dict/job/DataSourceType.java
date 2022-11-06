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

package cn.sliew.scaleph.common.dict.job;

import cn.sliew.scaleph.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataSourceType implements DictInstance {

    MYSQL("MySQL", "MySQL"),
    ORACLE("Oracle", "Oracle"),
    POSTGRESQL("PostgreSQL", "PostgreSQL"),
    SQLSERVER("SQLServer", "SQLServer"),
    DMDB("DmDB", "DmDB"),
    GBASE8A("GBase8a", "GBase8a"),
    GREENPLUM("Greenplum", "Greenplum"),
    PHOENIX("Phoenix", "Phoenix"),

    REDIS("Redis", "Redis"),
    ELASTICSEARCH("Elasticsearch", "Elasticsearch"),
    SOLR("Solr", "Solr"),
    MONGODB("MongoDB", "MongoDB"),
    TIDB("TiDB", "TiDB"),

    KAFKA("Kafka", "Kafka"),
    PULSAR("Pulsar", "Pulsar"),
    DATAHUB("DataHub", "DataHub"),

    FTP("Ftp", "Ftp"),
    SFTP("Sftp", "Sftp"),
    OSS("OSS", "OSS"),
    S3("S3", "S3"),
    HDFS("HDFS", "HDFS"),
    HIVE("Hive", "Hive"),
    HBASE("HBase", "HBase"),

    IMPALA("Impala", "Impala"),
    DORIS("Doris", "Doris"),
    CLICKHOUSE("ClickHouse", "ClickHouse"),
    KUDU("Kudu", "Kudu"),
    KYLIN("Kylin", "Kylin"),
    DRUID("Druid", "Druid"),

    IOTDB("IoTDB", "IoTDB"),
    NEO4J("Neo4j", "Neo4j"),

    HUDI("Hudi", "Hudi"),
    ICEBERG("Iceberg", "Iceberg"),

    INFLUXDB("InfluxDB", "InfluxDB"),
    EMAIL("Email", "Email"),
    SOCKET("Socket", "Socket"),
    ;

    @JsonCreator
    public static DataSourceType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(DataSourceType.class, value));
    }

    @EnumValue
    private String value;
    private String label;

    DataSourceType(String value, String label) {
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
