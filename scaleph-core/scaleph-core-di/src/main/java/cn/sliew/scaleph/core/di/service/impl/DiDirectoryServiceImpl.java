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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.core.di.service.DiDirectoryService;
import cn.sliew.scaleph.core.di.service.convert.DiDirectoryConvert;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiDirectory;
import cn.sliew.scaleph.dao.mapper.master.di.DiDirectoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gleiyu
 */
@Service
public class DiDirectoryServiceImpl implements DiDirectoryService {

    @Autowired
    private DiDirectoryMapper diDirectoryMapper;

    @Override
    public DiDirectoryDTO insert(DiDirectoryDTO dto) {
        DiDirectory directory = DiDirectoryConvert.INSTANCE.toDo(dto);
        diDirectoryMapper.insert(directory);
        dto.setId(directory.getId());
        return dto;
    }

    @Override
    public int update(DiDirectoryDTO dto) {
        DiDirectory directory = DiDirectoryConvert.INSTANCE.toDo(dto);
        return diDirectoryMapper.updateById(directory);
    }

    @Override
    public int deleteById(Long id) {
        return diDirectoryMapper.deleteById(id);
    }

    @Override
    public int deleteByPid(Long pid) {
        return diDirectoryMapper.delete(new LambdaQueryWrapper<DiDirectory>()
            .eq(DiDirectory::getPid, pid));
    }

    @Override
    public int deleteByProjectIds(Collection<? extends Serializable> projectId) {
        return diDirectoryMapper.delete(new LambdaQueryWrapper<DiDirectory>()
            .in(DiDirectory::getProjectId, projectId));
    }

    @Override
    public List<DiDirectoryDTO> selectByProjectId(Long projectId) {
        List<DiDirectory> list = diDirectoryMapper.selectList(
            new LambdaQueryWrapper<DiDirectory>().eq(DiDirectory::getProjectId, projectId)
        );
        return DiDirectoryConvert.INSTANCE.toDto(list);
    }

    @Override
    public boolean hasChildDir(Long projectId, Long dirId) {
        DiDirectory dir = diDirectoryMapper.selectOne(
            new LambdaQueryWrapper<DiDirectory>()
                .eq(DiDirectory::getProjectId, projectId)
                .eq(DiDirectory::getPid, dirId)
                .last("limit 1")
        );
        return dir != null && dir.getId() != null;
    }

    @Override
    public DiDirectoryDTO selectById(Long dirId) {
        DiDirectory dir = diDirectoryMapper.selectById(dirId);
        return DiDirectoryConvert.INSTANCE.toDto(dir);
    }

    @Override
    public Map<Long, DiDirectoryDTO> loadFullPath(List<Long> directoryIds) {
        if (CollectionUtil.isEmpty(directoryIds)) {
            return Collections.emptyMap();
        }

        List<DiDirectory> dirList = diDirectoryMapper.selectFullPath(directoryIds);
        Map<Long, DiDirectory> dirMap = dirList.stream().collect(Collectors.toMap(DiDirectory::getId, Function.identity()));

        Map<Long, DiDirectoryDTO> resultMap = new HashMap<>();
        for (Long dirId : directoryIds) {
            DiDirectoryDTO dto = convertFullPathDTO(dirMap, dirId);
            if (dto != null) {
                resultMap.put(dirId, dto);
            }
        }
        return resultMap;
    }

    private DiDirectoryDTO convertFullPathDTO(Map<Long, DiDirectory> dirMap, Long dirId) {
        DiDirectory dir = dirMap.get(dirId);
        if (dir == null) {
            return null;
        }
        DiDirectoryDTO dto = DiDirectoryConvert.INSTANCE.toDto(dir);
        String fullPath = concatPath(dirMap, dir);
        dto.setFullPath(fullPath);
        return dto;
    }

    private String concatPath(Map<Long, DiDirectory> dirMap, DiDirectory dir) {
        if (dir.getPid() == 0) {
            return Constants.PATH_SEPARATOR + dir.getDirectoryName();
        }
        DiDirectory parentDir = dirMap.get(dir.getPid());
        return concatPath(dirMap, parentDir) + Constants.PATH_SEPARATOR + dir.getDirectoryName();
    }
}
