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

package cn.sliew.scaleph.application.doris.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorTemplateService;
import cn.sliew.scaleph.application.doris.service.resource.template.DorisTemplate;
import cn.sliew.scaleph.application.doris.service.resource.template.DorisTemplateConverter;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsDorisOperatorTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDorisOperatorTemplateMapper;
import cn.sliew.scaleph.application.doris.service.convert.WsDorisOperatorTemplateConvert;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorTemplateDTO;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateAddParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateListParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorTemplateUpdateParam;
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
public class WsDorisOperatorTemplateServiceImpl implements WsDorisOperatorTemplateService {

    @Autowired
    private WsDorisOperatorTemplateMapper wsDorisOperatorTemplateMapper;

    @Override
    public Page<WsDorisOperatorTemplateDTO> list(WsDorisOperatorTemplateListParam param) {
        Page<WsDorisOperatorTemplate> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsDorisOperatorTemplate> queryWrapper = Wrappers.lambdaQuery(WsDorisOperatorTemplate.class)
                .eq(WsDorisOperatorTemplate::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), WsDorisOperatorTemplate::getName, param.getName())
                .orderByAsc(WsDorisOperatorTemplate::getId);
        Page<WsDorisOperatorTemplate> wsDorisOperatorTemplatePage = wsDorisOperatorTemplateMapper.selectPage(page, queryWrapper);
        Page<WsDorisOperatorTemplateDTO> result = new Page<>(wsDorisOperatorTemplatePage.getCurrent(), wsDorisOperatorTemplatePage.getSize(), wsDorisOperatorTemplatePage.getTotal());
        List<WsDorisOperatorTemplateDTO> dtoList = WsDorisOperatorTemplateConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsDorisOperatorTemplateDTO selectOne(Long id) {
        WsDorisOperatorTemplate record = wsDorisOperatorTemplateMapper.selectById(id);
        checkState(record != null, () -> "doris template not exist for id = " + id);
        return WsDorisOperatorTemplateConvert.INSTANCE.toDto(record);
    }

    @Override
    public DorisTemplate asYaml(WsDorisOperatorTemplateDTO dto) {
        return DorisTemplateConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public int insert(WsDorisOperatorTemplateAddParam param) {
        WsDorisOperatorTemplate record = new WsDorisOperatorTemplate();
        BeanUtils.copyProperties(param, record);
        record.setTemplateId(UUIDUtil.randomUUId());
        if (param.getAdmin() != null) {
            record.setAdmin(JacksonUtil.toJsonString(param.getAdmin()));
        }
        if (param.getFeSpec() != null) {
            record.setFeSpec(JacksonUtil.toJsonString(param.getFeSpec()));
        }
        if (param.getBeSpec() != null) {
            record.setBeSpec(JacksonUtil.toJsonString(param.getBeSpec()));
        }
        if (param.getCnSpec() != null) {
            record.setCnSpec(JacksonUtil.toJsonString(param.getCnSpec()));
        }
        if (param.getBrokerSpec() != null) {
            record.setBrokerSpec(JacksonUtil.toJsonString(param.getBrokerSpec()));
        }
        return wsDorisOperatorTemplateMapper.insert(record);
    }

    @Override
    public int update(WsDorisOperatorTemplateUpdateParam param) {
        WsDorisOperatorTemplate record = new WsDorisOperatorTemplate();
        BeanUtils.copyProperties(param, record);
        if (param.getAdmin() != null) {
            record.setAdmin(JacksonUtil.toJsonString(param.getAdmin()));
        }
        if (param.getFeSpec() != null) {
            record.setFeSpec(JacksonUtil.toJsonString(param.getFeSpec()));
        }
        if (param.getBeSpec() != null) {
            record.setBeSpec(JacksonUtil.toJsonString(param.getBeSpec()));
        }
        if (param.getCnSpec() != null) {
            record.setCnSpec(JacksonUtil.toJsonString(param.getCnSpec()));
        }
        if (param.getBrokerSpec() != null) {
            record.setBrokerSpec(JacksonUtil.toJsonString(param.getBrokerSpec()));
        }
        return wsDorisOperatorTemplateMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsDorisOperatorTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsDorisOperatorTemplateMapper.deleteBatchIds(ids);
    }
}
