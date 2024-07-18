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

import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.security.SecRole;
import cn.sliew.scaleph.dao.mapper.master.security.SecRoleMapper;
import cn.sliew.scaleph.security.service.SecDeptRoleService;
import cn.sliew.scaleph.security.service.SecRolePrivilegeService;
import cn.sliew.scaleph.security.service.SecRoleService;
import cn.sliew.scaleph.security.service.SecUserRoleService;
import cn.sliew.scaleph.security.service.convert.SecRoleConvert;
import cn.sliew.scaleph.security.service.dto.SecRoleDTO;
import cn.sliew.scaleph.security.service.param.SecRoleAddParam;
import cn.sliew.scaleph.security.service.param.SecRoleListParam;
import cn.sliew.scaleph.security.service.param.SecRoleUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
//@Service
public class SecRoleServiceImpl implements SecRoleService {

    @Autowired
    private SecRoleMapper secRoleMapper;
    @Autowired
    private SecUserRoleService secUserRoleService;
    @Autowired
    private SecRolePrivilegeService secRolePrivilegeService;
    @Autowired
    private SecDeptRoleService secDeptRoleService;

    @Override
    public int insert(SecRoleAddParam param) {
        SecRole record = new SecRole();
        BeanUtils.copyProperties(param, record);
        return this.secRoleMapper.insert(record);
    }

    @Override
    public int update(SecRoleUpdateParam param) {
        SecRole record = new SecRole();
        BeanUtils.copyProperties(param, record);
        return this.secRoleMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        this.secUserRoleService.deleteByRoleId(id);
        this.secRolePrivilegeService.deleteByRoleId(id);
        this.secDeptRoleService.deleteByRoleId(id);
        return this.secRoleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        for (Long id : ids) {
            deleteById(id);
        }
        return ids.size();
    }

    @Override
    public SecRoleDTO selectOne(Long id) {
        SecRole secRole = this.secRoleMapper.selectById(id);
        return SecRoleConvert.INSTANCE.toDto(secRole);
    }

    @Override
    public SecRoleDTO selectOne(String roleCode) {
        SecRole secRole = this.secRoleMapper.selectOne(
                new LambdaQueryWrapper<SecRole>().eq(SecRole::getCode, roleCode));
        return SecRoleConvert.INSTANCE.toDto(secRole);
    }

    @Override
    public List<SecRoleDTO> listAll() {
        List<SecRole> list = this.secRoleMapper.selectList(
                new LambdaQueryWrapper<SecRole>().orderByAsc(SecRole::getId));
        return SecRoleConvert.INSTANCE.toDto(list);
    }

    @Override
    public Page<SecRoleDTO> listByPage(SecRoleListParam param) {
        Page<SecRole> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<SecRole> queryWrapper = Wrappers.lambdaQuery(SecRole.class)
                .eq(param.getType() != null, SecRole::getType, param.getType())
                .eq(param.getStatus() != null, SecRole::getStatus, param.getStatus())
                .like(StringUtils.hasText(param.getName()), SecRole::getName, param.getName())
                .orderByAsc(SecRole::getId);
        Page<SecRole> secRolePage = secRoleMapper.selectPage(page, queryWrapper);
        Page<SecRoleDTO> result = new Page<>(secRolePage.getCurrent(), secRolePage.getSize(), secRolePage.getTotal());
        List<SecRoleDTO> secRoleDTOS = SecRoleConvert.INSTANCE.toDto(secRolePage.getRecords());
        result.setRecords(secRoleDTOS);
        return result;
    }

    @Override
    public List<SecRoleDTO> selectRoleByDept(String grant, Long deptId) {
        List<SecRole> list = this.secRoleMapper.selectRoleByDept(grant, deptId);
        return SecRoleConvert.INSTANCE.toDto(list);
    }

}
