package cn.sliew.scaleph.api.controller.flink;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.engine.flink.service.FlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarUploadParam;
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

@Slf4j
@Api(tags = "Flink管理-artifact-jar")
@RestController
@RequestMapping(path = "/api/flink/artifact/jar")
public class ArtifactJarController {

    @Autowired
    private FlinkArtifactJarService flinkArtifactJarService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 artifact jar 列表", notes = "查询 artifact jar 列表")
    public ResponseEntity<Page<FlinkArtifactJarDTO>> list(@Valid FlinkArtifactJarListParam param) {
        final Page<FlinkArtifactJarDTO> result = flinkArtifactJarService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/artifact/{id}")
    @ApiOperation(value = "根据artifact id查询jar列表",notes = "根据artifact id查询jar列表")
    public ResponseEntity<List<FlinkArtifactJarDTO>> listByArtifactId(@PathVariable("id") Long id) {
        final List<FlinkArtifactJarDTO> list = flinkArtifactJarService.listByArtifactId(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @GetMapping("{id}")
    @ApiOperation(value = "查询 artifact jar 详情", notes = "查询 artifact jar 详情")
    public ResponseEntity<FlinkArtifactJarDTO> selectOne(@PathVariable("id") Long id) {
        final FlinkArtifactJarDTO result = flinkArtifactJarService.selectOne(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "上传 artifact jar", notes = "上传artifact jar")
    public ResponseEntity<ResponseVO> upload(@Valid FlinkArtifactJarUploadParam param, @RequestPart("file") MultipartFile file) throws IOException {
        flinkArtifactJarService.upload(param, file);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping("/download/{id}")
    @ApiOperation(value = "下载 artifact jar", notes = "下载 artifact jar")
    public ResponseEntity<ResponseVO> download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            final String fileName = flinkArtifactJarService.download(id, outputStream);
            response.setCharacterEncoding("utf-8");// 设置字符编码
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8")); // 设置响应头
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
