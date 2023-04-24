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

package cn.sliew.scaleph.catalog.service;

import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;

import java.util.List;
import java.util.Optional;

public interface CatalogFunctionService {

    List<CatalogFunctionDTO> selectByDatabase(String catalog, String database);

    int countByDatabase(String catalog, String database);

    Optional<CatalogFunctionDTO> get(String catalog, String database, String name);

    int insert(String catalog, String database, CatalogFunctionDTO param);

    int update(String catalog, String database, CatalogFunctionDTO param);
}