/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.sql.gateway.store;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.catalog.CatalogStore;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.flink.table.factories.CatalogStoreFactory;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.gateway.api.session.SessionHandle;

import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.DRIVER;
import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.IDENTIFIER;
import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.JDBC_URL;
import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.PASSWORD;
import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.SESSION_HANDLE;
import static cn.sliew.scaleph.engine.sql.gateway.store.ScalephCatalogStoreOptions.USERNAME;

public class ScalephCatalogStoreFactory implements CatalogStoreFactory {

    private SessionHandle sessionHandle;
    private String driver;
    private String jdbcUrl;
    private String username;
    private String password;

    @Override
    public CatalogStore createCatalogStore() {
        return new ScalephCatalogStore(sessionHandle, driver, jdbcUrl, username, password);
    }

    @Override
    public void open(Context context) throws CatalogException {
        FactoryUtil.CatalogStoreFactoryHelper catalogStoreFactoryHelper =
                FactoryUtil.createCatalogStoreFactoryHelper(this, context);
        catalogStoreFactoryHelper.validate();
        ReadableConfig options = catalogStoreFactoryHelper.getOptions();
        String sessionHandleStr = options.get(SESSION_HANDLE);
        this.sessionHandle = new SessionHandle(UUID.fromString(sessionHandleStr));
        this.driver = options.get(DRIVER);
        this.jdbcUrl = options.get(JDBC_URL);
        this.username = options.get(USERNAME);
        this.password = options.get(PASSWORD);
    }

    @Override
    public void close() throws CatalogException {}

    @Override
    public String factoryIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Set<ConfigOption<?>> requiredOptions() {
        Set<ConfigOption<?>> options = new HashSet<>();
        options.add(SESSION_HANDLE);
        options.add(DRIVER);
        options.add(JDBC_URL);
        options.add(USERNAME);
        options.add(PASSWORD);
        return options;
    }

    @Override
    public Set<ConfigOption<?>> optionalOptions() {
        return Collections.emptySet();
    }
}
