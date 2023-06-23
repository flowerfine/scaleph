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

package cn.sliew.scaleph.meta.service.dto;

import cn.sliew.scaleph.system.model.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "元数据-表信息", description = "元数据-表信息")
public class TableMetaDTO extends BaseDTO {

    private static final long serialVersionUID = -5777242055646416476L;

    /**
     * 字段
     */
    private List<TableColumnMetaDTO> columns;

    private Long dataSourceId;
    /**
     * 表所属的catalog 可能为null
     */
    private String tableCatalog;
    /**
     * 表所属的 schema
     */
    private String tableSchema;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表的类型 TABLE/VIEW/
     * 调整为枚举类型，统一各种数据源
     */
    private DictVO tableType;
    /**
     * 表空间
     */
    private String tableSpace;
    /**
     * 表的备注
     */
    private String tableComment;
    /**
     * 数据量大小，非精确数据量
     */
    private Long tableRows;
    /**
     * 数据占用存储大小
     */
    private Long dataBytes;
    /**
     * 索引占用存储大小
     */
    private Long indexBytes;
    /**
     * 创建时间
     */
    private Date tableCreateTime;
    /**
     * 最后ddl时间
     */
    private Date lastDdlTime;
    /**
     * 最后数据查看时间
     */
    private Date lastAccessTime;
    /**
     * 生命周期 单位天
     */
    private Integer lifeCycle;
    /**
     * 是否分区表
     */
    private DictVO isPartitioned;
    /**
     * 扩展属性
     */
    private Map<String, String> attrs;
}
