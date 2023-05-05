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

package cn.sliew.scaleph.engine.sql.service;

import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.catalog.CatalogDatabase;
import org.apache.flink.table.catalog.CatalogTable;
import org.apache.flink.table.catalog.CatalogView;

import java.util.List;

public interface CatalogService {

    List<Catalog> listCatalogs();

    Catalog getCatalog(String catalog);

    List<CatalogDatabase> listDatabases(String catalog);

    CatalogDatabase getDatabase(String catalog, String database);

    List<CatalogTable> listTables(String catalog, String database);

    CatalogTable getTable(String catalog, String database, String table);

    List<CatalogView> listViews(String catalog, String database);

    CatalogView getView(String catalog, String database, String view);
}
