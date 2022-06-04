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

package cn.sliew.scaleph.meta.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.meta.service.dto.DataSourceMetaDTO;
import cn.sliew.scaleph.meta.service.param.DataSourceMetaParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 元数据-数据源连接信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-17
 */

public interface DataSourceMetaService {

    int insert(DataSourceMetaDTO metaDTO);

    int update(DataSourceMetaDTO metaDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    DataSourceMetaDTO selectOne(Serializable id);

    Page<DataSourceMetaDTO> listByPage(DataSourceMetaParam param);

    List<DataSourceMetaDTO> listByType(String type);
}
