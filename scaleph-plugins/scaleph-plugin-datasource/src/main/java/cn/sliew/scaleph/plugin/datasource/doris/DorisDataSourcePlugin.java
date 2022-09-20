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

package cn.sliew.scaleph.plugin.datasource.doris;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.doris.DorisProperties.*;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

@AutoService(DatasourcePlugin.class)
public class DorisDataSourcePlugin extends JDBCDataSourcePlugin {

    public DorisDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.DORIS.getValue(), "Doris Jdbc Datasource", DorisDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(PORT);
        props.add(DATABASE_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(HTTP_PORT);

        props.add(JDBC_URL_UNREQUIRED);
        props.add(DRIVER_CLASS_NAME_UNREQUIRED);
        props.add(FENODES);
        props.add(DATABASE);
        props.add(USER);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void configure(PropertyContext props) {
        super.configure(props);
        properties.set(JDBC_URL_UNREQUIRED, getJdbcUrl());
        properties.set(DRIVER_CLASS_NAME_UNREQUIRED, getDriverClassName());
        properties.set(FENODES, getFeNodes());
        properties.set(DATABASE, properties.getString(DATABASE_NAME));
        properties.set(USER, properties.getString(USERNAME));
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mysql://" + properties.getString(HOST) + ":" + properties.getInteger(PORT) + "/" + properties.getString(DATABASE_NAME);
    }

    @Override
    public String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    private String getFeNodes() {
        return properties.getString(HOST) + ":" + properties.getInteger(HTTP_PORT);
    }
}
