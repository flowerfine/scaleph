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

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "元数据-列信息", description = "元数据-列信息")
public class TableColumnMetaDTO extends BaseDTO {

    private static final long serialVersionUID = -6282945619070309858L;

    private Long id;

    private Long tableId;

    private String columnName;

    private String dataType;

    private Long dataLength;

    private Integer dataPrecision;

    private Integer dataScale;

    private DictVO nullable;

    private String dataDefault;

    private String lowValue;

    private String highValue;

    private Integer columnOrdinal;

    private String columnComment;

    private DictVO isPrimaryKey;
}
