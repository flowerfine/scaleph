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

import cn.sliew.scaleph.catalog.service.CatalogTableService;
import cn.sliew.scaleph.catalog.service.dto.CatalogTableDTO;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogTableMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogTableServiceImpl implements CatalogTableService {

    @Autowired
    private CatalogTableMapper catalogTableMapper;

    @Override
    public List<CatalogTableDTO> selectByDatabase(String catalog, String database) {
        
        return null;
    }

    @Override
    public int countByDatabase(String catalog, String database) {
        return 0;
    }

    @Override
    public Optional<CatalogTableDTO> get(String catalog, String database, String name) {
        return Optional.empty();
    }

    @Override
    public int insert(String catalog, String database, CatalogTableDTO param) {
        return 0;
    }

    @Override
    public int update(String catalog, String database, CatalogTableDTO param) {
        return 0;
    }
}
