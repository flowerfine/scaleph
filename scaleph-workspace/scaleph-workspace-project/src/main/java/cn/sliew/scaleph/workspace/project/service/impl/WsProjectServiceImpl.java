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

package cn.sliew.scaleph.workspace.project.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsProject;
import cn.sliew.scaleph.dao.mapper.master.ws.WsProjectMapper;
import cn.sliew.scaleph.workspace.project.service.WsProjectService;
import cn.sliew.scaleph.workspace.project.service.convert.WsProjectConvert;
import cn.sliew.scaleph.workspace.project.service.dto.WsProjectDTO;
import cn.sliew.scaleph.workspace.project.service.param.WsProjectParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WsProjectServiceImpl implements WsProjectService {

    @Autowired
    private WsProjectMapper diProjectMapper;

    @Override
    public Page<WsProjectDTO> listByPage(WsProjectParam param) {
        Page<WsProject> list = this.diProjectMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsProject.class)
                        .like(StrUtil.isNotEmpty(param.getProjectCode()), WsProject::getProjectCode, param.getProjectCode())
                        .like(StrUtil.isNotEmpty(param.getProjectName()), WsProject::getProjectName, param.getProjectName())
        );
        Page<WsProjectDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        result.setRecords(WsProjectConvert.INSTANCE.toDto(list.getRecords()));
        return result;
    }

    @Override
    public List<DictVO> listAll() {
        List<WsProject> list = diProjectMapper.selectList(null);
        return list.stream()
                .map(project -> new DictVO(String.valueOf(project.getId()), project.getProjectCode()))
                .collect(Collectors.toList());
    }

    @Override
    public WsProjectDTO selectOne(Serializable id) {
        WsProject project = diProjectMapper.selectById(id);
        return WsProjectConvert.INSTANCE.toDto(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int insert(WsProjectDTO dto) {
        WsProject project = WsProjectConvert.INSTANCE.toDo(dto);
        return diProjectMapper.insert(project);
    }

    @Override
    public int update(WsProjectDTO dto) {
        WsProject project = WsProjectConvert.INSTANCE.toDo(dto);
        return diProjectMapper.updateById(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        return diProjectMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(List<Long> ids) {
        return diProjectMapper.deleteBatchIds(ids);
    }

    @Override
    public Long totalCnt() {
        return diProjectMapper.selectCount(Wrappers.emptyWrapper());
    }
}
