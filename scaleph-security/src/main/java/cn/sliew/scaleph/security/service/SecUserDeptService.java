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

import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;

/**
 * <p>
 * 用户和部门关联表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecUserDeptService {
    /**
     * 新增用户部门关系
     *
     * @param secUserDeptDTO user dept
     * @return int
     */
    int insert(SecUserDeptDTO secUserDeptDTO);

    /**
     * 根据部门删除
     *
     * @param deptId deptid
     * @return int
     */
    int deleteBydeptId(Serializable deptId);

    /**
     * 删除用户部门关系
     *
     * @param secUserDeptDTO user dept
     * @return int
     */
    int delete(SecUserDeptDTO secUserDeptDTO);

    /**
     * 查询部门下用户列表
     *
     * @param deptId dept
     * @return list
     */
    List<SecUserDeptDTO> listByDeptId(Serializable deptId);
}
