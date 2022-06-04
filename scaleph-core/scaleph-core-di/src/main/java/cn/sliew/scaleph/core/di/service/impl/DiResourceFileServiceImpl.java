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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.DiResourceFileService;
import cn.sliew.scaleph.core.di.service.convert.DiResourceFileConvert;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.DiResourceFileParam;
import cn.sliew.scaleph.dao.entity.master.di.DiResourceFile;
import cn.sliew.scaleph.dao.mapper.master.di.DiResourceFileMapper;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiResourceFileServiceImpl implements DiResourceFileService {

    @Autowired
    private DiResourceFileMapper diResourceFileMapper;

    @Override
    public int insert(DiResourceFileDTO dto) {
        DiResourceFile file = DiResourceFileConvert.INSTANCE.toDo(dto);
        return this.diResourceFileMapper.insert(file);
    }

    @Override
    public int update(DiResourceFileDTO dto) {
        DiResourceFile file = DiResourceFileConvert.INSTANCE.toDo(dto);
        return this.diResourceFileMapper.updateById(file);
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diResourceFileMapper.delete(
            new LambdaQueryWrapper<DiResourceFile>()
                .in(DiResourceFile::getProjectId, projectIds)
        );
    }

    @Override
    public int deleteById(Long id) {
        return this.diResourceFileMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.diResourceFileMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<DiResourceFileDTO> listByPage(DiResourceFileParam param) {
        Page<DiResourceFileDTO> result = new Page<>();
        Page<DiResourceFile> list = this.diResourceFileMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()), param.getProjectId(),
            param.getFileName());
        List<DiResourceFileDTO> dtoList = DiResourceFileConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DiResourceFileDTO> listByIds(Collection<? extends Serializable> ids) {
        List<DiResourceFile> resources = this.diResourceFileMapper.selectList(
            new LambdaQueryWrapper<DiResourceFile>()
                .in(DiResourceFile::getId, ids)
        );
        return DiResourceFileConvert.INSTANCE.toDto(resources);
    }

    @Override
    public List<DictVO> listByProjectId(Long projectId) {
        List<DiResourceFile> resources = this.diResourceFileMapper.selectList(
            new LambdaQueryWrapper<DiResourceFile>()
                .eq(DiResourceFile::getProjectId, projectId)
        );
        List<DictVO> list = new ArrayList<>();
        resources.forEach(f -> {
            DictVO vo = new DictVO(String.valueOf(f.getId()), f.getFileName());
            list.add(vo);
        });
        return list;
    }
}
