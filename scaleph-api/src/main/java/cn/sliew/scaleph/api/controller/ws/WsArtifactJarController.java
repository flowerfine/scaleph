package cn.sliew.scaleph.api.controller.ws;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.param.PropertyUtil;
import cn.sliew.scaleph.engine.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarParam;
import cn.sliew.scaleph.system.vo.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Slf4j
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
        final Page<WsFlinkArtifactJarDTO> result = wsFlinkArtifactJarService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/artifact/{id}")
    @ApiOperation(value = "根据id查询 artifact jar 列表", notes = "根据id查询 artifact jar 列表")
    public ResponseEntity<List<WsFlinkArtifactJarDTO>> listByArtifact(@PathVariable("id") Long id) {
        final List<WsFlinkArtifactJarDTO> result = wsFlinkArtifactJarService.listByArtifact(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Logging
    @GetMapping("/{id}")
    @ApiOperation(value = "查询 artifact jar 详情", notes = "查询 artifact jar 详情")
    public ResponseEntity<WsFlinkArtifactJarDTO> selectOne(@PathVariable("id") Long id) {
        final WsFlinkArtifactJarDTO result = wsFlinkArtifactJarService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "上传 artifact jar", notes = "上传artifact jar")
    public ResponseEntity<ResponseVO> upload(@Valid WsFlinkArtifactJarDTO param, @RequestPart("file") MultipartFile file) throws IOException {
        if (StrUtil.isNotBlank(param.getJarParams())) {
            Map<String, Object> map = PropertyUtil.formatPropFromStr(param.getJarParams(), "\n", ":");
            param.setJarParams(PropertyUtil.mapToFormatProp(map, "\n", ":"));
        }
        wsFlinkArtifactJarService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/download/{id}")
    @ApiOperation(value = "下载 artifact jar", notes = "下载 artifact jar")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String fileName = wsFlinkArtifactJarService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "修改 artifact jar", notes = "修改 artifact jar")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkArtifactJarDTO param) {
        if (StrUtil.isNotBlank(param.getJarParams())) {
            Map<String, Object> map = PropertyUtil.formatPropFromStr(param.getJarParams(), "\n", ":");
            param.setJarParams(PropertyUtil.mapToFormatProp(map, "\n", ":"));
        }
        this.wsFlinkArtifactJarService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping
    @ApiOperation(value = "删除 artifact jar", notes = "删除 artifact jar")
    public ResponseEntity<ResponseVO> delete(@PathVariable("id") Long id) throws ScalephException {
        wsFlinkArtifactJarService.deleteOne(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
