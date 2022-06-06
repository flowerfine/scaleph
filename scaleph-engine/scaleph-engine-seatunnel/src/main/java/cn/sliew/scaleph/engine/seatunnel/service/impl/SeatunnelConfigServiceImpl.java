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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.JobNameProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SeatunnelConfigServiceImpl implements SeatunnelConfigService {

    private static final Map<String, String> JOB_STEP_MAP = new HashMap<>();
    private static final Map<String, String> PLUGIN_MAP = new HashMap<>();

    static {
        //init job step map
        JOB_STEP_MAP.put("sink-table", "JdbcSink");
        JOB_STEP_MAP.put("source-table", "JdbcSource");
        //init plugin map
        PLUGIN_MAP.put("source-table", "jdbc");
        PLUGIN_MAP.put("sink-table", "jdbc");
        PLUGIN_MAP.put("source-csv", "file");
        PLUGIN_MAP.put("sink-csv", "file");
    }

    @Autowired
    private MetaDatasourceService metaDatasourceService;
    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Override
    public String buildConfig(DiJobDTO diJobDTO) {
        ObjectNode conf = JacksonUtil.createObjectNode();
        conf.put(JobNameProperties.JOB_NAME.getName(), diJobDTO.getJobCode());
        conf.set("env", buildEnv(diJobDTO));

        return conf.toPrettyString();
    }

    private ObjectNode buildEnv(DiJobDTO job) {
        ObjectNode env = JacksonUtil.createObjectNode();
        List<DiJobAttrDTO> jobAttrList = job.getJobAttrList();
        if (CollectionUtils.isEmpty(jobAttrList)) {
            return env;
        }
        for (DiJobAttrDTO attr : jobAttrList) {
            if (JobAttrTypeEnum.JOB_PROP.getValue().equals(attr.getJobAttrType().getValue())) {
                env.put(attr.getJobAttrKey(), attr.getJobAttrValue());
            }
        }
        return env;
    }

}
