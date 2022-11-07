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

package cn.sliew.scaleph.ds.service.impl;

import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.dao.entity.master.ds.DsInfo;
import cn.sliew.scaleph.dao.entity.master.ds.DsInfoVO;
import cn.sliew.scaleph.dao.mapper.master.ds.DsInfoMapper;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.service.DsInfoService;
import cn.sliew.scaleph.ds.service.convert.DsInfoConvert;
import cn.sliew.scaleph.ds.service.convert.DsInfoVOConvert;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.param.DsInfoListParam;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DsInfoServiceImpl implements DsInfoService {

    @Autowired
    private DsInfoMapper dsInfoMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.DATASOURCE;
    }

    @Override
    public Page<DsInfoDTO> list(ResourceListParam param) {
        DsInfoListParam dsInfoListParam = DsInfoConvert.INSTANCE.convert(param);
        return list(dsInfoListParam);
    }

    @Override
    public DsInfoDTO getRaw(Long id) {
        return selectOne(id, true);
    }

    @Override
    public Page<DsInfoDTO> list(DsInfoListParam param) {
        Page<DsInfo> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<DsInfoVO> dsInfoPage = dsInfoMapper.list(page, param.getDsType(), param.getName());
        Page<DsInfoDTO> result = new Page<>(dsInfoPage.getCurrent(), dsInfoPage.getSize(), dsInfoPage.getTotal());
        List<DsInfoDTO> dsInfoDTOS = DsInfoVOConvert.INSTANCE.toDto(dsInfoPage.getRecords());
        result.setRecords(dsInfoDTOS);
        return result;
    }

    @Override
    public List<DsInfoDTO> listByType(DataSourceType type) {
        List<DsInfoVO> dsInfoVOS = dsInfoMapper.listByTypes(type);
        return DsInfoVOConvert.INSTANCE.toDto(dsInfoVOS);
    }

    @Override
    public DsInfoDTO selectOne(Long id, boolean decrypt) {
        DsInfoVO vo = dsInfoMapper.getById(id);
        checkState(vo != null, () -> "data source info not exists for id: " + id);
        DsInfoDTO dsInfoDTO = DsInfoVOConvert.INSTANCE.toDto(vo);
        if (decrypt) {
            Map<String, Object> props = new HashMap<>();
            for (Map.Entry<String, Object> entry : dsInfoDTO.getProps().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (Objects.nonNull(value)
                        && value instanceof String
                        && CodecUtil.isEncryptedStr((String) value)) {
                    props.put(key, CodecUtil.decrypt((String) value));
                } else {
                    props.put(key, value);
                }
            }
            dsInfoDTO.setProps(props);
        }
        return dsInfoDTO;
    }

    @Override
    public int insert(AbstractDataSource dataSource) {
        DsInfo record = DsInfoConvert.INSTANCE.toDo(dataSource.toDsInfo());
        return dsInfoMapper.insert(record);
    }

    @Override
    public int update(Long id, AbstractDataSource dataSource) {
        DsInfo record = DsInfoConvert.INSTANCE.toDo(dataSource.toDsInfo());
        record.setId(id);
        return dsInfoMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return dsInfoMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return dsInfoMapper.deleteBatchIds(ids);
    }
}
