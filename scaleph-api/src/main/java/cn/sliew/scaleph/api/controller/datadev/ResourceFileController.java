package cn.sliew.scaleph.api.controller.datadev;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.core.di.service.DiResourceFileService;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.DiResourceFileParam;
import cn.sliew.scaleph.storage.service.StorageService;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Api(tags = "数据开发-资源管理")
@RestController
@RequestMapping(path = "/api/datadev/resource")
public class ResourceFileController {

    @Autowired
    private DiResourceFileService diResourceFileService;

    @Resource(name = "${app.resource.type}")
    private StorageService storageService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询资源列表", notes = "分页查询资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_SELECT)")
    public ResponseEntity<Page<DiResourceFileDTO>> listResource(DiResourceFileParam param) {
        Page<DiResourceFileDTO> page = this.diResourceFileService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/project")
    @ApiOperation(value = "查询项目下资源", notes = "查询项目下资源列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_SELECT)")
    public ResponseEntity<List<DictVO>> listByProject(@NotNull Long projectId) {
        List<DictVO> list = this.diResourceFileService.listByProjectId(projectId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @Logging
    @PostMapping
    @ApiOperation(value = "新增资源", notes = "新增资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> addResource(@NotNull @RequestBody DiResourceFileDTO fileDTO)
        throws IOException {
        DiResourceFileDTO dto = new DiResourceFileDTO();
        dto.setProjectId(fileDTO.getProjectId());
        dto.setFileName(fileDTO.getFileName());
        dto.setFilePath(String.valueOf(fileDTO.getProjectId()));
        dto.resolveFileType(fileDTO.getFileName());
        Long fileSize = this.storageService.getFileSize(dto.getFilePath(), dto.getFileName());
        dto.setFileSize(fileSize);
        this.diResourceFileService.insert(dto);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PostMapping(path = "/upload")
    @ApiOperation(value = "上传资源文件", notes = "上传资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> uploadResource(@NotNull String projectId,
                                                     @RequestParam("file") MultipartFile file)
        throws IOException {
        if (!this.storageService.exists(projectId)) {
            this.storageService.mkdirs(projectId);
        }
        this.storageService.upload(file.getInputStream(), projectId, file.getOriginalFilename());
        return new ResponseEntity<>(ResponseVO.sucess(file.getOriginalFilename()),
            HttpStatus.CREATED);
    }

    @Logging
    @DeleteMapping(path = "/upload")
    @ApiOperation(value = "删除资源文件", notes = "删除资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_ADD)")
    public ResponseEntity<ResponseVO> deleteResource(@NotNull String projectId,
                                                     @NotNull String fileName) throws IOException {
        this.storageService.delete(projectId, fileName);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @GetMapping(path = "/download")
    @ApiOperation(value = "下载资源文件", notes = "下载资源文件")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DOWNLOAD)")
    public void downloadResource(@NotNull String projectId, @NotNull String fileName,
                                 HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = this.storageService.get(projectId, fileName);
        if (is != null) {
            try (BufferedInputStream bis = new BufferedInputStream(is);
                 BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())
            ) {
                FileCopyUtils.copy(bis, bos);
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
    }

    @Logging
    @DeleteMapping
    @ApiOperation(value = "删除资源", notes = "删除资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteResource(@PathVariable(value = "id") Long id) {
        List<DiResourceFileDTO> list =
            this.diResourceFileService.listByIds(Collections.singletonList(id));
        this.diResourceFileService.deleteById(id);
        if (CollectionUtil.isNotEmpty(list)) {
            DiResourceFileDTO resource = list.get(0);
            this.storageService.delete(String.valueOf(resource.getProjectId()),
                resource.getFileName());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除资源", notes = "批量删除资源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_RESOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteResource(@RequestBody Map<Integer, String> map) {
        List<DiResourceFileDTO> list = this.diResourceFileService.listByIds(map.values());
        this.diResourceFileService.deleteBatch(map);
        for (DiResourceFileDTO resource : list) {
            this.storageService.delete(String.valueOf(resource.getProjectId()),
                resource.getFileName());
        }
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
