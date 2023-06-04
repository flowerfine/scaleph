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
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecPrivilege;
import cn.sliew.scaleph.dao.mapper.master.security.SecPrivilegeMapper;
import cn.sliew.scaleph.security.service.SecPrivilegeService;
import cn.sliew.scaleph.security.service.convert.SecPrivilegeConvert;
import cn.sliew.scaleph.security.service.dto.SecPrivilegeDTO;
import cn.sliew.scaleph.security.service.param.SecPrivilegeAddParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeListParam;
import cn.sliew.scaleph.security.service.param.SecPrivilegeUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
                .eq(SecPrivilege::getPid, param.getPid())
                .like(StringUtils.hasText(param.getPrivilegeName()), SecPrivilege::getPrivilegeName, param.getPrivilegeName());
        Page<SecPrivilege> list = secPrivilegeMapper.selectPage(page, queryWrapper);
        Page<SecPrivilegeDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<SecPrivilegeDTO> dtoList = SecPrivilegeConvert.INSTANCE.toDto(list.getRecords());
        dtoList.forEach(dto -> recurse(dto, param.getPrivilegeName()));
        result.setRecords(dtoList);
        return result;
    }

    private void recurse(SecPrivilegeDTO privilege, String privilegeName) {
        List<SecPrivilegeDTO> children = listByPid(privilege.getId(), privilegeName);
        if (CollectionUtils.isEmpty(children) == false) {
            privilege.setChildren(children);
            children.forEach(child -> recurse(child, privilegeName));
        }
    }

    @Override
    public List<SecPrivilegeDTO> listAll(String resourceType) {
        List<SecPrivilege> list = this.secPrivilegeMapper.selectList(new LambdaQueryWrapper<SecPrivilege>()
                .eq(StrUtil.isNotEmpty(resourceType), SecPrivilege::getResourceType, resourceType));
        return SecPrivilegeConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecPrivilegeDTO> listByPid(Long pid, String privilegeName) {
        LambdaQueryWrapper<SecPrivilege> queryWrapper = Wrappers.lambdaQuery(SecPrivilege.class)
                .eq(SecPrivilege::getPid, pid)
                .like(StringUtils.hasText(privilegeName), SecPrivilege::getPrivilegeName, privilegeName);
        List<SecPrivilege> list = secPrivilegeMapper.selectList(queryWrapper);
        return SecPrivilegeConvert.INSTANCE.toDto(list);
    }

    @Override
    public int insert(SecPrivilegeAddParam param) {
        SecPrivilege record = BeanUtil.copy(param, new SecPrivilege());
        return secPrivilegeMapper.insert(record);
    }

    @Override
    public int update(Long id, SecPrivilegeUpdateParam param) {
        SecPrivilege record = BeanUtil.copy(param, new SecPrivilege());
        record.setId(id);
        return secPrivilegeMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        List<SecPrivilegeDTO> secPrivilegeDTOS = listByPid(id, null);
        for (SecPrivilegeDTO privilege : secPrivilegeDTOS) {
            deleteById(privilege.getPid());
        }
        return secPrivilegeMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        for (Long id : ids) {
            deleteById(id);
        }
        return ids.size();
    }

}
