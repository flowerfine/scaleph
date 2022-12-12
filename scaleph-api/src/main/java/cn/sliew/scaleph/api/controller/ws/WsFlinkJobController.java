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

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobAttrService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobAttrDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterInstanceService;
import cn.sliew.scaleph.engine.flink.service.WsFlinkJobService;
import cn.sliew.scaleph.engine.flink.service.dto.*;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobListByTypeParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkJobListParam;
import cn.sliew.scaleph.system.snowflake.UidGenerator;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Flink管理-任务管理")
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
    @ApiOperation(value = "查询任务列表", notes = "分页查询任务列表")
    public ResponseEntity<Page<WsFlinkJobDTO>> list(@Valid WsFlinkJobListParam param) {
        Page<WsFlinkJobDTO> page = wsFlinkJobService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增任务", notes = "新增任务")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkJobDTO wsFlinkJobDTO) throws UidGenerateException {
        wsFlinkJobDTO.setCode(defaultUidGenerator.getUID());
        if (FlinkJobType.JAR.equals(wsFlinkJobDTO.getType())) {
            WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO = wsFlinkArtifactJarService.selectOne(wsFlinkJobDTO.getFlinkArtifactId());
            wsFlinkJobDTO.setName(wsFlinkArtifactJarDTO.getFlinkArtifact().getName());
            //todo set job config value
        } else if (FlinkJobType.SEATUNNEL.equals(wsFlinkJobDTO.getType())) {
            WsDiJobDTO wsDiJobDTO = wsDiJobService.selectOne(wsFlinkJobDTO.getFlinkArtifactId());
            wsFlinkJobDTO.setName(wsDiJobDTO.getJobName());
            Map<String, String> jobConfig = new HashMap<>();
            List<WsDiJobAttrDTO> attrList = wsDiJobAttrService.listJobAttr(wsDiJobDTO.getId());
            attrList.stream()
                    .filter(attr -> attr.getJobAttrType().equals(JobAttrType.VARIABLE))
                    .forEach(attr -> jobConfig.put(attr.getJobAttrKey(), attr.getJobAttrValue())
                    );
            wsFlinkJobDTO.setJobConfig(jobConfig);
        }
        WsFlinkClusterInstanceDTO wsFlinkClusterInstanceDTO = wsFlinkClusterInstanceService.selectOne(wsFlinkJobDTO.getFlinkClusterInstanceId());
        wsFlinkJobDTO.setFlinkClusterConfigId(wsFlinkClusterInstanceDTO.getFlinkClusterConfigId());
        //flink config
        WsFlinkClusterConfigDTO wsFlinkClusterConfigDTO = wsFlinkClusterConfigService.selectOne(wsFlinkJobDTO.getFlinkClusterConfigId());
        Map<String, String> flinkConfig = new HashMap<>();
        flinkConfig.putAll(wsFlinkClusterConfigDTO.getConfigOptions());
        flinkConfig.putAll(wsFlinkJobDTO.getFlinkConfig());
        wsFlinkJobDTO.setFlinkConfig(flinkConfig);
        wsFlinkJobService.insert(wsFlinkJobDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


    @Logging
    @PostMapping
    @ApiOperation(value = "修改任务", notes = "修改任务")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkJobDTO param) {
        wsFlinkJobService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("jar")
    @ApiOperation(value = "查询 Jar 任务列表", notes = "分页查询 Jar 任务列表")
    public ResponseEntity<Page<WsFlinkJobForJarDTO>> listJobsForJar(@Valid WsFlinkJobListByTypeParam param) {
        Page<WsFlinkJobForJarDTO> page = wsFlinkJobService.listJobsForJar(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("seatunnel")
    @ApiOperation(value = "查询 SeaTunnel 任务列表", notes = "分页查询 SeaTunnel 任务列表")
    public ResponseEntity<Page<WsFlinkJobForSeaTunnelDTO>> listJobsForSeaTunnel(@Valid WsFlinkJobListByTypeParam param) {
        Page<WsFlinkJobForSeaTunnelDTO> page = wsFlinkJobService.listJobsForSeaTunnel(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

}
