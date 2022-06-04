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

import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.param.SysDictParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
public interface SysDictService {
    /**
     * 新增
     *
     * @param sysDictDTO dict
     * @return int
     */
    int insert(SysDictDTO sysDictDTO);

    /**
     * 修改
     *
     * @param sysDictDTO dict
     * @return int
     */
    int update(SysDictDTO sysDictDTO);

    /**
     * 根据主键id删除
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
     * 根据类型删除
     *
     * @param dictCodeType dictCodeType
     * @return int
     */
    int deleteByType(String dictCodeType);

    /**
     * 根据主键id查询
     *
     * @param id id
     * @return DictDTO
     */
    SysDictDTO selectOne(Long id);

    /**
     * 根据dictTypeCode查询
     *
     * @param dictTypeCode dictTypeCode
     * @return DictTypeDTO
     */
    List<SysDictDTO> selectByType(String dictTypeCode);

    /**
     * 查询全部
     *
     * @return list
     */
    List<SysDictDTO> selectAll();

    /**
     * 分页查询
     *
     * @param param 参数
     * @return page
     */
    Page<SysDictDTO> listByPage(SysDictParam param);
}
