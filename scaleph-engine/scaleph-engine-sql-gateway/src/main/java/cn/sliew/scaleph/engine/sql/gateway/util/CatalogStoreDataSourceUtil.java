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

package cn.sliew.scaleph.engine.sql.gateway.util;

import java.io.InputStream;
import java.util.Date;
import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.LanguageDriverRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.zaxxer.hikari.HikariDataSource;

public class CatalogStoreDataSourceUtil {

    private CatalogStoreDataSourceUtil() {}

    public static HikariDataSource createDataSource(String driver, String jdbcUrl, String username, String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaximumPoolSize(20);
        dataSource.setConnectionTimeout(100000);
        dataSource.setMinimumIdle(1);
        dataSource.setIdleTimeout(60000);
        dataSource.setConnectionInitSql("SELECT 1 FROM DUAL");
        dataSource.setConnectionTestQuery("SELECT 1 FROM DUAL");
        return dataSource;
    }

    public static SqlSessionFactory createSqlSessionFactory(DataSource dataSource, String... mapperPackages) {
        try {
            MybatisConfiguration configuration = new MybatisConfiguration();
            LanguageDriverRegistry languageRegistry = configuration.getLanguageRegistry();
            languageRegistry.register(MybatisXMLLanguageDriver.class);
            languageRegistry.setDefaultDriverClass(MybatisXMLLanguageDriver.class);
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setLogImpl(Slf4jImpl.class);
            Environment environment = new Environment("CatalogStore", new JdbcTransactionFactory(), dataSource);
            configuration.setEnvironment(environment);
            configuration.setCacheEnabled(false);
            for (String mapperPackage : mapperPackages) {
                try (InputStream inputStream = Resources.getResourceAsStream(mapperPackage)) {
                    new XMLMapperBuilder(inputStream, configuration, mapperPackage, configuration.getSqlFragments())
                            .parse();
                }
            }
            GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);
            globalConfig.setMetaObjectHandler(new MetaObjectHandler() {
                public void insertFill(MetaObject metaObject) {
                    this.strictInsertFill(metaObject, "createTime", Date::new, Date.class);
                    this.strictInsertFill(metaObject, "updateTime", Date::new, Date.class);
                }

                public void updateFill(MetaObject metaObject) {
                    this.strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
                }
            });
            return new DefaultSqlSessionFactory(configuration);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
