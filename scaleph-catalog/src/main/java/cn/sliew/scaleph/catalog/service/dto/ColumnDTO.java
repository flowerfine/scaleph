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

package cn.sliew.scaleph.catalog.service.dto;

import cn.sliew.scaleph.common.dict.catalog.CatalogColumnType;
import lombok.Data;
import org.apache.flink.table.types.DataType;

/**
 * @see org.apache.flink.table.catalog.Column
 */
@Data
public class ColumnDTO {

    private String name;
    private CatalogColumnType type;
    private DataType dataType;

}
