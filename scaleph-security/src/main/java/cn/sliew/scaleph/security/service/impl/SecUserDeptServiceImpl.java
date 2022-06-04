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

import java.io.Serializable;
import java.util.List;

import cn.sliew.scaleph.dao.entity.master.security.SecUserDept;
import cn.sliew.scaleph.dao.mapper.master.security.SecUserDeptMapper;
import cn.sliew.scaleph.security.service.SecUserDeptService;
import cn.sliew.scaleph.security.service.convert.SecUserDeptConvert;
import cn.sliew.scaleph.security.service.dto.SecUserDeptDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和部门关联表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class SecUserDeptServiceImpl implements SecUserDeptService {

    @Autowired
    private SecUserDeptMapper secUserDeptMapper;

    @Override
    public int insert(SecUserDeptDTO secUserDeptDTO) {
        SecUserDept secUserDept = SecUserDeptConvert.INSTANCE.toDo(secUserDeptDTO);
        return this.secUserDeptMapper.insert(secUserDept);
    }

    @Override
    public int deleteBydeptId(Serializable deptId) {
        return this.secUserDeptMapper.delete(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, deptId));
    }

    @Override
    public int delete(SecUserDeptDTO secUserDeptDTO) {
        return this.secUserDeptMapper.delete(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, secUserDeptDTO.getDeptId())
            .eq(SecUserDept::getUserId, secUserDeptDTO.getUserId())
        );
    }

    @Override
    public List<SecUserDeptDTO> listByDeptId(Serializable deptId) {
        List<SecUserDept> list = this.secUserDeptMapper.selectList(new LambdaQueryWrapper<SecUserDept>()
            .eq(SecUserDept::getDeptId, deptId));
        return SecUserDeptConvert.INSTANCE.toDto(list);
    }
}
