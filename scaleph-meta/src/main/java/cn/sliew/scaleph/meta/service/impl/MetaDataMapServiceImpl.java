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

package cn.sliew.scaleph.meta.service.impl;

import cn.sliew.scaleph.dao.entity.master.meta.MetaDataMap;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataMapMapper;
import cn.sliew.scaleph.meta.service.MetaDataMapService;
import cn.sliew.scaleph.meta.service.convert.MetaDataMapConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDataMapDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataMapParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataMapServiceImpl implements MetaDataMapService {

    @Autowired
    private MetaDataMapMapper metaDataMapMapper;

    @Override
    public int insert(MetaDataMapDTO metaDataMapDTO) {
        MetaDataMap map = MetaDataMapConvert.INSTANCE.toDo(metaDataMapDTO);
        return this.metaDataMapMapper.insert(map);
    }

    @Override
    public int update(MetaDataMapDTO metaDataMapDTO) {
        MetaDataMap map = MetaDataMapConvert.INSTANCE.toDo(metaDataMapDTO);
        return this.metaDataMapMapper.updateById(map);
    }

    @Override
    public int deleteById(Long id) {
        return this.metaDataMapMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return this.metaDataMapMapper.deleteBatchIds(ids);
    }

    @Override
    public Page<MetaDataMapDTO> listByPage(MetaDataMapParam param) {
        Page<MetaDataMapDTO> result = new Page<>();
        Page<MetaDataMap> list = this.metaDataMapMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.getSrcDataSetTypeCode(),
                param.getTgtDataSetTypeCode(),
                param.getSrcDataSetCode(),
                param.getTgtDataSetCode(),
                param.isAuto()
        );
        List<MetaDataMapDTO> dtoList = MetaDataMapConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
