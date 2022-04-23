package cn.sliew.breeze.api.controller.di;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.sliew.breeze.api.annotation.Logging;
import cn.sliew.breeze.api.util.I18nUtil;
import cn.sliew.breeze.api.vo.ResponseVO;
import cn.sliew.breeze.common.enums.ErrorShowTypeEnum;
import cn.sliew.breeze.common.enums.ResponseCodeEnum;
import cn.sliew.breeze.service.di.DiDirectoryService;
import cn.sliew.breeze.service.di.DiJobService;
import cn.sliew.breeze.service.dto.di.DiDirectoryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "数据集成-目录管理")
@RestController
@RequestMapping(path = "/api/di/dir")
public class DiDirectoryController {

    @Autowired
    private DiDirectoryService diDirectoryService;
    @Autowired
    private DiJobService jobService;

    @Logging
    @GetMapping(path = "/{projectId}")
    @ApiOperation(value = "查询项目目录树", notes = "查询项目目录树")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_DIR_SELECT)")
    public ResponseEntity<List<Tree<Long>>> listProjectDir(@PathVariable(value = "projectId") Long projectId) {
        List<DiDirectoryDTO> list = this.diDirectoryService.selectByProjectId(projectId);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setParentIdKey("pid");
        treeNodeConfig.setNameKey("directoryName");
        treeNodeConfig.setWeightKey("directoryName");
        List<Tree<Long>> treeList = TreeUtil.build(list, 0L, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getPid());
            tree.setName(treeNode.getDirectoryName());
            tree.setWeight(treeNode.getDirectoryName());
            if (treeNode.getPid() == 0L) {
                //默认展开顶级目录
                tree.putExtra("open", "true");
            }
        });
        return new ResponseEntity<>(treeList, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增目录", notes = "新增目录")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_DIR_ADD)")
    public ResponseEntity<ResponseVO> addDirectory(@Validated @RequestBody DiDirectoryDTO directoryDTO) {
        this.diDirectoryService.insert(directoryDTO);
        return new ResponseEntity<>(ResponseVO.sucess(directoryDTO.getId()), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改目录", notes = "修改目录")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_DIR_EDIT)")
    public ResponseEntity<ResponseVO> editDirectory(@Validated @RequestBody DiDirectoryDTO directoryDTO) {
        this.diDirectoryService.update(directoryDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除目录", notes = "删除目录")
    @PreAuthorize("@svs.validate(T(cn.sliew.breeze.common.constant.PrivilegeConstants).STUDIO_DIR_DELETE)")
    public ResponseEntity<ResponseVO> deleteDirectory(@PathVariable(value = "id") Long id) {
        DiDirectoryDTO dir = this.diDirectoryService.selectById(id);
        if (dir != null && (this.diDirectoryService.hasChildDir(dir.getProjectId(), dir.getId())
                || this.jobService.hasValidJob(dir.getProjectId(), dir.getId()))) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.notEmptyDirectory"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diDirectoryService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
