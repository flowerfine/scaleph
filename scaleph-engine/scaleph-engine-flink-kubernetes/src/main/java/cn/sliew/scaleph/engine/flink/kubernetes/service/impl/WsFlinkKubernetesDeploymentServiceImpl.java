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
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesDeploymentService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesDeploymentConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesDeploymentListParam;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesDeploymentServiceImpl implements WsFlinkKubernetesDeploymentService {

    @Autowired
    private WsFlinkKubernetesDeploymentMapper wsFlinkKubernetesDeploymentMapper;

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
}
