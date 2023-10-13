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

package cn.sliew.scaleph.engine.sql.gateway.services.dto;

import cn.sliew.scaleph.system.model.BaseDTO;
import lombok.Data;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.catalog.CatalogManager;
import org.apache.flink.table.catalog.FunctionCatalog;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.module.ModuleManager;
import org.apache.flink.table.operations.ModifyOperation;
import org.apache.flink.table.resource.ResourceManager;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * org.apache.flink.table.gateway.service.session.Session
 */
@Data
public class FlinkSqlGatewaySession extends BaseDTO {

    private SessionHandle sessionId;

    private DefaultContext defaultContext;
    private Configuration sessionConfig;

    private URLClassLoader userClassloader;
    // private SessionContext.SessionState sessionState;
    private CatalogManager catalogManager;
    private ResourceManager resourceManager;
    private FunctionCatalog functionCatalog;
    private ModuleManager moduleManager;

    private boolean isStatementSetState;
    private List<ModifyOperation> statementSetOperations = new ArrayList<>();
}
