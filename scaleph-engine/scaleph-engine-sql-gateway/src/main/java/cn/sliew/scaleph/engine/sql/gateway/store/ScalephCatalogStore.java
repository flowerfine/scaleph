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

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.catalog.CatalogDescriptor;
import org.apache.flink.table.gateway.api.session.SessionHandle;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkSqlGatewayCatalog;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkSqlGatewayCatalogMapper;

public class ScalephCatalogStore extends JdbcCatalogStore<WsFlinkSqlGatewayCatalog, WsFlinkSqlGatewayCatalogMapper> {

    private final SessionHandle sessionHandle;

    public ScalephCatalogStore(
            SessionHandle sessionHandle, String driver, String jdbcUrl, String username, String password) {
        super(driver, jdbcUrl, username, password);
        this.sessionHandle = sessionHandle;
    }

    @Override
    public String[] getMapperPackages() {
        return new String[] {"cn/sliew/scaleph/dao/mapper/master/ws/WsFlinkSqlGatewayCatalogMapper.xml"};
    }

    @Override
    public Class<WsFlinkSqlGatewayCatalogMapper> getMapperClass() {
        return WsFlinkSqlGatewayCatalogMapper.class;
    }

    @Override
    public String getCatalogName(WsFlinkSqlGatewayCatalog wsFlinkSqlGatewayCatalog) {
        return wsFlinkSqlGatewayCatalog.getCatalogName();
    }

    @Override
    public WsFlinkSqlGatewayCatalog fromDescriptor(String catalogName, CatalogDescriptor catalogDescriptor) {
        Map<String, String> map = catalogDescriptor.getConfiguration().toMap();
        return WsFlinkSqlGatewayCatalog.builder()
                .sessionHandler(this.sessionHandle.toString())
                .catalogDescription(JacksonUtil.toJsonString(map))
                .build();
    }

    @Override
    public CatalogDescriptor toDescriptor(WsFlinkSqlGatewayCatalog wsFlinkSqlGatewayCatalog) {
        String catalogOptions = wsFlinkSqlGatewayCatalog.getCatalogOptions();
        Map<String, String> optionsMap =
                JacksonUtil.parseJsonString(catalogOptions, new TypeReference<HashMap<String, String>>() {});
        return CatalogDescriptor.of(wsFlinkSqlGatewayCatalog.getCatalogName(), Configuration.fromMap(optionsMap));
    }

    @Override
    public Wrapper<WsFlinkSqlGatewayCatalog> buildWrapper(String catalogName) {
        LambdaQueryWrapper<WsFlinkSqlGatewayCatalog> wrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewayCatalog.class)
                .eq(WsFlinkSqlGatewayCatalog::getSessionHandler, sessionHandle.toString());
        if (catalogName != null) {
            wrapper.eq(WsFlinkSqlGatewayCatalog::getCatalogName, catalogName);
        }
        return wrapper;
    }
}
