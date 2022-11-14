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
import cn.sliew.scaleph.ds.modal.file.FtpDataSource;
import cn.sliew.scaleph.ds.modal.file.OSSDataSource;
import cn.sliew.scaleph.ds.modal.file.S3DataSource;
import cn.sliew.scaleph.ds.modal.jdbc.*;
import cn.sliew.scaleph.ds.modal.nosql.ElasticsearchDataSource;
import cn.sliew.scaleph.ds.modal.nosql.RedisDataSource;
import cn.sliew.scaleph.ds.modal.olap.KuduDataSource;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.List;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(name = "MySQL", value = MySQLDataSource.class),
        @JsonSubTypes.Type(name = "Oracle", value = OracleDataSource.class),
        @JsonSubTypes.Type(name = "PostgreSQL", value = PostgreSQLDataSource.class),
        @JsonSubTypes.Type(name = "SQLServer", value = SQLServerDataSource.class),
        @JsonSubTypes.Type(name = "DmDB", value = DmDBDataSource.class),
        @JsonSubTypes.Type(name = "GBase8a", value = GBase8aDataSource.class),
        @JsonSubTypes.Type(name = "Greenplum", value = GreenplumDataSource.class),
        @JsonSubTypes.Type(name = "Phoenix", value = PhoenixDataSource.class),

        @JsonSubTypes.Type(name = "Redis", value = RedisDataSource.class),
        @JsonSubTypes.Type(name = "Elasticsearch", value = ElasticsearchDataSource.class),

        @JsonSubTypes.Type(name = "Ftp", value = FtpDataSource.class),
        @JsonSubTypes.Type(name = "OSS", value = OSSDataSource.class),
        @JsonSubTypes.Type(name = "S3", value = S3DataSource.class),

        @JsonSubTypes.Type(name = "Kudu", value = KuduDataSource.class),

        @JsonSubTypes.Type(name = "IoTDB", value = IoTDBDataSource.class),
        @JsonSubTypes.Type(name = "Neo4j", value = Neo4jDataSource.class),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDataSource {

    @NotNull
    @ApiModelProperty("data source type id")
    private Long dsTypeId;

    @ApiModelProperty("version")
    private String version;

    @NotBlank
    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("remark")
    private String remark;

    @ApiModelProperty("additional props")
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

}
