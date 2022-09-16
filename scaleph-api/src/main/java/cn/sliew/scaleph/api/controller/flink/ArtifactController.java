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
import cn.sliew.scaleph.engine.flink.service.FlinkArtifactService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactListParam;
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
@Api(tags = "Flink管理-artifact管理")
@RestController
@RequestMapping(path = "/api/flink/artifact")
public class ArtifactController {

    @Autowired
    private FlinkArtifactService flinkArtifactService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 artifact 列表", notes = "查询 artifact 列表")
    public ResponseEntity<Page<FlinkArtifactDTO>> list(@Valid FlinkArtifactListParam param) {
        final Page<FlinkArtifactDTO> result = flinkArtifactService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "新增 artifact", notes = "新增 artifact")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody FlinkArtifactDTO param) {
        flinkArtifactService.insert(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("{id}")
    @ApiOperation(value = "修改 artifact", notes = "修改 artifact")
    public ResponseEntity<ResponseVO> update(@PathVariable("id") Long id, @Valid @RequestBody FlinkArtifactDTO param) {
        param.setId(id);
        flinkArtifactService.update(param);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @ApiOperation(value = "删除 artifact", notes = "删除 artifact")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) {
        flinkArtifactService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/batch")
    @ApiOperation(value = "批量删除 artifact", notes = "批量删除 artifact")
    public ResponseEntity<ResponseVO> deleteBatch(@RequestBody List<Long> ids) {
        flinkArtifactService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
