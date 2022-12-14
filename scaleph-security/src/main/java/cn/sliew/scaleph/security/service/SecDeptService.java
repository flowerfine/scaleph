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

import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import cn.sliew.scaleph.security.service.dto.SecDeptTreeDTO;
import cn.sliew.scaleph.security.service.param.SecDeptListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecDeptService {

    int insert(SecDeptDTO secDeptDTO);

    int update(SecDeptDTO secDeptDTO);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    SecDeptDTO selectOne(Long id);

    SecDeptDTO selectOne(String deptCode);

    List<SecDeptDTO> listAll();

    List<SecDeptDTO> listByDeptId(Long pid);

    Page<SecDeptTreeDTO> listByPage(SecDeptListParam param);

}
