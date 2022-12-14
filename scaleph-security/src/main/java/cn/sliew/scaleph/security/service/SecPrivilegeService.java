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

import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.param.SecPrivilegeAddParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeListParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface SecPrivilegeService {

    Page<SecPrivilegeDTO> listByPage(SecPrivilegeListParam param);

    List<SecPrivilegeDTO> listAll(String resourceType);

    List<SecPrivilegeDTO> listByPid(Long pid, String privilegeName);

    int insert(SecPrivilegeAddParam param);

    int update(Long id, SecPrivilegeUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

}
