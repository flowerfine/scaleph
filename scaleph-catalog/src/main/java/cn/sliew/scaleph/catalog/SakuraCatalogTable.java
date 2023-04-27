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

package cn.sliew.scaleph.catalog;

import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.CatalogTable;

import java.util.*;

/**
 * @see org.apache.flink.table.catalog.DefaultCatalogTable
 */
public class SakuraCatalogTable implements CatalogTable {

    private final Schema schema;
    private final Map<String, String> options;
    private final String comment;

    public SakuraCatalogTable(Schema schema, Map<String, String> options, String comment) {
        this.schema = schema;
        this.options = options;
        this.comment = comment;
    }

    @Override
    public boolean isPartitioned() {
        return false;
    }

    @Override
    public List<String> getPartitionKeys() {
        return Collections.emptyList();
    }

    @Override
    public CatalogTable copy(Map<String, String> options) {
        return new SakuraCatalogTable(schema, new HashMap<>(options), comment);
    }

    @Override
    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public CatalogBaseTable copy() {
        return new SakuraCatalogTable(schema, new HashMap<>(options), comment);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(comment);
    }

    @Override
    public Optional<String> getDetailedDescription() {
        return Optional.empty();
    }
}
