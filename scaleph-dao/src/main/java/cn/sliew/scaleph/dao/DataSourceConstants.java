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

package cn.sliew.scaleph.dao;

public enum DataSourceConstants {
    ;

    public static final String MASTER_ENTITY_PACKAGE = "cn.sliew.scaleph.dao.entity.master";
    public static final String MASTER_MAPPER_PACKAGE = "cn.sliew.scaleph.dao.mapper.master";
    public static final String MASTER_MAPPER_XML_PATH =
            "classpath*:cn.sliew.scaleph.dao.mapper/master/**/*.xml";

    public static final String MASTER_SQL_SESSION_FACTORY = "masterSqlSessionFactory";
    public static final String MASTER_DATA_SOURCE_FACTORY = "masterDataSource";
    public static final String MASTER_TRANSACTION_MANAGER_FACTORY = "masterTransactionManager";

    public static final String LOG_ENTITY_PACKAGE = "cn.sliew.scaleph.dao.entity.log";
    public static final String LOG_MAPPER_PACKAGE = "cn.sliew.scaleph.dao.mapper.log";
    public static final String LOG_MAPPER_XML_PATH = "classpath*:cn.sliew.scaleph.dao.mapper/log/**/*.xml";

    public static final String LOG_SQL_SESSION_FACTORY = "logSqlSessionFactory";
    public static final String LOG_DATA_SOURCE_FACTORY = "logDataSource";
    public static final String LOG_TRANSACTION_MANAGER_FACTORY = "logTransactionManager";
}
