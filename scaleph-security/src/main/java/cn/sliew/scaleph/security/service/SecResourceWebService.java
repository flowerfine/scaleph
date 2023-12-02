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

import cn.sliew.scaleph.common.dict.security.ResourceType;
import cn.sliew.scaleph.security.service.dto.SecResourceWebDTO;
import cn.sliew.scaleph.security.service.param.SecResourceWebAddParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebListParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface SecResourceWebService {

    Page<SecResourceWebDTO> listByPage(SecResourceWebListParam param);

    List<SecResourceWebDTO> listAll(ResourceType resourceType);

    List<SecResourceWebDTO> listByPid(Long pid, String name);

    List<SecResourceWebDTO> listByPidAndUserId(Long pid, Long userId, String name);

    int insert(SecResourceWebAddParam param);

    int update(SecResourceWebUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

}
