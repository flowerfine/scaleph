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

package cn.sliew.scaleph.engine.sql.service.impl;

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkCatalog;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkCatalogMapper;
import cn.sliew.scaleph.engine.sql.service.WsFlinkCatalogService;
import cn.sliew.scaleph.engine.sql.service.convert.WsFlinkCatalogConvert;
import cn.sliew.scaleph.engine.sql.service.dto.WsFlinkCatalogDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WsFlinkCatalogServiceImpl implements WsFlinkCatalogService {

    @Autowired
    private WsFlinkCatalogMapper wsFlinkCatalogMapper;

    @Override
    public List<WsFlinkCatalogDTO> list() {
        LambdaQueryWrapper<WsFlinkCatalog> queryWrapper = Wrappers.lambdaQuery(WsFlinkCatalog.class)
                .orderByAsc(WsFlinkCatalog::getType)
                .orderByAsc(WsFlinkCatalog::getId);
        List<WsFlinkCatalog> wsFlinkCatalogs = wsFlinkCatalogMapper.selectList(queryWrapper);
        return WsFlinkCatalogConvert.INSTANCE.toDto(wsFlinkCatalogs);
    }

    @Override
    public WsFlinkCatalogDTO selectOne(Long id) {
        WsFlinkCatalog wsFlinkCatalog = wsFlinkCatalogMapper.selectById(id);
        return WsFlinkCatalogConvert.INSTANCE.toDto(wsFlinkCatalog);
    }

    @Override
    public int insert(WsFlinkCatalogDTO param) {
        WsFlinkCatalog record = WsFlinkCatalogConvert.INSTANCE.toDo(param);
        return wsFlinkCatalogMapper.insert(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkCatalogMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkCatalogMapper.deleteBatchIds(ids);
    }
}
