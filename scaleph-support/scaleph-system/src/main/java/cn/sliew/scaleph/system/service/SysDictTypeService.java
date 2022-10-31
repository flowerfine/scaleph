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

package cn.sliew.scaleph.system.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.system.service.dto.SysDictTypeDTO;
import cn.sliew.scaleph.system.service.param.SysDictTypeParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据字典类型 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
public interface SysDictTypeService {

    int insert(SysDictTypeDTO sysDictTypeDTO);

    int update(SysDictTypeDTO sysDictTypeDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    SysDictTypeDTO selectOne(Long id);

    SysDictTypeDTO selectOne(String dictTypeCode);

    Page<SysDictTypeDTO> listByPage(SysDictTypeParam sysDictTypeParam);

    List<SysDictTypeDTO> selectAll();

}
