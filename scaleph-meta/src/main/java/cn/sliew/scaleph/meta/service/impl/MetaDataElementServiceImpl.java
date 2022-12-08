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

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataElement;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataElementMapper;
import cn.sliew.scaleph.meta.service.MetaDataElementService;
import cn.sliew.scaleph.meta.service.convert.MetaDataElementConvert;
import cn.sliew.scaleph.meta.service.dto.MetaDataElementDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataElementParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaDataElementServiceImpl implements MetaDataElementService {

    @Autowired
    private MetaDataElementMapper metaDataElementMapper;

    @Override
    public int insert(MetaDataElementDTO metaDataElementDTO) {
        MetaDataElement dataElement = MetaDataElementConvert.INSTANCE.toDo(metaDataElementDTO);
        return this.metaDataElementMapper.insert(dataElement);
    }

    @Override
    public int update(MetaDataElementDTO metaDataElementDTO) {
        MetaDataElement dataElement = MetaDataElementConvert.INSTANCE.toDo(metaDataElementDTO);
        if (dataElement.getDataSetTypeId() == null) {
            dataElement.setDataSetTypeId(0L);
        }
        return this.metaDataElementMapper.updateById(dataElement);
    }

    @Override
    public int deleteById(Long id) {
        return this.metaDataElementMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return this.metaDataElementMapper.deleteBatchIds(ids);
    }

    @Override
    public Page<MetaDataElementDTO> listByPage(MetaDataElementParam param) {
        Page<MetaDataElementDTO> result = new Page<>();
        Page<MetaDataElement> list = this.metaDataElementMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<MetaDataElement>()
                        .like(StrUtil.isNotEmpty(param.getElementCode()), MetaDataElement::getElementCode,
                                param.getElementCode())
                        .like(StrUtil.isNotEmpty(param.getElementName()), MetaDataElement::getElementName,
                                param.getElementName())
        );
        List<MetaDataElementDTO> dtoList = MetaDataElementConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
