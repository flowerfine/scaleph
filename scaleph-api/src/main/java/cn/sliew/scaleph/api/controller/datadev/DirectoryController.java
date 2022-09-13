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

package cn.sliew.scaleph.api.controller.datadev;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.core.di.service.DiDirectoryService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;
import cn.sliew.scaleph.system.util.I18nUtil;
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
@Api(tags = "数据开发-目录管理")
@RestController
@RequestMapping(path = {"/api/datadev/dir", "/api/di/dir"})
public class DirectoryController {

    @Autowired
    private DiDirectoryService diDirectoryService;
    @Autowired
    private DiJobService jobService;

    @Logging
    @GetMapping(path = "/{projectId}")
    @ApiOperation(value = "查询项目目录树", notes = "查询项目目录树")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DIR_SELECT)")
    public ResponseEntity<List<Tree<Long>>> listProjectDir(
            @PathVariable(value = "projectId") Long projectId) {
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
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DIR_ADD)")
    public ResponseEntity<ResponseVO> addDirectory(
            @Validated @RequestBody DiDirectoryDTO directoryDTO) {
        this.diDirectoryService.insert(directoryDTO);
        return new ResponseEntity<>(ResponseVO.sucess(directoryDTO.getId()), HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改目录", notes = "修改目录")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DIR_EDIT)")
    public ResponseEntity<ResponseVO> editDirectory(
            @Validated @RequestBody DiDirectoryDTO directoryDTO) {
        this.diDirectoryService.update(directoryDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除目录", notes = "删除目录")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DIR_DELETE)")
    public ResponseEntity<ResponseVO> deleteDirectory(@PathVariable(value = "id") Long id) {
        DiDirectoryDTO dir = this.diDirectoryService.selectById(id);
        if (dir != null && (this.diDirectoryService.hasChildDir(dir.getProjectId(), dir.getId())
                || this.jobService.hasValidJob(dir.getProjectId(), dir.getId()))) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.notEmptyDirectory"),
                    ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diDirectoryService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

}
