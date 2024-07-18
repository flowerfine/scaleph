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
import cn.sliew.scaleph.dao.mapper.master.security.SecResourceWebMapper;
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

    @Autowired
    private SecResourceWebMapper secResourceWebMapper;

    @Override
    public Page<SecResourceWebDTO> listByPage(SecResourceWebListParam param) {
        Page<SecResourceWeb> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<SecResourceWeb> queryWrapper = Wrappers.lambdaQuery(SecResourceWeb.class)
                .eq(SecResourceWeb::getPid, param.getPid())
                .like(StringUtils.hasText(param.getName()), SecResourceWeb::getName, param.getName());
        Page<SecResourceWeb> list = secResourceWebMapper.selectPage(page, queryWrapper);
        Page<SecResourceWebDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<SecResourceWebDTO> dtoList = SecResourceWebConvert.INSTANCE.toDto(list.getRecords());
        dtoList.forEach(dto -> recurse(dto, param.getName()));
        result.setRecords(dtoList);
        return result;
    }

    private void recurse(SecResourceWebDTO resourceWebDTO, String name) {
        List<SecResourceWebDTO> children = listByPid(resourceWebDTO.getId(), name);
        if (CollectionUtils.isEmpty(children) == false) {
            resourceWebDTO.setChildren(children);
            children.forEach(child -> recurse(child, name));
        }
    }

    @Override
    public List<SecResourceWebDTO> listAll(ResourceType resourceType) {
        List<SecResourceWeb> list = secResourceWebMapper.selectList(new LambdaQueryWrapper<SecResourceWeb>()
                .eq(resourceType != null, SecResourceWeb::getType, resourceType));
        return SecResourceWebConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecResourceWebDTO> listByPid(Long pid, String name) {
        LambdaQueryWrapper<SecResourceWeb> queryWrapper = Wrappers.lambdaQuery(SecResourceWeb.class)
                .eq(SecResourceWeb::getPid, pid)
                .like(StringUtils.hasText(name), SecResourceWeb::getName, name);
        List<SecResourceWeb> list = secResourceWebMapper.selectList(queryWrapper);
        return SecResourceWebConvert.INSTANCE.toDto(list);
    }

    @Override
    public List<SecResourceWebDTO> listByPidAndUserId(Long pid, Long userId, String name) {
        List<SecResourceWeb> list = secResourceWebMapper.listByPidAndUserId(pid, userId, name, true);
        return SecResourceWebConvert.INSTANCE.toDto(list);
    }

    @Override
    public int insert(SecResourceWebAddParam param) {
        SecResourceWeb record = BeanUtil.copy(param, new SecResourceWeb());
        return secResourceWebMapper.insert(record);
    }

    @Override
    public int update(SecResourceWebUpdateParam param) {
        SecResourceWeb record = BeanUtil.copy(param, new SecResourceWeb());
        return secResourceWebMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        List<SecResourceWebDTO> secResourceWebDTOS = listByPid(id, null);
        for (SecResourceWebDTO resourceWebDTO : secResourceWebDTOS) {
            deleteById(resourceWebDTO.getId());
        }
        return secResourceWebMapper.deleteById(id);
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
