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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.scaleph.engine.flink.service.WsFlinkService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WsFlinkServiceImpl implements WsFlinkService {

    @Override
    public void createSessionCluster(Long projectId, Long flinkClusterConfigId) throws Exception {

    }

    @Override
    public void submit(WsFlinkJobDTO wsFlinkJobDTO) throws Exception {

    }

    @Override
    public void shutdown(Long id) throws Exception {

    }

    @Override
    public void shutdownBatch(List<Long> ids) throws Exception {

    }

    @Override
    public void stop(Long id) throws Exception {

    }

    @Override
    public void cancel(Long id) throws Exception {

    }

    @Override
    public void triggerSavepoint(Long id) throws Exception {

    }
}
