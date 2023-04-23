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

import cn.sliew.scaleph.catalog.model.SakuraCatalogTable;
import cn.sliew.scaleph.dao.entity.sakura.CatalogTable;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.ResolvedCatalogTable;

public enum CatalogTableFactory {
    ;

    public static SakuraCatalogTable toResolvedTable(String tableName, CatalogBaseTable catalogBaseTable) {
        SakuraCatalogTable catalogTable = new SakuraCatalogTable();
        catalogTable.setName(tableName);
        catalogTable.setProperties(catalogBaseTable.getOptions());
        catalogTable.setComment(catalogBaseTable.getComment());
        if (catalogBaseTable instanceof ResolvedCatalogTable) {
            ResolvedCatalogTable resolvedTable = (ResolvedCatalogTable) catalogBaseTable;
            catalogTable.setSchema(CatalogSchemaFactory.toSchema(resolvedTable.getResolvedSchema()));
        }
        return catalogTable;
    }

    public static SakuraCatalogTable toUnresolvedTable(String tableName, CatalogBaseTable catalogBaseTable) {
        SakuraCatalogTable catalogTable = new SakuraCatalogTable();
        catalogTable.setName(tableName);
        catalogTable.setProperties(catalogBaseTable.getOptions());
        catalogTable.setComment(catalogBaseTable.getComment());
        catalogTable.setSchema(CatalogSchemaFactory.toSchema(catalogBaseTable.getUnresolvedSchema()));
        return catalogTable;
    }

    public static CatalogTable toCatalog(SakuraCatalogTable sakuraCatalogTable) {

        return null;
    }

}
