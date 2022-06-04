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
    /**
     * 新增字典类型
     *
     * @param sysDictTypeDTO dict type
     * @return int
     */
    int insert(SysDictTypeDTO sysDictTypeDTO);

    /**
     * 更新字典类型
     *
     * @param sysDictTypeDTO dict type
     * @return int
     */
    int update(SysDictTypeDTO sysDictTypeDTO);

    /**
     * 根据主键id删除数据
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 根据主键id批量删除
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据主键id查询
     *
     * @param id id
     * @return DictTypeDTO
     */
    SysDictTypeDTO selectOne(Long id);

    /**
     * 根据dictTypeCode查询
     *
     * @param dictTypeCode dictTypeCode
     * @return DictTypeDTO
     */
    SysDictTypeDTO selectOne(String dictTypeCode);

    /**
     * 分页查询
     *
     * @param sysDictTypeParam 参数
     * @return page
     */
    Page<SysDictTypeDTO> listByPage(SysDictTypeParam sysDictTypeParam);

    /**
     * 查询全部
     *
     * @return list
     */
    List<SysDictTypeDTO> selectAll();

}
