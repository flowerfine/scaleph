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

package cn.sliew.scaleph.engine.doris.service.impl;

import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsDorisTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDorisTemplateMapper;
import cn.sliew.scaleph.engine.doris.service.WsDorisTemplateService;
import cn.sliew.scaleph.engine.doris.service.convert.WsDorisTemplateConvert;
import cn.sliew.scaleph.engine.doris.service.dto.WsDorisTemplateDTO;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateAddParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateListParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateUpdateParam;
import cn.sliew.scaleph.engine.doris.service.resource.DorisTemplate;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsDorisTemplateServiceImpl implements WsDorisTemplateService {

    @Autowired
    private WsDorisTemplateMapper wsDorisTemplateMapper;

    @Override
    public Page<WsDorisTemplateDTO> list(WsDorisTemplateListParam param) {
        Page<WsDorisTemplate> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsDorisTemplate> queryWrapper = Wrappers.lambdaQuery(WsDorisTemplate.class)
                .eq(WsDorisTemplate::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), WsDorisTemplate::getName, param.getName())
                .orderByAsc(WsDorisTemplate::getId);
        Page<WsDorisTemplate> wsDorisTemplatePage = wsDorisTemplateMapper.selectPage(page, queryWrapper);
        Page<WsDorisTemplateDTO> result = new Page<>(wsDorisTemplatePage.getCurrent(), wsDorisTemplatePage.getSize(), wsDorisTemplatePage.getTotal());
        List<WsDorisTemplateDTO> dtoList = WsDorisTemplateConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsDorisTemplateDTO selectOne(Long id) {
        WsDorisTemplate record = wsDorisTemplateMapper.selectById(id);
        checkState(record != null, () -> "doris template not exist for id = " + id);
        return WsDorisTemplateConvert.INSTANCE.toDto(record);
    }

    @Override
    public DorisTemplate asYaml(Long id) {
        return null;
    }

    @Override
    public int insert(WsDorisTemplateAddParam param) {
        WsDorisTemplate record = new WsDorisTemplate();
        BeanUtils.copyProperties(param, record);
        record.setTemplateId(UUIDUtil.randomUUId());
        return wsDorisTemplateMapper.insert(record);
    }

    @Override
    public int update(WsDorisTemplateUpdateParam param) {
        WsDorisTemplate record = new WsDorisTemplate();
        BeanUtils.copyProperties(param, record);
        return wsDorisTemplateMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsDorisTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsDorisTemplateMapper.deleteBatchIds(ids);
    }
}
