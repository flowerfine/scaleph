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

package cn.sliew.scaleph.core.di.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.DiProjectService;
import cn.sliew.scaleph.core.di.service.convert.DiProjectConvert;
import cn.sliew.scaleph.core.di.service.dto.DiProjectDTO;
import cn.sliew.scaleph.core.di.service.param.DiProjectParam;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.di.DiProject;
import cn.sliew.scaleph.dao.mapper.master.di.DiProjectMapper;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiProjectServiceImpl implements DiProjectService {

    @Autowired
    private DiProjectMapper diProjectMapper;
    @Autowired
    private DiJobService diJobService;

    @Override
    public Page<DiProjectDTO> listByPage(DiProjectParam param) {
        Page<DiProjectDTO> result = new Page<>();
        Page<DiProject> list = this.diProjectMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<DiProject>()
                        .like(StrUtil.isNotEmpty(param.getProjectCode()), DiProject::getProjectCode,
                                param.getProjectCode())
                        .like(StrUtil.isNotEmpty(param.getProjectName()), DiProject::getProjectName,
                                param.getProjectName())
        );
        List<DiProjectDTO> dtoList = DiProjectConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DictVO> listAll() {
        List<DiProject> list = diProjectMapper.selectList(null);
        return list.stream()
                .map(project -> new DictVO(String.valueOf(project.getId()), project.getProjectCode()))
                .collect(Collectors.toList());
    }

    @Override
    public DiProjectDTO selectOne(Serializable id) {
        DiProject project = diProjectMapper.selectById(id);
        return DiProjectConvert.INSTANCE.toDto(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int insert(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        int result = diProjectMapper.insert(project);
        return result;
    }

    @Override
    public int update(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        return diProjectMapper.updateById(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) throws ScalephException {
        checkValidJob(Collections.singletonList(id));
        deleteProjectRelatedData(Collections.singletonList(id));
        return diProjectMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(Map<Integer, Long> map) throws ScalephException {
        checkValidJob(map.values());
        deleteProjectRelatedData(map.values());
        return diProjectMapper.deleteBatchIds(map.values());
    }

    private void checkValidJob(Collection<Long> projectIds) throws ScalephException {
        if (diJobService.hasValidJob(projectIds)) {
            throw new ScalephException(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.notEmptyProject"));
        }
    }

    private void deleteProjectRelatedData(Collection<Long> projectIds) {
        diJobService.deleteByProjectId(projectIds);
    }

    @Override
    public Long totalCnt() {
        return diProjectMapper.selectCount(null);
    }
}
