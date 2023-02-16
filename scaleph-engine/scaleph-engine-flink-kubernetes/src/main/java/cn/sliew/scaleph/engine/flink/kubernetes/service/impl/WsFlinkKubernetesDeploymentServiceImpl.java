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

package cn.sliew.scaleph.engine.flink.kubernetes.service.impl;

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesDeployment;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesDeploymentMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobState;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.FlinkDeployment;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesDeploymentService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesDeploymentConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesDeploymentListParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesDeploymentServiceImpl implements WsFlinkKubernetesDeploymentService, InitializingBean, DisposableBean {

    private KubernetesClient client;

    @Autowired
    private WsFlinkKubernetesDeploymentMapper wsFlinkKubernetesDeploymentMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new KubernetesClientBuilder()
                .withConfig(Config.autoConfigure("docker-desktop"))
                .build();
    }

    @Override
    public void destroy() throws Exception {
        client.close();
    }

    @Override
    public Page<WsFlinkKubernetesDeploymentDTO> list(WsFlinkKubernetesDeploymentListParam param) {
        Page<WsFlinkKubernetesDeployment> page = wsFlinkKubernetesDeploymentMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkKubernetesDeployment.class)
                        .eq(param.getKind() != null, WsFlinkKubernetesDeployment::getKind, param.getKind())
                        .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesDeployment::getName, param.getName()));
        Page<WsFlinkKubernetesDeploymentDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkKubernetesDeploymentDTO> dtoList = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkKubernetesDeploymentDTO selectOne(Long id) {
        WsFlinkKubernetesDeployment record = wsFlinkKubernetesDeploymentMapper.selectById(id);
        checkState(record != null, () -> "flink kubernetes deployment not exist for id = " + id);
        return WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(record);
    }

    @Override
    public FlinkDeployment asYaml(Long id) {
        WsFlinkKubernetesDeploymentDTO dto = selectOne(id);
        FlinkDeployment deployment = new FlinkDeployment();
        ObjectMetaBuilder builder = new ObjectMetaBuilder(true);
        builder.withName(dto.getName()).withNamespace(dto.getNamespace());
        deployment.setMetadata(builder.build());
        FlinkDeploymentSpec spec = new FlinkDeploymentSpec();
        KubernetesOptionsVO kuberenetesOptions = dto.getKuberenetesOptions();
        spec.setImage(kuberenetesOptions.getImage());
        spec.setServiceAccount(kuberenetesOptions.getServiceAccount());
        spec.setFlinkVersion(EnumUtils.getEnum(FlinkVersion.class, kuberenetesOptions.getFlinkVersion()));
        spec.setFlinkConfiguration(dto.getFlinkConfiguration());
        spec.setJobManager(dto.getJobManager());
        spec.setTaskManager(dto.getTaskManager());
        spec.setPodTemplate(dto.getPodTemplate());
        spec.setJob(dto.getJob());
        deployment.setSpec(spec);
        return deployment;
    }

    @Override
    public int insert(WsFlinkKubernetesDeploymentDTO dto) {
        WsFlinkKubernetesDeployment record = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesDeploymentMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesDeploymentDTO dto) {
        WsFlinkKubernetesDeployment record = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesDeploymentMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesDeploymentMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesDeploymentMapper.deleteBatchIds(ids);
    }

    @Override
    public void run(Long id) throws Exception {
        FlinkDeployment deployment = asYaml(id);
        client.resource(deployment).createOrReplace();
    }

    @Override
    public void suspend(Long id) throws Exception {
        FlinkDeployment deployment = asYaml(id);
        deployment.getSpec().getJob().setState(JobState.SUSPENDED);
        client.resource(deployment).replace();
    }

    @Override
    public void resume(Long id) throws Exception {
        FlinkDeployment deployment = asYaml(id);
        deployment.getSpec().getJob().setState(JobState.RUNNING);
        client.resource(deployment).replace();
    }

    @Override
    public void shutdown(Long id) throws Exception {
        FlinkDeployment deployment = asYaml(id);
        client.resource(deployment).delete();
    }
}
