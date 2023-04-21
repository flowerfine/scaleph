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

package cn.sliew.scaleph.catalog.service.impl;

import cn.sliew.scaleph.catalog.service.CatalogDatabaseService;
import cn.sliew.scaleph.catalog.service.convert.CatalogDatabaseConvert;
import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.dao.entity.sakura.CatalogDatabase;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogDatabaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogDatabaseServiceImpl implements CatalogDatabaseService {

    @Autowired
    private CatalogDatabaseMapper catalogDatabaseMapper;

    @Override
    public List<CatalogDatabaseDTO> list(String catalog) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .orderByAsc(CatalogDatabase::getName);
        List<CatalogDatabase> databases = catalogDatabaseMapper.selectList(queryWrapper);
        return CatalogDatabaseConvert.INSTANCE.toDto(databases);
    }

    @Override
    public Optional<CatalogDatabaseDTO> get(String catalog, String name) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .eq(CatalogDatabase::getName, name);
        CatalogDatabase database = catalogDatabaseMapper.selectOne(queryWrapper);
        return Optional.ofNullable(CatalogDatabaseConvert.INSTANCE.toDto(database));
    }

    @Override
    public int insert(CatalogDatabaseDTO param) {
        CatalogDatabase database = CatalogDatabaseConvert.INSTANCE.toDo(param);
        return catalogDatabaseMapper.insert(database);
    }

    @Override
    public int update(CatalogDatabaseDTO param) {
        CatalogDatabase database = CatalogDatabaseConvert.INSTANCE.toDo(param);
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, param.getCatalog())
                .eq(CatalogDatabase::getName, param.getName());
        return catalogDatabaseMapper.update(database, queryWrapper);
    }
}
