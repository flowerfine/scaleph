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

import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import org.apache.flink.table.catalog.CatalogDatabase;
import org.apache.flink.table.catalog.CatalogDatabaseImpl;

public enum CatalogDatabaseFactory {
    ;

    public static CatalogDatabaseDTO toDatabase(String catalogName, String name, CatalogDatabase catalog) {
        CatalogDatabaseDTO database = new CatalogDatabaseDTO();
        database.setCatalog(catalogName);
        database.setName(name);
        database.setProperties(catalog.getProperties());
        database.setRemark(catalog.getComment());
        return database;
    }

    public static CatalogDatabase toCatalog(CatalogDatabaseDTO database) {
        return new CatalogDatabaseImpl(database.getProperties(), database.getRemark());
    }

}
