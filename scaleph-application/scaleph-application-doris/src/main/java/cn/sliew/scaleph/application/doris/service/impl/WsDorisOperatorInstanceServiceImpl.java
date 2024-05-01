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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorInstanceService;
import cn.sliew.scaleph.application.doris.service.WsDorisOperatorTemplateService;
import cn.sliew.scaleph.application.doris.service.resource.cluster.DorisClusterConverter;
import cn.sliew.scaleph.application.doris.operator.status.DorisClusterStatus;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsDorisOperatorInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDorisOperatorInstanceMapper;
import cn.sliew.scaleph.application.doris.operator.DorisCluster;
import cn.sliew.scaleph.application.doris.service.DorisOperatorService;
import cn.sliew.scaleph.application.doris.service.convert.WsDorisOperatorInstanceConvert;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorTemplateDTO;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceAddParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceListParam;
import cn.sliew.scaleph.application.doris.service.param.WsDorisOperatorInstanceUpdateParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Predicates;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class WsDorisOperatorInstanceServiceImpl implements WsDorisOperatorInstanceService {

    @Autowired
    private WsDorisOperatorInstanceMapper wsDorisOperatorInstanceMapper;
    @Autowired
    private WsDorisOperatorTemplateService wsDorisOperatorTemplateService;
    @Autowired
    private DorisOperatorService dorisOperatorService;

    @Override
    public Page<WsDorisOperatorInstanceDTO> list(WsDorisOperatorInstanceListParam param) {
        Page<WsDorisOperatorInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsDorisOperatorInstance> queryWrapper = Wrappers.lambdaQuery(WsDorisOperatorInstance.class)
                .eq(WsDorisOperatorInstance::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), WsDorisOperatorInstance::getName, param.getName())
                .orderByAsc(WsDorisOperatorInstance::getId);
        Page<WsDorisOperatorInstance> wsDorisOperatorInstancePage = wsDorisOperatorInstanceMapper.selectPage(page, queryWrapper);
        Page<WsDorisOperatorInstanceDTO> result = new Page<>(wsDorisOperatorInstancePage.getCurrent(), wsDorisOperatorInstancePage.getSize(), wsDorisOperatorInstancePage.getTotal());
        List<WsDorisOperatorInstanceDTO> dtoList = WsDorisOperatorInstanceConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<Long> listAll() {
        LambdaQueryWrapper<WsDorisOperatorInstance> queryWrapper = Wrappers.lambdaQuery(WsDorisOperatorInstance.class)
                .eq(WsDorisOperatorInstance::getDeployed, YesOrNo.YES)
                .select(WsDorisOperatorInstance::getId);
        List<WsDorisOperatorInstance> wsDorisOperatorInstances = wsDorisOperatorInstanceMapper.selectList(queryWrapper);
        return wsDorisOperatorInstances.stream().map(WsDorisOperatorInstance::getId).collect(Collectors.toList());
    }

    @Override
    public WsDorisOperatorInstanceDTO selectOne(Long id) {
        WsDorisOperatorInstance record = wsDorisOperatorInstanceMapper.selectById(id);
        checkState(record != null, () -> "doris instance not exist for id = " + id);
        return WsDorisOperatorInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsDorisOperatorInstanceDTO fromTemplate(Long templateId) {
        WsDorisOperatorTemplateDTO wsDorisOperatorTemplateDTO = wsDorisOperatorTemplateService.selectOne(templateId);
        WsDorisOperatorInstanceDTO wsDorisOperatorInstanceDTO = new WsDorisOperatorInstanceDTO();
        wsDorisOperatorInstanceDTO.setAdmin(wsDorisOperatorTemplateDTO.getAdmin());
        wsDorisOperatorInstanceDTO.setFeSpec(wsDorisOperatorTemplateDTO.getFeSpec());
        wsDorisOperatorInstanceDTO.setBeSpec(wsDorisOperatorTemplateDTO.getBeSpec());
        wsDorisOperatorInstanceDTO.setCnSpec(wsDorisOperatorTemplateDTO.getCnSpec());
        wsDorisOperatorInstanceDTO.setBrokerSpec(wsDorisOperatorTemplateDTO.getBrokerSpec());
        wsDorisOperatorInstanceDTO.setRemark("generated from template-" + wsDorisOperatorTemplateDTO.getName());
        return wsDorisOperatorInstanceDTO;
    }

    @Override
    public DorisCluster asYaml(WsDorisOperatorInstanceDTO dto) {
        return DorisClusterConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public int insert(WsDorisOperatorInstanceAddParam param) {
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
    public int update(WsDorisOperatorInstanceUpdateParam param) {
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
        WsDorisOperatorInstanceDTO instanceDTO = selectOne(id);
        checkState(instanceDTO.getDeployed() == YesOrNo.NO, () -> "doris instance already deployed! can't delete");
        return wsDorisOperatorInstanceMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        ids.forEach(this::deleteById);
        return ids.size();
    }

    @Override
    public void deploy(Long id) {
        WsDorisOperatorInstanceDTO instanceDTO = selectOne(id);
        DorisCluster dorisCluster = asYaml(instanceDTO);
        if (dorisCluster.getSpec().getAdminUser() != null) {
            log.error("{} can't specify doris admin when deploy, remove it automatically", instanceDTO.getName());
            dorisCluster.getSpec().setAdminUser(null);
        }
        String yaml = Serialization.asYaml(dorisCluster);
        dorisOperatorService.deploy(instanceDTO.getClusterCredentialId(), yaml);
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
        record.setId(instanceDTO.getId());
        record.setDeployed(YesOrNo.YES);
        wsDorisOperatorInstanceMapper.updateById(record);
    }

    @Override
    public void apply(Long id) {
        WsDorisOperatorInstanceDTO instanceDTO = selectOne(id);
        DorisCluster dorisCluster = asYaml(instanceDTO);
        String yaml = Serialization.asYaml(dorisCluster);
        dorisOperatorService.apply(instanceDTO.getClusterCredentialId(), yaml);
    }

    @Override
    public void shutdown(Long id) {
        WsDorisOperatorInstanceDTO instanceDTO = selectOne(id);
        DorisCluster dorisCluster = asYaml(instanceDTO);
        String yaml = Serialization.asYaml(dorisCluster);
        dorisOperatorService.shutdown(instanceDTO.getClusterCredentialId(), yaml);
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
        record.setId(instanceDTO.getId());
        wsDorisOperatorInstanceMapper.updateById(record);
    }

    @Override
    public Optional<GenericKubernetesResource> getStatus(Long id) {
        try {
            WsDorisOperatorInstanceDTO instanceDTO = selectOne(id);
            if (instanceDTO.getDeployed() == YesOrNo.YES) {
                return dorisOperatorService.get(instanceDTO);
            }
            return Optional.empty();
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public Optional<GenericKubernetesResource> getStatusWithoutManagedFields(Long id) {
        Optional<GenericKubernetesResource> optional = getStatus(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        GenericKubernetesResource status = optional.get();
        GenericKubernetesResourceBuilder builder = new GenericKubernetesResourceBuilder(status);
        ObjectMetaBuilder objectMetaBuilder = new ObjectMetaBuilder(status.getMetadata());
        objectMetaBuilder.removeMatchingFromManagedFields(Predicates.isTrue());
        builder.withMetadata(objectMetaBuilder.build());
        return Optional.of(builder.build());
    }

    @Override
    public int updateStatus(Long id, DorisClusterStatus status) {
        if (status == null) {
            return -1;
        }
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
        record.setId(id);
        if (status.getFeStatus() != null) {
            record.setFeStatus(JacksonUtil.toJsonString(status.getFeStatus()));
        }
        if (status.getBeStatus() != null) {
            record.setBeStatus(JacksonUtil.toJsonString(status.getBeStatus()));
        }
        if (status.getCnStatus() != null) {
            record.setCnStatus(JacksonUtil.toJsonString(status.getCnStatus()));
        }
        if (status.getBrokerStatus() != null) {
            record.setBrokerStatus(JacksonUtil.toJsonString(status.getBrokerStatus()));
        }
        return wsDorisOperatorInstanceMapper.updateById(record);
    }

    @Override
    public int clearStatus(Long id) {
        WsDorisOperatorInstance record = new WsDorisOperatorInstance();
        record.setId(id);
        record.setDeployed(YesOrNo.NO);
        record.setFeStatus(null);
        record.setBeStatus(null);
        record.setCnStatus(null);
        record.setBrokerStatus(null);
        return wsDorisOperatorInstanceMapper.updateById(record);
    }
}
