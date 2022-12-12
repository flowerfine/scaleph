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
import cn.sliew.scaleph.dao.entity.master.security.SecDept;
import cn.sliew.scaleph.dao.mapper.master.security.SecDeptMapper;
import cn.sliew.scaleph.security.service.SecDeptRoleService;
import cn.sliew.scaleph.security.service.SecDeptService;
import cn.sliew.scaleph.security.service.SecUserDeptService;
import cn.sliew.scaleph.security.service.convert.SecDeptConvert;
import cn.sliew.scaleph.security.service.dto.SecDeptDTO;
import cn.sliew.scaleph.security.service.dto.SecDeptTreeDTO;
import cn.sliew.scaleph.security.service.param.SecDeptListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecDeptServiceImpl implements SecDeptService {

    @Autowired
    private SecDeptMapper secDeptMapper;
    @Autowired
    private SecUserDeptService secUserDeptService;
    @Autowired
    private SecDeptRoleService secDeptRoleService;

    @Override
    public int insert(SecDeptDTO secDeptDTO) {
        SecDept secDept = SecDeptConvert.INSTANCE.toDo(secDeptDTO);
        return secDeptMapper.insert(secDept);
    }

    @Override
    public int update(SecDeptDTO secDeptDTO) {
        SecDept secDept = SecDeptConvert.INSTANCE.toDo(secDeptDTO);
        return secDeptMapper.updateById(secDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        List<SecDeptDTO> secDeptDTOS = listByDeptId(id);
        for (SecDeptDTO deptDTO : secDeptDTOS) {
            deleteById(deptDTO.getId());
        }
        secUserDeptService.deleteBydeptId(id);
        secDeptRoleService.deleteByDeptId(id);
        return secDeptMapper.deleteById(id);
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
    public SecDeptDTO selectOne(Long id) {
        SecDept secDept = secDeptMapper.selectById(id);
        return SecDeptConvert.INSTANCE.toDto(secDept);
    }

    @Override
    public SecDeptDTO selectOne(String deptCode) {
        SecDept secDept = secDeptMapper.selectOne(new LambdaQueryWrapper<SecDept>()
                .eq(SecDept::getDeptCode, deptCode));
        return SecDeptConvert.INSTANCE.toDto(secDept);
    }

    @Override
    public List<SecDeptDTO> listAll() {
        List<SecDept> secDeptList = secDeptMapper.selectList(null);
        return SecDeptConvert.INSTANCE.toDto(secDeptList);
    }

    @Override
    public List<SecDeptDTO> listByDeptId(Long pid) {
        List<SecDept> secDeptList = secDeptMapper.selectList(new LambdaQueryWrapper<SecDept>()
                .eq(SecDept::getPid, pid));
        return SecDeptConvert.INSTANCE.toDto(secDeptList);
    }

    @Override
    public Page<SecDeptTreeDTO> listByPage(SecDeptListParam param) {
        Page<SecDept> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<SecDept> queryWrapper = Wrappers.lambdaQuery(SecDept.class)
                .eq(SecDept::getPid, param.getPid())
                .eq(StringUtils.hasText(param.getDeptCode()), SecDept::getDeptCode, param.getDeptCode())
                .like(StringUtils.hasText(param.getDeptName()), SecDept::getDeptName, param.getDeptName());
        Page<SecDept> list = secDeptMapper.selectPage(page, queryWrapper);
        Page<SecDeptTreeDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<SecDeptTreeDTO> dtoList = SecDeptConvert.INSTANCE.entity2TreeDTO(list.getRecords());
        dtoList.forEach(dto -> recurse(dto));
        result.setRecords(dtoList);
        return result;
    }

    private void recurse(SecDeptTreeDTO deptTreeDTO) {
        List<SecDeptDTO> secDeptDTOS = listByDeptId(deptTreeDTO.getId());
        List<SecDeptTreeDTO> children = SecDeptConvert.INSTANCE.dto2TreeDTO(secDeptDTOS);
        if (CollectionUtils.isEmpty(children) == false) {
            deptTreeDTO.setChildren(children);
            children.forEach(child -> recurse(child));
        }
    }
}
