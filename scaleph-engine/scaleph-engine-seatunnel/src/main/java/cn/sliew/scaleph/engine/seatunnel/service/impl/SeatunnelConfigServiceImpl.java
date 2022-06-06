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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.JobAttrTypeEnum;
import cn.sliew.scaleph.core.di.service.dto.*;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnector;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.JobNameProperties;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static cn.sliew.scaleph.engine.seatunnel.service.util.GraphConstants.*;

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

    private MutableGraph<ObjectNode> buildGraph(DiJobDTO diJobDTO) {
        MutableGraph<ObjectNode> graph = GraphBuilder.directed().build();
        List<DiJobStepDTO> jobStepList = diJobDTO.getJobStepList();
        List<DiJobLinkDTO> jobLinkList = diJobDTO.getJobLinkList();
        if (CollectionUtil.isNotEmpty(jobStepList) && CollectionUtil.isNotEmpty(jobLinkList)) {
            Map<String, ObjectNode> stepMap = new HashMap<>();
            for (DiJobStepDTO step : jobStepList) {
                // todo 组合数据源参数和插件本身参数
                Properties properties = mergeJobAttrs(step);
                SeatunnelNativeFlinkConnector connector = seatunnelConnectorService.newConnector(step.getStepName(), properties);
                ObjectNode connectorConf = connector.createConf();
                connectorConf.put(PLUGIN_NAME, step.getStepName());
                connectorConf.put(NODE_TYPE, step.getStepType().getValue());
                connectorConf.put(NODE_ID, step.getId());
                stepMap.put(step.getStepCode(), connectorConf);
                graph.addNode(connectorConf);
            }

            jobLinkList.forEach(link -> {
                String from = link.getFromStepCode();
                String to = link.getToStepCode();
                graph.putEdge(stepMap.get(from), stepMap.get(to));
            });
        }

        return graph;
    }

    private Properties mergeJobAttrs(DiJobStepDTO step) {
        Properties properties = new Properties();
        List<DiJobStepAttrDTO> stepAttrList = step.getJobStepAttrList();
        if (CollectionUtil.isNotEmpty(stepAttrList)) {
            stepAttrList.forEach(attr -> {
                //resolve datasource
                if (Constants.JOB_STEP_ATTR_DATASOURCE.equals(attr.getStepAttrKey())) {
                    DictVO dsAttr = JSONUtil.toBean(attr.getStepAttrValue(), DictVO.class);
                    MetaDatasourceDTO datasourceDTO =
                            metaDatasourceService.selectOne(Long.parseLong(dsAttr.getValue()));
                    properties.putAll(datasourceDTO.getProps());
                    properties.putAll(datasourceDTO.getAdditionalProps());
                } else {
                    properties.put(attr.getStepAttrKey(), attr.getStepAttrValue());
                }
            });
        }
        return properties;
    }
}
