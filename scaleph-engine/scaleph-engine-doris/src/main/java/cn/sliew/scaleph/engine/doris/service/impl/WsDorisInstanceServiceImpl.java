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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsDorisOperatorInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDorisOperatorInstanceMapper;
import cn.sliew.scaleph.engine.doris.operator.DorisCluster;
import cn.sliew.scaleph.engine.doris.service.WsDorisInstanceService;
import cn.sliew.scaleph.engine.doris.service.WsDorisTemplateService;
import cn.sliew.scaleph.engine.doris.service.convert.WsDorisInstanceConvert;
import cn.sliew.scaleph.engine.doris.service.dto.WsDorisInstanceDTO;
import cn.sliew.scaleph.engine.doris.service.dto.WsDorisTemplateDTO;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisInstanceAddParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisInstanceListParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisInstanceUpdateParam;
import cn.sliew.scaleph.engine.doris.service.resource.cluster.DorisClusterConverter;
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
public class WsDorisInstanceServiceImpl implements WsDorisInstanceService {

    @Autowired
    private WsDorisOperatorInstanceMapper wsDorisOperatorInstanceMapper;
    @Autowired
    private WsDorisTemplateService wsDorisTemplateService;

    @Override
    public Page<WsDorisInstanceDTO> list(WsDorisInstanceListParam param) {
        Page<WsDorisOperatorInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsDorisOperatorInstance> queryWrapper = Wrappers.lambdaQuery(WsDorisOperatorInstance.class)
                .eq(WsDorisOperatorInstance::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), WsDorisOperatorInstance::getName, param.getName())
                .orderByAsc(WsDorisOperatorInstance::getId);
        Page<WsDorisOperatorInstance> wsDorisInstancePage = wsDorisOperatorInstanceMapper.selectPage(page, queryWrapper);
        Page<WsDorisInstanceDTO> result = new Page<>(wsDorisInstancePage.getCurrent(), wsDorisInstancePage.getSize(), wsDorisInstancePage.getTotal());
        List<WsDorisInstanceDTO> dtoList = WsDorisInstanceConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsDorisInstanceDTO selectOne(Long id) {
        WsDorisOperatorInstance record = wsDorisOperatorInstanceMapper.selectById(id);
        checkState(record != null, () -> "doris instance not exist for id = " + id);
        return WsDorisInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsDorisInstanceDTO fromTemplate(Long templateId) {
        WsDorisTemplateDTO wsDorisTemplateDTO = wsDorisTemplateService.selectOne(templateId);
        WsDorisInstanceDTO wsDorisInstanceDTO = new WsDorisInstanceDTO();
        wsDorisInstanceDTO.setAdmin(wsDorisTemplateDTO.getAdmin());
        wsDorisInstanceDTO.setFeSpec(wsDorisTemplateDTO.getFeSpec());
        wsDorisInstanceDTO.setBeSpec(wsDorisTemplateDTO.getBeSpec());
        wsDorisInstanceDTO.setCnSpec(wsDorisTemplateDTO.getCnSpec());
        wsDorisInstanceDTO.setBrokerSpec(wsDorisTemplateDTO.getBrokerSpec());
        wsDorisInstanceDTO.setRemark("generated from template-" + wsDorisTemplateDTO.getName());
        return wsDorisInstanceDTO;
    }

    @Override
    public DorisCluster asYaml(WsDorisInstanceDTO dto) {
        return DorisClusterConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public int insert(WsDorisInstanceAddParam param) {
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
        BeanUtils.copyProperties(param, record);
        record.setInstanceId(UUIDUtil.randomUUId());
        record.setDeployed(YesOrNo.NO);
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
        return wsDorisOperatorInstanceMapper.insert(record);
    }

    @Override
    public int update(WsDorisInstanceUpdateParam param) {
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
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
        return wsDorisOperatorInstanceMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsDorisOperatorInstanceMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsDorisOperatorInstanceMapper.deleteBatchIds(ids);
    }
}
