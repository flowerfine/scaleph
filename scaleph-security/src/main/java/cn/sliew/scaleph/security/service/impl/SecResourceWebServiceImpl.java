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

package cn.sliew.scaleph.security.service.impl;

import cn.sliew.scaleph.common.dict.security.ResourceType;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecResourceWeb;
import cn.sliew.scaleph.security.service.SecResourceWebService;
import cn.sliew.scaleph.security.service.convert.SecResourceWebConvert;
import cn.sliew.scaleph.security.service.dto.SecResourceWebDTO;
import cn.sliew.scaleph.security.service.param.SecResourceWebAddParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebListParam;
import cn.sliew.scaleph.security.service.param.SecResourceWebUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

//@Service
public class SecResourceWebServiceImpl implements SecResourceWebService {
    @Override
    public Page<SecResourceWebDTO> listByPage(SecResourceWebListParam param) {
        return null;
    }

    @Override
    public List<SecResourceWebDTO> listAll(ResourceType resourceType) {
        return null;
    }

    @Override
    public List<SecResourceWebDTO> listByPid(Long pid, String name) {
        return null;
    }

    @Override
    public List<SecResourceWebDTO> listByPidAndUserId(Long pid, Long userId, String name) {
        return null;
    }

    @Override
    public int insert(SecResourceWebAddParam param) {
        return 0;
    }

    @Override
    public int update(SecResourceWebUpdateParam param) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return 0;
    }
}
