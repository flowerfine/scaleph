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

package cn.sliew.scaleph.api.controller.flink;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptions;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterConfigAddParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkClusterConfigListParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "Flink管理-集群配置管理")
@RestController
@RequestMapping(path = "/api/flink/cluster-config")
public class ClusterConfigController {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询集群配置列表", notes = "分页查询集群配置列表")
    public ResponseEntity<Page<FlinkClusterConfigDTO>> list(@Valid FlinkClusterConfigListParam param) {
        Page<FlinkClusterConfigDTO> page = flinkClusterConfigService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping({"{id}"})
    @ApiOperation(value = "查询集群配置", notes = "查询集群配置")
    public ResponseEntity<FlinkClusterConfigDTO> selectOne(@PathVariable("id") Long id) {
        final FlinkClusterConfigDTO result = flinkClusterConfigService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增集群配置", notes = "新增集群配置")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody FlinkClusterConfigAddParam param) {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(flinkClusterConfigDTO), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/kubernetes")
    @ApiOperation(value = "修改 kubernetes 配置", notes = "修改 kubernetes 配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody KubernetesOptions options) {
        flinkClusterConfigService.updateKubernetesOptions(id, options);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/flink")
    @ApiOperation(value = "修改 flink 配置", notes = "修改 flink 配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @RequestBody Map<String, String> options) {
        flinkClusterConfigService.updateFlinkConfig(id, options);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @ApiOperation(value = "修改集群配置", notes = "修改集群配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody FlinkClusterConfigDTO param) {
        param.setId(id);
        flinkClusterConfigService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除集群配置", notes = "删除集群配置")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        flinkClusterConfigService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除集群", notes = "批量删除集群")
    public ResponseEntity<ResponseVO> deleteCluster(@RequestBody List<Long> ids) {
        flinkClusterConfigService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
