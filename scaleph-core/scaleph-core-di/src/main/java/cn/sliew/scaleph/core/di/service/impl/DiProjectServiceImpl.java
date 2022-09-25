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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.core.di.service.DiDirectoryService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.DiProjectService;
import cn.sliew.scaleph.core.di.service.DiResourceFileService;
import cn.sliew.scaleph.core.di.service.convert.DiProjectConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.core.di.service.dto.DiProjectDTO;
import cn.sliew.scaleph.core.di.service.param.DiProjectParam;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.di.DiProject;
import cn.sliew.scaleph.dao.mapper.master.di.DiProjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据集成-项目信息 服务实现类
 * </p>
 *
 * @author liyu
 */
@Service
public class DiProjectServiceImpl implements DiProjectService {

    @Autowired
    private DiProjectMapper diProjectMapper;

    @Autowired
    private DiDirectoryService diDirectoryService;

    @Autowired
    private DiJobService diJobService;

    @Autowired
    private DiResourceFileService diResourceFileService;

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int insert(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        int result = this.diProjectMapper.insert(project);
        DiDirectoryDTO dir = new DiDirectoryDTO();
        dir.setProjectId(project.getId());
        dir.setDirectoryName(dto.getProjectCode());
        dir.setPid(0L);
        this.diDirectoryService.insert(dir);
        return result;
    }

    @Override
    public int update(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        return this.diProjectMapper.updateById(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        List<Long> list = Collections.singletonList(id);
        this.diResourceFileService.deleteByProjectId(list);
        this.diDirectoryService.deleteByProjectIds(list);
        this.diJobService.deleteByProjectId(list);
        return this.diProjectMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        this.diResourceFileService.deleteByProjectId(map.values());
        this.diDirectoryService.deleteByProjectIds(map.values());
        this.diJobService.deleteByProjectId(map.values());
        return this.diProjectMapper.deleteBatchIds(map.values());
    }

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
    public List<DiProjectDTO> listAll() {
        List<DiProject> list = this.diProjectMapper.selectList(null);
        return DiProjectConvert.INSTANCE.toDto(list);
    }

    @Override
    public Long totalCnt() {
        return this.diProjectMapper.selectCount(null);
    }

    @Override
    public DiProjectDTO selectOne(Serializable id) {
        DiProject project = this.diProjectMapper.selectById(id);
        return DiProjectConvert.INSTANCE.toDto(project);
    }
}
