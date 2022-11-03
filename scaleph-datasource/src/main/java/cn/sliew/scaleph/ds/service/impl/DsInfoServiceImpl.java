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

import cn.sliew.scaleph.dao.entity.master.ds.DsInfo;
import cn.sliew.scaleph.dao.mapper.master.ds.DsInfoMapper;
import cn.sliew.scaleph.ds.service.DsInfoService;
import cn.sliew.scaleph.ds.service.convert.DsInfoConvert;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.param.DsInfoListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class DsInfoServiceImpl implements DsInfoService {

    @Autowired
    private DsInfoMapper dsInfoMapper;

    @Override
    public Page<DsInfoDTO> list(DsInfoListParam param) {
        LambdaQueryWrapper<DsInfo> queryWrapper = Wrappers.lambdaQuery(DsInfo.class)
                .eq(param.getDsTypeId() != null, DsInfo::getDsTypeId, param.getDsTypeId())
                .like(StringUtils.hasText(param.getName()), DsInfo::getName, param.getName());
        Page<DsInfo> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<DsInfo> dsInfoPage = dsInfoMapper.selectPage(page, queryWrapper);
        Page<DsInfoDTO> result = new Page<>(dsInfoPage.getCurrent(), dsInfoPage.getSize(), dsInfoPage.getTotal());
        List<DsInfoDTO> dsInfoDTOS = DsInfoConvert.INSTANCE.toDto(dsInfoPage.getRecords());
        result.setRecords(dsInfoDTOS);
        return result;
    }

    @Override
    public DsInfoDTO selectOne(Long id) {
        DsInfo record = dsInfoMapper.selectById(id);
        checkState(record != null, () -> "data source info not exists for id: " + id);
        return DsInfoConvert.INSTANCE.toDto(record);
    }
    
}
