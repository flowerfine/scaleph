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

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataSourceType implements DictInstance {

    MYSQL("MySQL", "MySQL", "mysql remark"),
    ORACLE("Oracle", "Oracle", null),
    POSTGRESQL("PostgreSQL", "PostgreSQL", null),
    SQLSERVER("SQLServer", "SQLServer", null),
    DMDB("DmDB", "DmDB", null),
    GBASE8A("GBase8a", "GBase8a", null),
    GREENPLUM("Greenplum", "Greenplum", null),
    PHOENIX("Phoenix", "Phoenix", null),

    REDIS("Redis", "Redis", null),
    ELASTICSEARCH("Elasticsearch", "Elasticsearch", null),
    SOLR("Solr", "Solr", null),
    MONGODB("MongoDB", "MongoDB", null),
    TIDB("TiDB", "TiDB", null),
    CASSANDRA("Cassandra", "Cassandra", null),

    KAFKA("Kafka", "Kafka", null),
    PULSAR("Pulsar", "Pulsar", null),
    DATAHUB("DataHub", "DataHub", null),

    FTP("Ftp", "Ftp", null),
    SFTP("Sftp", "Sftp", null),
    OSS("OSS", "OSS", null),
    OSSJINDO("OSSJindo", "OSSJindo", null),
    S3("S3", "S3", null),
    HDFS("HDFS", "HDFS", null),

    HIVE("Hive", "Hive", null),
    HBASE("HBase", "HBase", null),
    IMPALA("Impala", "Impala", null),
    DORIS("Doris", "Doris", null),
    STARROCKS("StarRocks", "StarRocks", null),
    CLICKHOUSE("ClickHouse", "ClickHouse", null),
    KUDU("Kudu", "Kudu", null),
    KYLIN("Kylin", "Kylin", null),
    DRUID("Druid", "Druid", null),
    MAXCOMPUTE("MaxCompute", "MaxCompute", null),

    IOTDB("IoTDB", "IoTDB", null),
    NEO4J("Neo4j", "Neo4j", null),

    HUDI("Hudi", "Hudi", null),
    ICEBERG("Iceberg", "Iceberg", null),

    INFLUXDB("InfluxDB", "InfluxDB", null),
    EMAIL("Email", "Email", null),
    SOCKET("Socket", "Socket", null),
    HTTP("Http", "Http", null),
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
    private String remark;

    DataSourceType(String value, String label, String remark) {
        this.value = value;
        this.label = label;
        this.remark = remark;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getRemark() {
        return remark;
    }
}
