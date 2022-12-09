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

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecPrivilege;
import cn.sliew.scaleph.dao.mapper.master.security.SecPrivilegeMapper;
import cn.sliew.scaleph.security.service.SecPrivilegeService;
import cn.sliew.scaleph.security.service.convert.SecPrivilegeConvert;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.param.SecPrivilegeListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecPrivilegeServiceImpl implements SecPrivilegeService {

    @Autowired
    private SecPrivilegeMapper secPrivilegeMapper;

    @Override
    public Page<SecPrivilegeDTO> listByPage(SecPrivilegeListParam param) {
        Page<SecPrivilege> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<SecPrivilege> queryWrapper = Wrappers.lambdaQuery(SecPrivilege.class)
                .like(StringUtils.hasText(param.getPrivilegeName()), SecPrivilege::getPrivilegeName, param.getPrivilegeName());
        Page<SecPrivilege> list = secPrivilegeMapper.selectPage(page, queryWrapper);
        Page<SecPrivilegeDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<SecPrivilegeDTO> dtoList = SecPrivilegeConvert.INSTANCE.toDto(list.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<SecPrivilegeDTO> listAll(String resourceType) {
        List<SecPrivilege> list = this.secPrivilegeMapper.selectList(new LambdaQueryWrapper<SecPrivilege>()
                .eq(StrUtil.isNotEmpty(resourceType), SecPrivilege::getResourceType, resourceType));
        return SecPrivilegeConvert.INSTANCE.toDto(list);
    }

    @Override
    public int insert(SecPrivilegeDTO param) {
        SecPrivilege record = SecPrivilegeConvert.INSTANCE.toDo(param);
        return secPrivilegeMapper.insert(record);
    }

    @Override
    public int update(SecPrivilegeDTO param) {
        SecPrivilege record = SecPrivilegeConvert.INSTANCE.toDo(param);
        return secPrivilegeMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return secPrivilegeMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return secPrivilegeMapper.deleteBatchIds(ids);
    }

}
