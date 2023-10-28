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

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.flink.table.catalog.CatalogDescriptor;
import org.apache.flink.table.catalog.CatalogStore;
import org.apache.flink.table.catalog.exceptions.CatalogException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaxxer.hikari.HikariDataSource;

import cn.sliew.scaleph.engine.sql.gateway.util.CatalogStoreDataSourceUtil;

public abstract class JdbcCatalogStore<DTO, MAPPER extends BaseMapper<DTO>> implements CatalogStore {

    private final String driver;
    private final String jdbcUrl;
    private final String username;

    private final String password;

    private HikariDataSource dataSource;

    private SqlSessionFactory sqlSessionFactory;

    public JdbcCatalogStore(String driver, String jdbcUrl, String username, String password) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public abstract String[] getMapperPackages();

    public abstract Class<MAPPER> getMapperClass();

    public abstract String getCatalogName(DTO dto);

    public abstract DTO fromDescriptor(String catalogName, CatalogDescriptor catalogDescriptor);

    public abstract CatalogDescriptor toDescriptor(DTO dto);

    public abstract Wrapper<DTO> buildWrapper(String catalogName);

    protected <R> R doAction(Function<MAPPER, R> action) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            R result = action.apply(sqlSession.getMapper(getMapperClass()));
            sqlSession.commit();
            return result;
        }
    }

    protected void doAction(Consumer<MAPPER> consumer) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            MAPPER mapper = sqlSession.getMapper(getMapperClass());
            consumer.accept(mapper);
            sqlSession.commit();
        }
    }

    @Override
    public void open() throws CatalogException {
        this.dataSource = CatalogStoreDataSourceUtil.createDataSource(driver, jdbcUrl, username, password);
        this.sqlSessionFactory = CatalogStoreDataSourceUtil.createSqlSessionFactory(dataSource, getMapperPackages());
    }

    @Override
    public void close() throws CatalogException {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

    @Override
    public void storeCatalog(String catalogName, CatalogDescriptor catalog) throws CatalogException {
        doAction(mapper -> {
            mapper.insert(fromDescriptor(catalogName, catalog));
        });
    }

    @Override
    public void removeCatalog(String catalogName, boolean ignoreIfNotExists) throws CatalogException {
        if (!contains(catalogName) && !ignoreIfNotExists) {
            throw new CatalogException("Catalog " + catalogName + " not exists!");
        }
        doAction(mapper -> {
            Wrapper<DTO> wrapper = buildWrapper(catalogName);
            mapper.delete(wrapper);
        });
    }

    @Override
    public Optional<CatalogDescriptor> getCatalog(String catalogName) throws CatalogException {
        DTO dto = doAction((Function<MAPPER, DTO>) mapper -> mapper.selectOne(buildWrapper(catalogName)));
        if (dto == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(toDescriptor(dto));
    }

    @Override
    public Set<String> listCatalogs() throws CatalogException {
        return doAction(mapper -> {
            Wrapper<DTO> wrapper = buildWrapper(null);
            return mapper.selectList(wrapper).stream().map(this::getCatalogName).collect(Collectors.toSet());
        });
    }

    @Override
    public boolean contains(String catalogName) throws CatalogException {
        return doAction(mapper -> {
            return mapper.exists(buildWrapper(catalogName));
        });
    }
}
