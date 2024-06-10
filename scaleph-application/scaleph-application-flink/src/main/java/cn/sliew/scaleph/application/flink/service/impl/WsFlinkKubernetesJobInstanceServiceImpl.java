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

package cn.sliew.scaleph.application.flink.service.impl;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionJobSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobState;
import cn.sliew.scaleph.application.flink.operator.status.*;
import cn.sliew.scaleph.application.flink.resource.definition.job.instance.FlinkJobInstanceConverterFactory;
import cn.sliew.scaleph.application.flink.resource.definition.job.instance.MetadataHandler;
import cn.sliew.scaleph.application.flink.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.application.flink.service.convert.WsFlinkKubernetesJobInstanceConvert;
import cn.sliew.scaleph.application.flink.service.convert.WsFlinkKubernetesJobInstanceSavepointConvert;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceSavepointDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceDeployParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceSavepointListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobInstanceShutdownParam;
import cn.sliew.scaleph.application.flink.watch.FlinkDeploymentWatchCallbackHandler;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.ResourceLifecycleState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointFormatType;
import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointTriggerType;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJobInstance;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJobInstanceSavepoint;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesJobInstanceMapper;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesJobInstanceSavepointMapper;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Predicates;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesJobInstanceServiceImpl implements WsFlinkKubernetesJobInstanceService {

    @Autowired
    private WsFlinkKubernetesJobInstanceMapper wsFlinkKubernetesJobInstanceMapper;
    @Autowired
    private WsFlinkKubernetesJobInstanceSavepointMapper wsFlinkKubernetesJobInstanceSavepointMapper;
    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;
    @Autowired
    private FlinkKubernetesOperatorService flinkKubernetesOperatorService;
    @Autowired
    private FlinkJobInstanceConverterFactory flinkJobInstanceConverterFactory;

    @Autowired
    private MetadataHandler metadataHandler;
    @Autowired
    private FlinkDeploymentWatchCallbackHandler flinkDeploymentWatchCallbackHandler;

    @Override
    public Page<WsFlinkKubernetesJobInstanceDTO> list(WsFlinkKubernetesJobInstanceListParam param) {
        Page<WsFlinkKubernetesJobInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkKubernetesJobInstance> queryWrapper = Wrappers.lambdaQuery(WsFlinkKubernetesJobInstance.class)
                .eq(WsFlinkKubernetesJobInstance::getWsFlinkKubernetesJobId, param.getWsFlinkKubernetesJobId())
                .orderByDesc(WsFlinkKubernetesJobInstance::getId);
        Page<WsFlinkKubernetesJobInstance> wsFlinkKubernetesJobInstancePage = wsFlinkKubernetesJobInstanceMapper.selectPage(page, queryWrapper);
        Page<WsFlinkKubernetesJobInstanceDTO> result = new Page<>(wsFlinkKubernetesJobInstancePage.getCurrent(), wsFlinkKubernetesJobInstancePage.getSize(), wsFlinkKubernetesJobInstancePage.getTotal());
        List<WsFlinkKubernetesJobInstanceDTO> wsFlinkKubernetesJobInstanceDTOS = WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(wsFlinkKubernetesJobInstancePage.getRecords());
        result.setRecords(wsFlinkKubernetesJobInstanceDTOS);
        return result;
    }

    @Override
    public WsFlinkKubernetesJobInstanceDTO selectOne(Long id) {
        WsFlinkKubernetesJobInstance record = wsFlinkKubernetesJobInstanceMapper.selectOne(id);
        checkState(record != null, () -> "flink kubernetes job instance not exist for id = " + id);
        return WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public Optional<WsFlinkKubernetesJobInstanceDTO> selectCurrent(Long wsFlinkKubernetesJobId) {
        Optional<WsFlinkKubernetesJobInstance> optional = wsFlinkKubernetesJobInstanceMapper.selectCurrent(wsFlinkKubernetesJobId);
        return optional.map(record -> WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(record));
    }

    @Override
    public Page<WsFlinkKubernetesJobInstanceSavepointDTO> selectSavepoint(WsFlinkKubernetesJobInstanceSavepointListParam param) {
        Page<WsFlinkKubernetesJobInstanceSavepoint> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkKubernetesJobInstanceSavepoint> queryWrapper = Wrappers.lambdaQuery(WsFlinkKubernetesJobInstanceSavepoint.class)
                .eq(WsFlinkKubernetesJobInstanceSavepoint::getWsFlinkKubernetesJobInstanceId, param.getWsFlinkKubernetesJobInstanceId())
                .orderByDesc(WsFlinkKubernetesJobInstanceSavepoint::getTimeStamp);
        Page<WsFlinkKubernetesJobInstanceSavepoint> wsFlinkKubernetesJobInstanceSavepointPage = wsFlinkKubernetesJobInstanceSavepointMapper.selectPage(page, queryWrapper);
        Page<WsFlinkKubernetesJobInstanceSavepointDTO> result = new Page<>(wsFlinkKubernetesJobInstanceSavepointPage.getCurrent(), wsFlinkKubernetesJobInstanceSavepointPage.getSize(), wsFlinkKubernetesJobInstanceSavepointPage.getTotal());
        List<WsFlinkKubernetesJobInstanceSavepointDTO> wsFlinkKubernetesJobInstanceSavepointDTOS = WsFlinkKubernetesJobInstanceSavepointConvert.INSTANCE.toDto(wsFlinkKubernetesJobInstanceSavepointPage.getRecords());
        result.setRecords(wsFlinkKubernetesJobInstanceSavepointDTOS);
        return result;
    }

    @Override
    public String mockYaml(Long wsFlinkKubernetesJobId) {
        WsFlinkKubernetesJobDTO jobDTO = wsFlinkKubernetesJobService.selectOne(wsFlinkKubernetesJobId);
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = new WsFlinkKubernetesJobInstanceDTO();
        jobInstanceDTO.setInstanceId(jobDTO.getJobId());
        jobInstanceDTO.setWsFlinkKubernetesJobId(wsFlinkKubernetesJobId);
        jobInstanceDTO.setWsFlinkKubernetesJob(jobDTO);
        return flinkJobInstanceConverterFactory.convert(jobInstanceDTO);
    }

    @Override
    public String asYaml(Long id) throws Exception {
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(id);
        return flinkJobInstanceConverterFactory.convert(jobInstanceDTO);
    }

    @Override
    public void deploy(WsFlinkKubernetesJobInstanceDeployParam param) throws Exception {
        WsFlinkKubernetesJobInstance record = new WsFlinkKubernetesJobInstance();
        BeanUtils.copyProperties(param, record);
        record.setInstanceId(UUIDUtil.randomUUId());
        if (param.getJobManager() != null) {
            record.setJobManager(JacksonUtil.toJsonString(param.getJobManager()));
        }
        if (param.getTaskManager() != null) {
            record.setTaskManager(JacksonUtil.toJsonString(param.getTaskManager()));
        }
        if (param.getUserFlinkConfiguration() != null) {
            record.setUserFlinkConfiguration(JacksonUtil.toJsonString(param.getUserFlinkConfiguration()));
        }
        WsFlinkKubernetesJobDTO jobDTO = wsFlinkKubernetesJobService.selectOne(param.getWsFlinkKubernetesJobId());
        Long clusterCredentialId = null;
        String resource = null;
        WatchCallbackHandler callbackHandler = null;
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                clusterCredentialId = jobDTO.getFlinkDeployment().getClusterCredentialId();
                Map<String, String> flinkConfiguration = jobDTO.getFlinkDeployment().getFlinkConfiguration();
                Map<String, String> mergedFlinkConfiguration = JsonMerger.merge(flinkConfiguration, param.getUserFlinkConfiguration(), Map.class);
                record.setMergedFlinkConfiguration(JacksonUtil.toJsonString(mergedFlinkConfiguration));
                resource = Constant.FLINK_DEPLOYMENT;
                callbackHandler = flinkDeploymentWatchCallbackHandler;
                break;
            case FLINK_SESSION_JOB:
                clusterCredentialId = jobDTO.getFlinkSessionCluster().getClusterCredentialId();
                resource = Constant.FLINK_SESSION_JOB;
                callbackHandler = null;
                break;
            default:
        }
        wsFlinkKubernetesJobInstanceMapper.insert(record);

        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(record.getId());
        String yaml = asYaml(record.getId());
        flinkKubernetesOperatorService.applyJob(clusterCredentialId, yaml);
        // add watch
        Map<String, String> lables = metadataHandler.generateLables(jobInstanceDTO);
        flinkKubernetesOperatorService.addWatch(clusterCredentialId, resource, lables, callbackHandler);
    }

    @Override
    public void shutdown(WsFlinkKubernetesJobInstanceShutdownParam param) throws Exception {
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(param.getId());
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();
        String yaml = asYaml(param.getId());
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), yaml);
                return;
            case FLINK_SESSION_JOB:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), yaml);
                return;
            default:
        }
    }

    @Override
    public void restart(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id).getWsFlinkKubernetesJob();
        Optional<GenericKubernetesResource> optional = getJobWithoutStatus(id);
        if (optional.isEmpty()) {
            return;
        }
        GenericKubernetesResource genericKubernetesResource = optional.get();
        Object spec = genericKubernetesResource.get("spec");
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                String deploymentJson = JacksonUtil.toJsonString(spec);
                FlinkDeploymentSpec flinkDeploymentSpec = JacksonUtil.parseJsonString(deploymentJson, FlinkDeploymentSpec.class);
                flinkDeploymentSpec.setRestartNonce(System.currentTimeMillis());
                genericKubernetesResource.setAdditionalProperty("spec", flinkDeploymentSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            case FLINK_SESSION_JOB:
                String sessionJobJson = JacksonUtil.toJsonString(spec);
                FlinkSessionJobSpec flinkSessionJobSpec = JacksonUtil.parseJsonString(sessionJobJson, FlinkSessionJobSpec.class);
                flinkSessionJobSpec.setRestartNonce(System.currentTimeMillis());
                genericKubernetesResource.setAdditionalProperty("spec", flinkSessionJobSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            default:
        }
    }

    @Override
    public void triggerSavepoint(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id).getWsFlinkKubernetesJob();
        Optional<GenericKubernetesResource> optional = getJobWithoutStatus(id);
        if (optional.isEmpty()) {
            return;
        }
        GenericKubernetesResource genericKubernetesResource = optional.get();
        Object spec = genericKubernetesResource.get("spec");
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                String deploymentJson = JacksonUtil.toJsonString(spec);
                FlinkDeploymentSpec flinkDeploymentSpec = JacksonUtil.parseJsonString(deploymentJson, FlinkDeploymentSpec.class);
                flinkDeploymentSpec.getJob().setSavepointTriggerNonce(System.currentTimeMillis());
                genericKubernetesResource.setAdditionalProperty("spec", flinkDeploymentSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            case FLINK_SESSION_JOB:
                String sessionJobJson = JacksonUtil.toJsonString(spec);
                FlinkSessionJobSpec flinkSessionJobSpec = JacksonUtil.parseJsonString(sessionJobJson, FlinkSessionJobSpec.class);
                flinkSessionJobSpec.getJob().setSavepointTriggerNonce(System.currentTimeMillis());
                genericKubernetesResource.setAdditionalProperty("spec", flinkSessionJobSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            default:
        }
    }

    @Override
    public void suspend(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id).getWsFlinkKubernetesJob();
        Optional<GenericKubernetesResource> optional = getJobWithoutStatus(id);
        if (optional.isEmpty()) {
            return;
        }
        GenericKubernetesResource genericKubernetesResource = optional.get();
        Object spec = genericKubernetesResource.get("spec");
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                String deploymentJson = JacksonUtil.toJsonString(spec);
                FlinkDeploymentSpec flinkDeploymentSpec = JacksonUtil.parseJsonString(deploymentJson, FlinkDeploymentSpec.class);
                flinkDeploymentSpec.getJob().setState(JobState.SUSPENDED);
                genericKubernetesResource.setAdditionalProperty("spec", flinkDeploymentSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            case FLINK_SESSION_JOB:
                String sessionJobJson = JacksonUtil.toJsonString(spec);
                FlinkSessionJobSpec flinkSessionJobSpec = JacksonUtil.parseJsonString(sessionJobJson, FlinkSessionJobSpec.class);
                flinkSessionJobSpec.getJob().setState(JobState.SUSPENDED);
                genericKubernetesResource.setAdditionalProperty("spec", flinkSessionJobSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            default:
        }
    }

    @Override
    public void resume(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id).getWsFlinkKubernetesJob();
        Optional<GenericKubernetesResource> optional = getJobWithoutStatus(id);
        if (optional.isEmpty()) {
            return;
        }
        GenericKubernetesResource genericKubernetesResource = optional.get();
        Object spec = genericKubernetesResource.get("spec");
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                String deploymentJson = JacksonUtil.toJsonString(spec);
                FlinkDeploymentSpec flinkDeploymentSpec = JacksonUtil.parseJsonString(deploymentJson, FlinkDeploymentSpec.class);
                flinkDeploymentSpec.getJob().setState(JobState.RUNNING);
                genericKubernetesResource.setAdditionalProperty("spec", flinkDeploymentSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            case FLINK_SESSION_JOB:
                String sessionJobJson = JacksonUtil.toJsonString(spec);
                FlinkSessionJobSpec flinkSessionJobSpec = JacksonUtil.parseJsonString(sessionJobJson, FlinkSessionJobSpec.class);
                flinkSessionJobSpec.getJob().setState(JobState.RUNNING);
                genericKubernetesResource.setAdditionalProperty("spec", flinkSessionJobSpec);
                flinkKubernetesOperatorService.applyJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), Serialization.asYaml(genericKubernetesResource));
                return;
            default:
        }
    }

    @Override
    public Optional<GenericKubernetesResource> getStatus(Long id) {
        try {
            WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(id);
            return flinkKubernetesOperatorService.getJob(jobInstanceDTO);
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
    public Optional<GenericKubernetesResource> getJobWithoutStatus(Long id) {
        Optional<GenericKubernetesResource> optional = getStatusWithoutManagedFields(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        GenericKubernetesResource status = optional.get();
        GenericKubernetesResourceBuilder builder = new GenericKubernetesResourceBuilder(status);
        builder.removeFromAdditionalProperties("status");
        return Optional.of(builder.build());
    }

    @Override
    public int updateStatus(Long id, FlinkDeploymentStatus status) {
        if (status == null) {
            return -1;
        }
        WsFlinkKubernetesJobInstance record = WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDo(selectOne(id));
        record.setState(EnumUtils.getEnum(ResourceLifecycleState.class, status.getLifecycleState().name()));
        if (status.getJobStatus() != null) {
            JobStatus jobStatus = status.getJobStatus();
            if (jobStatus.getState() != null) {
                record.setJobState(FlinkJobState.of(jobStatus.getState()));
            }
            if (record.getJobState() == FlinkJobState.FAILED
                    || record.getJobState() == FlinkJobState.CANCELED
                    || record.getJobState() == FlinkJobState.FINISHED
                    || record.getJobState() == FlinkJobState.SUSPENDED) {
                record.setEndTime(new Date(Long.parseLong(jobStatus.getUpdateTime())));
            }
            if (jobStatus.getStartTime() != null) {
                record.setStartTime(new Date(Long.parseLong(jobStatus.getStartTime())));
                record.setDuration(Long.parseLong(jobStatus.getUpdateTime()) - Long.parseLong(jobStatus.getStartTime()));
            }
        }
        record.setError(status.getError());
        if (CollectionUtils.isEmpty(status.getClusterInfo())) {
            record.setClusterInfo(null);
        } else {
            record.setClusterInfo(JacksonUtil.toJsonString(status.getClusterInfo()));
        }
        if (status.getTaskManager() == null) {
            record.setTaskManagerInfo(null);
        } else {
            record.setTaskManagerInfo(JacksonUtil.toJsonString(status.getTaskManager()));
        }
        updateSavepoint(id, status);
        return wsFlinkKubernetesJobInstanceMapper.updateById(record);
    }

    @Override
    public int updateStatus(Long id, FlinkSessionJobStatus status) {
        if (status == null) {
            return -1;
        }
        WsFlinkKubernetesJobInstance record = WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDo(selectOne(id));
        record.setState(EnumUtils.getEnum(ResourceLifecycleState.class, status.getLifecycleState().name()));
        if (status.getJobStatus() != null) {
            JobStatus jobStatus = status.getJobStatus();
            if (jobStatus.getState() != null) {
                record.setJobState(FlinkJobState.of(jobStatus.getState()));
            }
            if (record.getJobState() == FlinkJobState.FAILED
                    || record.getJobState() == FlinkJobState.CANCELED
                    || record.getJobState() == FlinkJobState.FINISHED
                    || record.getJobState() == FlinkJobState.SUSPENDED) {
                record.setEndTime(new Date(Long.parseLong(jobStatus.getUpdateTime())));
            }
            if (jobStatus.getStartTime() != null) {
                record.setStartTime(new Date(Long.parseLong(jobStatus.getStartTime())));
                record.setDuration(Long.parseLong(jobStatus.getUpdateTime()) - Long.parseLong(jobStatus.getStartTime()));
            }
        }
        record.setError(status.getError());
        updateSavepoint(id, status);
        return wsFlinkKubernetesJobInstanceMapper.updateById(record);
    }

    private void updateSavepoint(Long id, CommonStatus status) {
        SavepointInfo savepointInfo = status.getJobStatus().getSavepointInfo();
        if (savepointInfo == null) {
            return;
        }
        if (CollectionUtils.isEmpty(savepointInfo.getSavepointHistory()) == false) {
            for (Savepoint savepoint : savepointInfo.getSavepointHistory()) {
                WsFlinkKubernetesJobInstanceSavepoint record = new WsFlinkKubernetesJobInstanceSavepoint();
                record.setWsFlinkKubernetesJobInstanceId(id);
                record.setTimeStamp(savepoint.getTimeStamp());
                record.setLocation(savepoint.getLocation());
                record.setTriggerType(EnumUtils.getEnum(SavepointTriggerType.class, savepoint.getTriggerType().name()));
                record.setFormatType(EnumUtils.getEnum(SavepointFormatType.class, savepoint.getFormatType().name()));

                LambdaQueryWrapper<WsFlinkKubernetesJobInstanceSavepoint> queryWrapper = Wrappers.lambdaQuery(WsFlinkKubernetesJobInstanceSavepoint.class)
                        .eq(WsFlinkKubernetesJobInstanceSavepoint::getWsFlinkKubernetesJobInstanceId, id)
                        .eq(WsFlinkKubernetesJobInstanceSavepoint::getTimeStamp, savepoint.getTimeStamp());
                WsFlinkKubernetesJobInstanceSavepoint oldRecord = wsFlinkKubernetesJobInstanceSavepointMapper.selectOne(queryWrapper);
                if (oldRecord == null) {
                    wsFlinkKubernetesJobInstanceSavepointMapper.insert(record);
                }
            }
        }
    }

    @Override
    public int clearStatus(Long id) {
        WsFlinkKubernetesJobInstance record = WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDo(selectOne(id));
        record.setState(null);
        record.setError(null);
        record.setClusterInfo(null);
        record.setTaskManagerInfo(null);
        if (record.getStartTime() != null && record.getEndTime() == null) {
            record.setEndTime(new Date());
            record.setDuration(System.currentTimeMillis() - record.getStartTime().getTime());
        }
        return wsFlinkKubernetesJobInstanceMapper.updateById(record);
    }
}
