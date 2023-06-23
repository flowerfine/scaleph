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

package cn.sliew.scaleph.api.controller.ws;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.engine.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkClusterInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkJobDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobListParam;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobAttrService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobAttrDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Flink管理-任务管理")
@RestController
@RequestMapping(path = "/api/flink/job")
public class WsFlinkJobController {

    @Autowired
    private WsFlinkJobService wsFlinkJobService;
    @Autowired
    private UidGenerator defaultUidGenerator;
    @Autowired
    private WsFlinkArtifactJarService wsFlinkArtifactJarService;
    @Autowired
    private WsFlinkClusterInstanceService wsFlinkClusterInstanceService;
    @Autowired
    private WsFlinkClusterConfigService wsFlinkClusterConfigService;
    @Autowired
    private WsDiJobService wsDiJobService;
    @Autowired
    private WsDiJobAttrService wsDiJobAttrService;

    @Logging
    @GetMapping
    @Operation(summary = "查询任务列表", description = "分页查询任务列表")
    public ResponseEntity<Page<WsFlinkJobDTO>> list(@Valid WsFlinkJobListParam param) {
        Page<WsFlinkJobDTO> page = wsFlinkJobService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @Operation(summary = "查询任务信息", description = "查询任务信息")
    public ResponseEntity<ResponseVO<WsFlinkJobDTO>> selectOne(@PathVariable("id") Long id) {
        WsFlinkJobDTO dto = wsFlinkJobService.selectOne(id);
        return new ResponseEntity<ResponseVO<WsFlinkJobDTO>>(ResponseVO.success(dto), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增任务", description = "新增任务")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkJobDTO wsFlinkJobDTO) throws UidGenerateException {
        wsFlinkJobDTO.setCode(defaultUidGenerator.getUID());
        if (FlinkJobType.JAR.equals(wsFlinkJobDTO.getType())) {
            WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO = wsFlinkArtifactJarService.selectOne(wsFlinkJobDTO.getFlinkArtifactId());
            wsFlinkJobDTO.setName(wsFlinkArtifactJarDTO.getWsFlinkArtifact().getName());
//            Map<String, Object> jarParamMap = PropertyUtil.formatPropFromStr(wsFlinkArtifactJarDTO.getJarParams(), "\n", ":");
            Map<String, String> jobConfig = new HashMap<>();
//            jarParamMap.forEach((k, v) -> {
//                jobConfig.put(k, String.valueOf(v));
//            });
            wsFlinkJobDTO.setJobConfig(jobConfig);

        } else if (FlinkJobType.SEATUNNEL.equals(wsFlinkJobDTO.getType())) {
            WsDiJobDTO wsDiJobDTO = wsDiJobService.selectOne(wsFlinkJobDTO.getFlinkArtifactId());
            wsFlinkJobDTO.setName(wsDiJobDTO.getWsFlinkArtifact().getName());
            Map<String, String> jobConfig = new HashMap<>();
            List<WsDiJobAttrDTO> attrList = wsDiJobAttrService.listJobAttr(wsDiJobDTO.getId());
            attrList.stream()
                    .filter(attr -> attr.getJobAttrType().equals(JobAttrType.VARIABLE))
                    .forEach(attr -> jobConfig.put(attr.getJobAttrKey(), attr.getJobAttrValue())
                    );
            wsFlinkJobDTO.setJobConfig(jobConfig);
        }
        WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO = wsFlinkClusterInstanceService.selectOne(wsFlinkJobDTO.getWsFlinkClusterInstance().getId());
        wsFlinkJobDTO.setWsFlinkClusterInstance(wsFlinkClusterInstanceDTO);
        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(wsFlinkClusterInstanceDTO.getFlinkClusterConfigId());
        wsFlinkJobDTO.setWsFlinkClusterConfig(wsFlinkClusterConfigDTO);
        //merge flink config
        Map<String, String> flinkConfig = new HashMap<>();
        if (CollectionUtil.isNotEmpty(wsFlinkClusterConfigDTO.getConfigOptions())) {
            flinkConfig.putAll(wsFlinkClusterConfigDTO.getConfigOptions());
        }
        if (CollectionUtil.isNotEmpty(wsFlinkJobDTO.getFlinkConfig())) {
            flinkConfig.putAll(wsFlinkJobDTO.getFlinkConfig());
        }
        wsFlinkJobDTO.setFlinkConfig(flinkConfig);
        wsFlinkJobService.insert(wsFlinkJobDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }


    @Logging
    @PostMapping
    @Operation(summary = "修改任务", description = "修改任务")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkJobDTO param) {
        wsFlinkJobService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务", description = "删除任务")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        wsFlinkJobService.delete(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
