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

package cn.sliew.scaleph.security.service;

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.security.service.dto.SecDeptDTO;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecDeptService {
    /**
     * 新增部门
     *
     * @param secDeptDTO dept
     * @return int
     */
    int insert(SecDeptDTO secDeptDTO);

    /**
     * 更新部门
     *
     * @param secDeptDTO dept
     * @return int
     */
    int update(SecDeptDTO secDeptDTO);

    /**
     * 删除部门
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 批量删除部门
     *
     * @param list ids
     * @return int
     */
    int deleteBatch(List<? extends Serializable> list);

    /**
     * 根据id查询
     *
     * @param id dept id
     * @return dept
     */
    SecDeptDTO selectOne(Long id);

    /**
     * 根据部门编码查询
     *
     * @param deptCode deptCode
     * @return dept
     */
    SecDeptDTO selectOne(String deptCode);

    /**
     * 查询全部部门信息
     *
     * @return all dept
     */
    List<SecDeptDTO> listAll();

    /**
     * 根据id查询下属一级部门信息
     *
     * @param pid dept pid
     * @return dept list
     */
    List<SecDeptDTO> listByDeptId(Long pid);
}
