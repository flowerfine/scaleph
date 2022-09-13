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
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.engine.flink.service.FlinkJobConfigJarService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobConfigJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobConfigJarListParam;
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

@Slf4j
@Api(tags = "Flink管理-Jar 任务配置")
@RestController
@RequestMapping(path = "/api/flink/job-config/jar")
public class JobConfigJarController {

    @Autowired
    private FlinkJobConfigJarService flinkJobConfigJarService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询任务配置列表", notes = "分页任务配置列表")
    public ResponseEntity<Page<FlinkJobConfigJarDTO>> list(@Valid FlinkJobConfigJarListParam param) {
        Page<FlinkJobConfigJarDTO> page = flinkJobConfigJarService.list(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增任务配置", notes = "新增任务配置")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody FlinkJobConfigJarDTO param) {
        flinkJobConfigJarService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @ApiOperation(value = "修改任务配置", notes = "修改任务配置")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody FlinkJobConfigJarDTO param) {
        param.setId(id);
        flinkJobConfigJarService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除任务配置", notes = "删除任务配置")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) {
        flinkJobConfigJarService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除任务", notes = "批量删除任务")
    public ResponseEntity<ResponseVO> deleteCluster(@RequestBody List<Long> ids) {
        flinkJobConfigJarService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
