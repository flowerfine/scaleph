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
import cn.sliew.scaleph.catalog.model.SakuraSchema;
import cn.sliew.scaleph.catalog.model.SakuraUniqueConstraint;
import cn.sliew.scaleph.catalog.model.SakuraWatermark;
import cn.sliew.scaleph.common.dict.catalog.CatalogColumnType;
import cn.sliew.scaleph.common.dict.catalog.CatalogConstraintType;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableException;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.catalog.UniqueConstraint;
import org.apache.flink.table.catalog.WatermarkSpec;
import org.apache.flink.table.expressions.Expression;
import org.apache.flink.table.expressions.ResolvedExpression;
import org.apache.flink.table.expressions.SqlCallExpression;
import org.apache.flink.table.types.AbstractDataType;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.utils.LogicalTypeParser;
import org.apache.flink.table.types.utils.TypeConversions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum CatalogSchemaFactory {
    ;

    public static SakuraSchema toSchema(ResolvedSchema schema) {
        SakuraSchema sakuraSchema = new SakuraSchema();
        List<SakuraColumn> columns = schema.getColumns().stream()
                .map(CatalogSchemaFactory::toColumn)
                .collect(Collectors.toList());
        sakuraSchema.setColumns(columns);
        List<SakuraWatermark> watermarks = schema.getWatermarkSpecs().stream()
                .map(CatalogSchemaFactory::toWatermark)
                .collect(Collectors.toList());
        sakuraSchema.setWatermarks(watermarks);
        schema.getPrimaryKey().map(CatalogSchemaFactory::toConstraint).ifPresent(constraint -> sakuraSchema.setPrimaryKey(constraint));
        return sakuraSchema;
    }

    public static SakuraSchema toSchema(Schema schema) {
        SakuraSchema sakuraSchema = new SakuraSchema();
        List<SakuraColumn> columns = schema.getColumns().stream()
                .map(CatalogSchemaFactory::toColumn)
                .collect(Collectors.toList());
        sakuraSchema.setColumns(columns);
        List<SakuraWatermark> watermarks = schema.getWatermarkSpecs().stream()
                .map(CatalogSchemaFactory::toWatermark)
                .collect(Collectors.toList());
        sakuraSchema.setWatermarks(watermarks);
        schema.getPrimaryKey().map(CatalogSchemaFactory::toConstraint).ifPresent(constraint -> sakuraSchema.setPrimaryKey(constraint));
        return sakuraSchema;
    }

    public static Schema toCatalog(SakuraSchema sakuraSchema) {
        Schema.Builder builder = Schema.newBuilder();
        Optional.ofNullable(sakuraSchema.getColumns()).ifPresent(columns -> columns.stream().forEach(column -> addColumn(builder, column)));
        Optional.ofNullable(sakuraSchema.getWatermarks()).ifPresent(watermarks -> watermarks.stream().forEach(watermark -> addWatermark(builder, watermark)));
        Optional.ofNullable(sakuraSchema.getPrimaryKey()).ifPresent(primaryKey -> addConstraint(builder, primaryKey));
        return builder.build();
    }

    public static SakuraColumn toColumn(Column column) {
        SakuraColumn sakuraColumn = new SakuraColumn();
        sakuraColumn.setName(column.getName());
        serializeDataType(column.getDataType()).ifPresent(dataType -> sakuraColumn.setDataType(dataType));
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

    public static SakuraColumn toColumn(Schema.UnresolvedColumn column) {
        SakuraColumn sakuraColumn = new SakuraColumn();
        sakuraColumn.setName(column.getName());
        column.getComment().ifPresent(comment -> sakuraColumn.setComment(comment));
        if (column instanceof Schema.UnresolvedPhysicalColumn) {
            Schema.UnresolvedPhysicalColumn physicalColumn = (Schema.UnresolvedPhysicalColumn) column;
            sakuraColumn.setType(CatalogColumnType.PHYSICAL);
            serializeDataType(physicalColumn.getDataType()).ifPresent(dataType -> sakuraColumn.setDataType(dataType));
        }
        if (column instanceof Schema.UnresolvedComputedColumn) {
            Schema.UnresolvedComputedColumn computedColumn = (Schema.UnresolvedComputedColumn) column;
            sakuraColumn.setType(CatalogColumnType.COMPUTED);
            serializeExpression(computedColumn.getExpression()).ifPresent(expression -> sakuraColumn.setExpression(expression));
        }
        if (column instanceof Schema.UnresolvedMetadataColumn) {
            Schema.UnresolvedMetadataColumn metadataColumn = (Schema.UnresolvedMetadataColumn) column;
            sakuraColumn.setType(CatalogColumnType.METADATA);
            serializeDataType(metadataColumn.getDataType()).ifPresent(dataType -> sakuraColumn.setDataType(dataType));
            sakuraColumn.setMetadataKey(metadataColumn.getMetadataKey());
            sakuraColumn.setVirtual(metadataColumn.isVirtual());
        }
        return sakuraColumn;
    }

    public static void addColumn(Schema.Builder schemaBuilder, SakuraColumn column) {
        switch (column.getType()) {
            case PHYSICAL:
                schemaBuilder.column(column.getName(), column.getDataType()).withComment(column.getComment());
                break;
            case COMPUTED:
                schemaBuilder.columnByExpression(column.getName(), column.getExpression()).withComment(column.getComment());
                break;
            case METADATA:
                schemaBuilder.columnByMetadata(column.getName(), column.getDataType(), column.getMetadataKey(), column.isVirtual()).withComment(column.getComment());
                break;
            case WATERMARK:
                schemaBuilder.watermark(column.getName(), column.getExpression()).withComment(column.getComment());
                break;
            default:
        }
    }

    public static SakuraWatermark toWatermark(WatermarkSpec watermark) {
        SakuraWatermark sakuraWatermark = new SakuraWatermark();
        sakuraWatermark.setName(watermark.getRowtimeAttribute());
        sakuraWatermark.setExpression(serializeResolvedExpression(watermark.getWatermarkExpression()));
        return sakuraWatermark;
    }

    public static SakuraWatermark toWatermark(Schema.UnresolvedWatermarkSpec watermark) {
        SakuraWatermark sakuraWatermark = new SakuraWatermark();
        sakuraWatermark.setName(watermark.getColumnName());
        serializeExpression(watermark.getWatermarkExpression()).ifPresent(expression -> sakuraWatermark.setExpression(expression));
        return sakuraWatermark;
    }

    public static void addWatermark(Schema.Builder schemaBuilder, SakuraWatermark watermark) {
        schemaBuilder.watermark(watermark.getName(), watermark.getExpression());
    }

    public static SakuraUniqueConstraint toConstraint(UniqueConstraint constraint) {
        SakuraUniqueConstraint uniqueConstraint = new SakuraUniqueConstraint();
        uniqueConstraint.setName(constraint.getName());
        uniqueConstraint.setColumns(constraint.getColumns());
        switch (constraint.getType()) {
            case UNIQUE_KEY:
                uniqueConstraint.setType(CatalogConstraintType.UNIQUE_KEY);
                break;
            case PRIMARY_KEY:
                uniqueConstraint.setType(CatalogConstraintType.PRIMARY_KEY);
                break;
            default:
        }
        uniqueConstraint.setEnforced(constraint.isEnforced());
        return uniqueConstraint;
    }

    public static SakuraUniqueConstraint toConstraint(Schema.UnresolvedPrimaryKey constraint) {
        SakuraUniqueConstraint uniqueConstraint = new SakuraUniqueConstraint();
        uniqueConstraint.setName(constraint.getConstraintName());
        uniqueConstraint.setColumns(constraint.getColumnNames());
        uniqueConstraint.setType(CatalogConstraintType.PRIMARY_KEY);
        uniqueConstraint.setEnforced(false);
        return uniqueConstraint;
    }

    public static void addConstraint(Schema.Builder schemaBuilder, SakuraUniqueConstraint constraint) {
        schemaBuilder.primaryKeyNamed(constraint.getName(), constraint.getColumns());
    }

    private static Optional<String> serializeDataType(AbstractDataType<?> type) {
        try {
            if (type instanceof DataType) {
                DataType dataType = (DataType) type;
                return Optional.of(dataType.getLogicalType().asSerializableString());
            }
            return Optional.empty();
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

    private static Optional<String> serializeExpression(Expression expression) {
        try {
            if (expression instanceof SqlCallExpression) {
                return Optional.of(((SqlCallExpression) expression).getSqlExpression());
            }
            return Optional.empty();
        } catch (TableException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
