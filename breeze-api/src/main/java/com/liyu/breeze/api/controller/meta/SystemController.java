package com.liyu.breeze.api.controller.meta;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liyu.breeze.api.annotation.Logging;
import com.liyu.breeze.api.vo.ResponseVO;
import com.liyu.breeze.service.meta.MetaSystemService;
import com.liyu.breeze.service.dto.meta.MetaSystemDTO;
import com.liyu.breeze.service.param.meta.MetaSystemParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author gleiyu
 */
@Slf4j
@Api(tags = "数据标准-业务系统")
@RestController
@RequestMapping(path = "/api/meta/system")
public class SystemController {

    @Autowired
    private MetaSystemService metaSystemService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询业务系统", notes = "分页查询业务系统信息")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).STDATA_SYSTEM_SELECT)")
    public ResponseEntity<Page<MetaSystemDTO>> listMetaSystem(MetaSystemParam param) {
        Page<MetaSystemDTO> page = this.metaSystemService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增数据源", notes = "新增数据源")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).STDATA_SYSTEM_ADD)")
    public ResponseEntity<ResponseVO> addMetaSystem(@Validated @RequestBody MetaSystemDTO metaSystemDTO) {
        this.metaSystemService.insert(metaSystemDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改数据源", notes = "修改数据源")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).STDATA_SYSTEM_EDIT)")
    public ResponseEntity<ResponseVO> editMetaSystem(@Validated @RequestBody MetaSystemDTO metaSystemDTO) {
        this.metaSystemService.update(metaSystemDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除数据源", notes = "删除数据源")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@PathVariable(value = "id") String id) {
        this.metaSystemService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除数据源", notes = "批量删除数据源")
    @PreAuthorize("@svs.validate(T(com.liyu.breeze.common.constant.PrivilegeConstants).STDATA_SYSTEM_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaSystem(@RequestBody Map<Integer, String> map) {
        this.metaSystemService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
