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
import cn.sliew.scaleph.engine.flink.service.WsFlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.dto.KubernetesOptionsDTO;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkClusterConfigParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Tag(name = "Flink管理-集群配置管理")
@RestController
@RequestMapping(path = "/api/flink/cluster-config")
public class WsClusterConfigController {

    @Autowired
    private WsFlinkClusterConfigService wsFlinkClusterConfigService;

    @Logging
    @GetMapping
    @Operation(summary = "查询集群配置列表", description = "分页查询集群配置列表")
    public ResponseEntity<Page<WsFlinkClusterConfigDTO>> list(@Valid WsFlinkClusterConfigParam param) {
        Page<WsFlinkClusterConfigDTO> page = wsFlinkClusterConfigService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}")
    @Operation(summary = "查询集群配置", description = "查询集群配置")
    public ResponseEntity<WsFlinkClusterConfigDTO> selectOne(@PathVariable("id") Long id) {
        final WsFlinkClusterConfigDTO result = wsFlinkClusterConfigService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增集群配置", description = "新增集群配置")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkClusterConfigDTO param) {
        wsFlinkClusterConfigService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/kubernetes")
    @Operation(summary = "修改 kubernetes 配置", description = "修改 kubernetes 配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody KubernetesOptionsDTO options) {
        wsFlinkClusterConfigService.updateKubernetesOptions(id, options);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}/flink")
    @Operation(summary = "修改 flink 配置", description = "修改 flink 配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @RequestBody Map<String, String> options) {
        wsFlinkClusterConfigService.updateFlinkConfig(id, options);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改集群配置", description = "修改集群配置")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkClusterConfigDTO param) {
        wsFlinkClusterConfigService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除集群配置", description = "删除集群配置")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        //todo check if exists cluster instance
        wsFlinkClusterConfigService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除集群", description = "批量删除集群")
    public ResponseEntity<ResponseVO> deleteCluster(@RequestBody List<Long> ids) {
        wsFlinkClusterConfigService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
