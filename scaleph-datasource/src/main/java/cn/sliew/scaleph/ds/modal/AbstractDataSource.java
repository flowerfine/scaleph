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

package cn.sliew.scaleph.ds.modal;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.common.jackson.polymorphic.Polymorphic;
import cn.sliew.scaleph.common.jackson.polymorphic.PolymorphicResolver;
import cn.sliew.scaleph.ds.modal.file.*;
import cn.sliew.scaleph.ds.modal.jdbc.*;
import cn.sliew.scaleph.ds.modal.mq.DataHubDataSource;
import cn.sliew.scaleph.ds.modal.mq.KafkaDataSource;
import cn.sliew.scaleph.ds.modal.mq.PulsarDataSource;
import cn.sliew.scaleph.ds.modal.nosql.CassandraDataSource;
import cn.sliew.scaleph.ds.modal.nosql.ElasticsearchDataSource;
import cn.sliew.scaleph.ds.modal.nosql.MongoDBDataSource;
import cn.sliew.scaleph.ds.modal.nosql.RedisDataSource;
import cn.sliew.scaleph.ds.modal.olap.*;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Iterator;
import java.util.List;

@Data
@JsonTypeIdResolver(AbstractDataSource.DataSourceResolver.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDataSource implements Polymorphic<DataSourceType> {

    @NotNull
    @Schema(description = "data source type id")
    private Long dsTypeId;

    @Schema(description = "version")
    private String version;

    @NotBlank
    @Schema(description = "name")
    private String name;

    @Schema(description = "remark")
    private String remark;

    @Schema(description = "additional props")
    private List<PropValuePair> additionalProps;

    public abstract DataSourceType getType();

    public abstract DsInfoDTO toDsInfo();

    public static AbstractDataSource fromDsInfo(ObjectNode jsonNode) {
        jsonNode.putPOJO("type", jsonNode.path("dsType").path("type").path("value"));
        if (jsonNode.has("props")) {
            ObjectNode props = (ObjectNode) jsonNode.get("props");
            Iterator<String> fieldNames = props.fieldNames();
            while (fieldNames.hasNext()) {
                String name = fieldNames.next();
                jsonNode.putPOJO(name, props.get(name));
            }
        }
        return JacksonUtil.toObject(jsonNode, AbstractDataSource.class);
    }

    public static final class DataSourceResolver extends PolymorphicResolver<DataSourceType> {
        public DataSourceResolver() {
            bindDefault(MySQLDataSource.class);

            bind(DataSourceType.MYSQL, MySQLDataSource.class);
            bind(DataSourceType.ORACLE, OracleDataSource.class);
            bind(DataSourceType.POSTGRESQL, PostgreSQLDataSource.class);
            bind(DataSourceType.SQLSERVER, SQLServerDataSource.class);
            bind(DataSourceType.DMDB, DmDBDataSource.class);
            bind(DataSourceType.GBASE8A, GBase8aDataSource.class);
            bind(DataSourceType.GREENPLUM, GreenplumDataSource.class);
            bind(DataSourceType.PHOENIX, PhoenixDataSource.class);

            bind(DataSourceType.REDIS, RedisDataSource.class);
            bind(DataSourceType.ELASTICSEARCH, ElasticsearchDataSource.class);
            bind(DataSourceType.MONGODB, MongoDBDataSource.class);
            bind(DataSourceType.CASSANDRA, CassandraDataSource.class);

            bind(DataSourceType.KAFKA, KafkaDataSource.class);
            bind(DataSourceType.PULSAR, PulsarDataSource.class);
            bind(DataSourceType.DATAHUB, DataHubDataSource.class);

            bind(DataSourceType.FTP, FtpDataSource.class);
            bind(DataSourceType.SFTP, SftpDataSource.class);
            bind(DataSourceType.OSS, OSSDataSource.class);
            bind(DataSourceType.OSSJINDO, OSSJindoDataSource.class);
            bind(DataSourceType.S3, S3DataSource.class);
            bind(DataSourceType.HDFS, HDFSDataSource.class);

            bind(DataSourceType.HIVE, HiveDataSource.class);

            bind(DataSourceType.CLICKHOUSE, ClickHouseDataSource.class);
            bind(DataSourceType.KUDU, KuduDataSource.class);
            bind(DataSourceType.DORIS, DorisDataSource.class);
            bind(DataSourceType.STARROCKS, StarRocksDataSource.class);
            bind(DataSourceType.MAXCOMPUTE, MaxComputeDataSource.class);

            bind(DataSourceType.IOTDB, IoTDBDataSource.class);
            bind(DataSourceType.NEO4J, Neo4jDataSource.class);

            bind(DataSourceType.SOCKET, SocketDataSource.class);
            bind(DataSourceType.HTTP, HttpDataSource.class);
            bind(DataSourceType.INFLUXDB, InfluxDBDataSource.class);
        }

        @Override
        protected String typeFromSubtype(Object obj) {
            return subTypes.inverse().get(obj.getClass()).getValue();
        }

        @Override
        protected Class<?> subTypeFromType(String id) {
            Class<?> subType = subTypes.get(DataSourceType.of(id));
            return subType != null ? subType : defaultClass;
        }
    }

}
