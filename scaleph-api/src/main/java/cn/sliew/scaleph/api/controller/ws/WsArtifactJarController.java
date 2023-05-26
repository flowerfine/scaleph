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
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.engine.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarHistoryParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarUpdateParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarUploadParam;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Api(tags = "Flink管理-artifact-jar")
@RestController
@RequestMapping(path = "/api/flink/artifact/jar")
public class WsArtifactJarController {

    @Autowired
    private WsFlinkArtifactJarService wsFlinkArtifactJarService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 artifact jar 列表", notes = "查询 artifact jar 列表")
    public ResponseEntity<Page<WsFlinkArtifactJarDTO>> list(@Valid WsFlinkArtifactJarParam param) {
        Page<WsFlinkArtifactJarDTO> result = wsFlinkArtifactJarService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/all")
    @ApiOperation(value = "根据 id 查询 artifact jar 列表", notes = "根据 id 查询 artifact jar 列表")
    public ResponseEntity<List<WsFlinkArtifactJarDTO>> listAllByArtifact(@RequestParam("id") Long id) {
        final List<WsFlinkArtifactJarDTO> result = wsFlinkArtifactJarService.listAllByArtifact(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/page")
    @ApiOperation(value = "根据 id 分页查询 artifact jar 列表", notes = "根据 id 分页查询 artifact jar 列表")
    public ResponseEntity<Page<WsFlinkArtifactJarDTO>> listByArtifact(@Valid WsFlinkArtifactJarHistoryParam param) {
        final Page<WsFlinkArtifactJarDTO> result = wsFlinkArtifactJarService.listByArtifact(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 artifact jar 详情", notes = "查询 artifact jar 详情")
    public ResponseEntity<WsFlinkArtifactJarDTO> selectOne(@PathVariable("id") Long id) {
        WsFlinkArtifactJarDTO result = wsFlinkArtifactJarService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "上传 artifact jar", notes = "上传 artifact jar")
    public ResponseEntity<ResponseVO> upload(@Valid WsFlinkArtifactJarUploadParam param, @RequestPart("file") MultipartFile file) throws IOException, UidGenerateException {
        if (StringUtils.hasText(param.getJarParams())) {
            Map<String, Object> map = PropertyUtil.formatPropFromStr(param.getJarParams(), "\n", ":");
            param.setJarParams(PropertyUtil.mapToFormatProp(map, "\n", ":"));
        }
        wsFlinkArtifactJarService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping("jar")
    @ApiOperation(value = "修改 artifact jar", notes = "修改 artifact jar")
    public ResponseEntity<ResponseVO> updateJar(@Valid WsFlinkArtifactJarUpdateParam param, @RequestPart(value = "file", required = false) MultipartFile file) throws UidGenerateException, IOException {
        if (StringUtils.hasText(param.getJarParams())) {
            Map<String, Object> map = PropertyUtil.formatPropFromStr(param.getJarParams(), "\n", ":");
            param.setJarParams(PropertyUtil.mapToFormatProp(map, "\n", ":"));
        }
        this.wsFlinkArtifactJarService.update(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/download/{id}")
    @ApiOperation(value = "下载 artifact jar", notes = "下载 artifact jar")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            String fileName = wsFlinkArtifactJarService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除 artifact jar", notes = "删除 artifact jar")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws ScalephException, IOException {
        wsFlinkArtifactJarService.deleteOne(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("/all/{id}")
    @ApiOperation(value = "删除所有 artifact jar", notes = "删除所有 artifact jar")
    public ResponseEntity<ResponseVO> deleteAll(@PathVariable("id") Long id) throws IOException, ScalephException {
        wsFlinkArtifactJarService.deleteAll(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }
}
