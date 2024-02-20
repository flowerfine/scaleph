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

package cn.sliew.scaleph.workspace.flink.service.impl;

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkCheckpoint;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkCheckpointMapper;
import cn.sliew.scaleph.workspace.flink.service.WsFlinkCheckpointService;
import cn.sliew.scaleph.workspace.flink.service.convert.WsFlinkCheckpointConvert;
import cn.sliew.scaleph.workspace.flink.service.dto.WsFlinkCheckpointDTO;
import cn.sliew.scaleph.workspace.flink.service.param.WsFlinkCheckpointListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WsFlinkCheckpointServiceImpl implements WsFlinkCheckpointService {

    @Autowired
    private WsFlinkCheckpointMapper wsFlinkCheckpointMapper;

    @Override
    public Page<WsFlinkCheckpointDTO> list(WsFlinkCheckpointListParam param) {
        Page<WsFlinkCheckpoint> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkCheckpoint> queryWrapper = Wrappers.lambdaQuery(WsFlinkCheckpoint.class)
                .eq(WsFlinkCheckpoint::getFlinkJobInstanceId, param.getFlinkJobInstanceId());
        Page<WsFlinkCheckpoint> wsFlinkCheckpointPage = wsFlinkCheckpointMapper.selectPage(page, queryWrapper);
        Page<WsFlinkCheckpointDTO> result = new Page<>(wsFlinkCheckpointPage.getCurrent(), wsFlinkCheckpointPage.getSize(), wsFlinkCheckpointPage.getTotal());
        List<WsFlinkCheckpointDTO> wsFlinkCheckpointDTOS = WsFlinkCheckpointConvert.INSTANCE.toDto(wsFlinkCheckpointPage.getRecords());
        result.setRecords(wsFlinkCheckpointDTOS);
        return result;
    }
    
}
