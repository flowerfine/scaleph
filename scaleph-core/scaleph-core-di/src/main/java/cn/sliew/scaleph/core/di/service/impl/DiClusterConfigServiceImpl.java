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
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.core.di.service.DiClusterConfigService;
import cn.sliew.scaleph.core.di.service.convert.DiClusterConfigConvert;
import cn.sliew.scaleph.core.di.service.dto.DiClusterConfigDTO;
import cn.sliew.scaleph.core.di.service.param.DiClusterConfigParam;
import cn.sliew.scaleph.dao.entity.master.di.DiClusterConfig;
import cn.sliew.scaleph.dao.mapper.master.di.DiClusterConfigMapper;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiClusterConfigServiceImpl implements DiClusterConfigService {

    @Autowired
    private DiClusterConfigMapper diClusterConfigMapper;

    @Override
    public int insert(DiClusterConfigDTO dto) {
        DiClusterConfig config = DiClusterConfigConvert.INSTANCE.toDo(dto);
        return this.diClusterConfigMapper.insert(config);
    }

    @Override
    public int update(DiClusterConfigDTO dto) {
        DiClusterConfig config = DiClusterConfigConvert.INSTANCE.toDo(dto);
        return this.diClusterConfigMapper.updateById(config);
    }

    @Override
    public int deleteById(Long id) {
        return this.diClusterConfigMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.diClusterConfigMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<DiClusterConfigDTO> listByPage(DiClusterConfigParam param) {
        Page<DiClusterConfigDTO> result = new Page<>();
        Page<DiClusterConfig> list = this.diClusterConfigMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            new LambdaQueryWrapper<DiClusterConfig>()
                .like(StrUtil.isNotEmpty(param.getClusterName()), DiClusterConfig::getClusterName,
                    param.getClusterName())
                .eq(StrUtil.isNotEmpty(param.getClusterType()), DiClusterConfig::getClusterType,
                    param.getClusterType())
        );
        List<DiClusterConfigDTO> dtoList = DiClusterConfigConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DictVO> listAll() {
        List<DiClusterConfig> list = this.diClusterConfigMapper.selectList(null);
        List<DictVO> voList = new ArrayList<>();
        list.forEach(c -> {
            DictVO vo = new DictVO(String.valueOf(c.getId()), c.getClusterName());
            voList.add(vo);
        });
        return voList;
    }

    @Override
    public DiClusterConfigDTO selectOne(Long id) {
        DiClusterConfig config = this.diClusterConfigMapper.selectById(id);
        return DiClusterConfigConvert.INSTANCE.toDto(config);
    }

    @Override
    public Long totalCnt() {
        return this.diClusterConfigMapper.selectCount(null);
    }
}
