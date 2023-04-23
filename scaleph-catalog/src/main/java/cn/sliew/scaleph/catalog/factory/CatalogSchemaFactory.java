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
import cn.sliew.scaleph.catalog.service.dto.ColumnDTO;
import cn.sliew.scaleph.catalog.service.dto.SchemaDTO;
import cn.sliew.scaleph.catalog.service.dto.UniqueConstraintDTO;
import cn.sliew.scaleph.catalog.service.dto.WatermarkDTO;
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

    public static SchemaDTO toSchema(ResolvedSchema schema) {
        SchemaDTO schemaDTO = new SchemaDTO();
        List<ColumnDTO> columns = schema.getColumns().stream()
                .map(CatalogSchemaFactory::toColumn)
                .collect(Collectors.toList());
        schemaDTO.setColumns(columns);
        List<WatermarkDTO> watermarks = schema.getWatermarkSpecs().stream()
                .map(CatalogSchemaFactory::toWatermark)
                .collect(Collectors.toList());
        schemaDTO.setWatermarks(watermarks);
        schema.getPrimaryKey().map(CatalogSchemaFactory::toConstraint).ifPresent(constraint -> schemaDTO.setPrimaryKey(constraint));
        return schemaDTO;
    }

    public static SchemaDTO toSchema(Schema schema) {
        SchemaDTO schemaDTO = new SchemaDTO();
        List<ColumnDTO> columns = schema.getColumns().stream()
                .map(CatalogSchemaFactory::toColumn)
                .collect(Collectors.toList());
        schemaDTO.setColumns(columns);
        List<WatermarkDTO> watermarks = schema.getWatermarkSpecs().stream()
                .map(CatalogSchemaFactory::toWatermark)
                .collect(Collectors.toList());
        schemaDTO.setWatermarks(watermarks);
        schema.getPrimaryKey().map(CatalogSchemaFactory::toConstraint).ifPresent(constraint -> schemaDTO.setPrimaryKey(constraint));
        return schemaDTO;
    }

    public static Schema toCatalog(SchemaDTO schemaDTO) {
        Schema.Builder builder = Schema.newBuilder();
        Optional.ofNullable(schemaDTO.getColumns()).ifPresent(columns -> columns.stream().forEach(column -> addColumn(builder, column)));
        Optional.ofNullable(schemaDTO.getWatermarks()).ifPresent(watermarks -> watermarks.stream().forEach(watermark -> addWatermark(builder, watermark)));
        Optional.ofNullable(schemaDTO.getPrimaryKey()).ifPresent(primaryKey -> addConstraint(builder, primaryKey));
        return builder.build();
    }

    public static ColumnDTO toColumn(Column column) {
        ColumnDTO columnDTO = new ColumnDTO();
        columnDTO.setName(column.getName());
        serializeDataType(column.getDataType()).ifPresent(dataType -> columnDTO.setDataType(dataType));
        column.getComment().ifPresent(comment -> columnDTO.setComment(comment));
        if (column instanceof Column.PhysicalColumn) {
            columnDTO.setType(CatalogColumnType.PHYSICAL);
        }
        if (column instanceof Column.ComputedColumn) {
            Column.ComputedColumn computedColumn = (Column.ComputedColumn) column;
            columnDTO.setType(CatalogColumnType.COMPUTED);
            columnDTO.setExpression(serializeResolvedExpression(computedColumn.getExpression()));
        }
        if (column instanceof Column.MetadataColumn) {
            Column.MetadataColumn metadataColumn = (Column.MetadataColumn) column;
            columnDTO.setType(CatalogColumnType.METADATA);
            metadataColumn.getMetadataKey().ifPresent(metadataKey -> columnDTO.setMetadataKey(metadataKey));
            columnDTO.setVirtual(metadataColumn.isVirtual());
        }
        return columnDTO;
    }

    public static ColumnDTO toColumn(Schema.UnresolvedColumn column) {
        ColumnDTO columnDTO = new ColumnDTO();
        columnDTO.setName(column.getName());
        column.getComment().ifPresent(comment -> columnDTO.setComment(comment));
        if (column instanceof Schema.UnresolvedPhysicalColumn) {
            Schema.UnresolvedPhysicalColumn physicalColumn = (Schema.UnresolvedPhysicalColumn) column;
            columnDTO.setType(CatalogColumnType.PHYSICAL);
            serializeDataType(physicalColumn.getDataType()).ifPresent(dataType -> columnDTO.setDataType(dataType));
        }
        if (column instanceof Schema.UnresolvedComputedColumn) {
            Schema.UnresolvedComputedColumn computedColumn = (Schema.UnresolvedComputedColumn) column;
            columnDTO.setType(CatalogColumnType.COMPUTED);
            serializeExpression(computedColumn.getExpression()).ifPresent(expression -> columnDTO.setExpression(expression));
        }
        if (column instanceof Schema.UnresolvedMetadataColumn) {
            Schema.UnresolvedMetadataColumn metadataColumn = (Schema.UnresolvedMetadataColumn) column;
            columnDTO.setType(CatalogColumnType.METADATA);
            serializeDataType(metadataColumn.getDataType()).ifPresent(dataType -> columnDTO.setDataType(dataType));
            columnDTO.setMetadataKey(metadataColumn.getMetadataKey());
            columnDTO.setVirtual(metadataColumn.isVirtual());
        }
        return columnDTO;
    }

    public static void addColumn(Schema.Builder schemaBuilder, ColumnDTO column) {
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

    public static WatermarkDTO toWatermark(WatermarkSpec watermark) {
        WatermarkDTO watermarkDTO = new WatermarkDTO();
        watermarkDTO.setName(watermark.getRowtimeAttribute());
        watermarkDTO.setExpression(serializeResolvedExpression(watermark.getWatermarkExpression()));
        return watermarkDTO;
    }

    public static WatermarkDTO toWatermark(Schema.UnresolvedWatermarkSpec watermark) {
        WatermarkDTO watermarkDTO = new WatermarkDTO();
        watermarkDTO.setName(watermark.getColumnName());
        serializeExpression(watermark.getWatermarkExpression()).ifPresent(expression -> watermarkDTO.setExpression(expression));
        return watermarkDTO;
    }

    public static void addWatermark(Schema.Builder schemaBuilder, WatermarkDTO watermark) {
        schemaBuilder.watermark(watermark.getName(), watermark.getExpression());
    }

    public static UniqueConstraintDTO toConstraint(UniqueConstraint constraint) {
        UniqueConstraintDTO uniqueConstraint = new UniqueConstraintDTO();
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

    public static UniqueConstraintDTO toConstraint(Schema.UnresolvedPrimaryKey constraint) {
        UniqueConstraintDTO uniqueConstraint = new UniqueConstraintDTO();
        uniqueConstraint.setName(constraint.getConstraintName());
        uniqueConstraint.setColumns(constraint.getColumnNames());
        uniqueConstraint.setType(CatalogConstraintType.PRIMARY_KEY);
        uniqueConstraint.setEnforced(false);
        return uniqueConstraint;
    }

    public static void addConstraint(Schema.Builder schemaBuilder, UniqueConstraintDTO constraint) {
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
