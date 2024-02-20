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

import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsProject;
import cn.sliew.scaleph.dao.mapper.master.ws.WsProjectMapper;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.workspace.project.service.WsProjectService;
import cn.sliew.scaleph.workspace.project.service.convert.WsProjectConvert;
import cn.sliew.scaleph.workspace.project.service.dto.WsProjectDTO;
import cn.sliew.scaleph.workspace.project.service.param.WsProjectParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WsProjectServiceImpl implements WsProjectService {

    @Autowired
    private WsProjectMapper wsProjectMapper;

    @Override
    public Page<WsProjectDTO> listByPage(WsProjectParam param) {
        Page<WsProject> list = this.wsProjectMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsProject.class)
                        .like(StringUtils.hasText(param.getProjectCode()), WsProject::getProjectCode, param.getProjectCode())
                        .like(StringUtils.hasText(param.getProjectName()), WsProject::getProjectName, param.getProjectName())
        );
        Page<WsProjectDTO> result = new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        result.setRecords(WsProjectConvert.INSTANCE.toDto(list.getRecords()));
        return result;
    }

    @Override
    public List<DictVO> listAll() {
        List<WsProject> list = wsProjectMapper.selectList(Wrappers.emptyWrapper());
        return list.stream()
                .map(project -> new DictVO(String.valueOf(project.getId()), project.getProjectCode()))
                .collect(Collectors.toList());
    }

    @Override
    public WsProjectDTO selectOne(Long id) {
        WsProject record = wsProjectMapper.selectById(id);
        return WsProjectConvert.INSTANCE.toDto(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int insert(WsProjectDTO dto) {
        WsProject record = WsProjectConvert.INSTANCE.toDo(dto);
        return wsProjectMapper.insert(record);
    }

    @Override
    public int update(WsProjectDTO dto) {
        WsProject record = WsProjectConvert.INSTANCE.toDo(dto);
        return wsProjectMapper.updateById(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        return wsProjectMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(List<Long> ids) {
        return wsProjectMapper.deleteBatchIds(ids);
    }

    @Override
    public Long totalCnt() {
        return wsProjectMapper.selectCount(Wrappers.emptyWrapper());
    }
}
