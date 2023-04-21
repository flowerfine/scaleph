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

package cn.sliew.scaleph.catalog.factory;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.catalog.model.SakuraColumn;
import cn.sliew.scaleph.common.dict.catalog.CatalogColumnType;
import org.apache.flink.table.api.Expressions;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableException;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.expressions.ResolvedExpression;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.utils.LogicalTypeParser;
import org.apache.flink.table.types.utils.TypeConversions;

public enum CatalogColumnFactory {
    ;

    public static SakuraColumn toColumn(Column column) {
        SakuraColumn sakuraColumn = new SakuraColumn();
        sakuraColumn.setName(column.getName());
        sakuraColumn.setDataType(serializeDataType(column.getDataType()));
        column.getComment().ifPresent(comment -> sakuraColumn.setComment(comment));
        if (column instanceof Column.PhysicalColumn) {
            sakuraColumn.setType(CatalogColumnType.PHYSICAL);
        }
        if (column instanceof Column.ComputedColumn) {
            Column.ComputedColumn computedColumn = (Column.ComputedColumn) column;
            sakuraColumn.setType(CatalogColumnType.COMPUTED);
            sakuraColumn.setExpression(serializeResolvedExpression(computedColumn.getExpression()));
        }
        if (column instanceof Column.MetadataColumn) {
            Column.MetadataColumn metadataColumn = (Column.MetadataColumn) column;
            sakuraColumn.setType(CatalogColumnType.METADATA);
            metadataColumn.getMetadataKey().ifPresent(metadataKey -> sakuraColumn.setMetadataKey(metadataKey));
            sakuraColumn.setVirtual(metadataColumn.isVirtual());
        }
        return sakuraColumn;
    }

    public static Column toCatalog(SakuraColumn column) {
        switch (column.getType()) {
            case PHYSICAL:
                return toPhysicalColumn(column);
            case COMPUTED:
                break;
            case METADATA:
                break;
            default:

        }
        return null;
    }

    public static Column.PhysicalColumn toPhysicalColumn(SakuraColumn column) {
        return Column.physical(column.getName(), deserializeDataType(column.getDataType())).withComment(column.getComment());
    }

    public static Column.ComputedColumn toComputedColumn(SakuraColumn column) {
        return Column.computed(column.getName(), column.getExpression()).withComment(column.getComment());
    }

    private static String serializeDataType(DataType dataType) {
        try {
            return dataType.getLogicalType().asSerializableString();
        } catch (TableException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    private static DataType deserializeDataType(String dataType) {
        return TypeConversions.fromLogicalToDataType(LogicalTypeParser.parse(dataType));
    }

    private static String serializeResolvedExpression(ResolvedExpression expression) {
        try {
            return expression.asSerializableString();
        } catch (TableException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    private static ResolvedExpression deserializeResolvedExpression(String expression) {

    }

}
