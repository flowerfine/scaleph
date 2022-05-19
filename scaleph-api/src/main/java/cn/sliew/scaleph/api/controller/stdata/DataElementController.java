package cn.sliew.scaleph.api.controller.stdata;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.meta.service.dto.MetaDataElementDTO;
import cn.sliew.scaleph.service.meta.MetaDataElementService;
import cn.sliew.scaleph.meta.service.param.MetaDataElementParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

@Slf4j
@Api(tags = "数据标准-数据元")
@RestController
@RequestMapping(path = "/api/stdata/element")
public class DataElementController {

    @Autowired
    private MetaDataElementService metaDataElementService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询数据元", notes = "分页查询数据元信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_SELECT)")
    public ResponseEntity<Page<MetaDataElementDTO>> listMetaDataElement(MetaDataElementParam param) {
        Page<MetaDataElementDTO> page = this.metaDataElementService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增数据元", notes = "新增数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_ADD)")
    public ResponseEntity<ResponseVO> addMetaDataElement(@Validated @RequestBody MetaDataElementDTO metaDataElementDTO) {
        this.metaDataElementService.insert(metaDataElementDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改数据元", notes = "修改数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_EDIT)")
    public ResponseEntity<ResponseVO> editMetaDataElement(@Validated @RequestBody MetaDataElementDTO metaDataElementDTO) {
        this.metaDataElementService.update(metaDataElementDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除数据元", notes = "删除数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataElement(@PathVariable(value = "id") String id) {
        this.metaDataElementService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除数据元", notes = "批量删除数据元")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).STDATA_DATA_ELEMENT_DELETE)")
    public ResponseEntity<ResponseVO> deleteMetaDataElement(@RequestBody Map<Integer, String> map) {
        this.metaDataElementService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
