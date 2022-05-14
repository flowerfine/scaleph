package cn.sliew.scalegh.api.controller.admin;


import cn.sliew.scalegh.api.annotation.AnonymousAccess;
import cn.sliew.scalegh.api.annotation.Logging;
import cn.sliew.scalegh.api.vo.ResponseVO;
import cn.sliew.scalegh.service.admin.DictService;
import cn.sliew.scalegh.service.admin.DictTypeService;
import cn.sliew.scalegh.service.cache.DictTypeCache;
import cn.sliew.scalegh.service.dto.admin.DictDTO;
import cn.sliew.scalegh.service.dto.admin.DictTypeDTO;
import cn.sliew.scalegh.service.param.admin.DictParam;
import cn.sliew.scalegh.service.param.admin.DictTypeParam;
import cn.sliew.scalegh.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典表 前端控制器
 * </p>
 *
 * @author liyu
 */
@RestController
@RequestMapping("/api/admin/dict")
@Api(tags = "系统管理-数据字典管理")
public class DictController {

    @Autowired
    private DictTypeService dictTypeService;
    @Autowired
    private DictService dictService;

    /**
     * 分页查询数据字典
     *
     * @param dictParam 查询参数
     * @return ResponseEntity
     */
    @Logging
    @GetMapping(path = "/data")
    @ApiOperation(value = "查询数据字典", notes = "分页查询数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_DATA_SELECT)")
    public ResponseEntity<Page<DictDTO>> listDict(DictParam dictParam) {
        Page<DictDTO> pageDTO = this.dictService.listByPage(dictParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    @AnonymousAccess
    @GetMapping(path = "/data/{dictTypeCode}")
    @ApiOperation(value = "查询数据字典", notes = "根据字典类型code查询数据字典")
    public ResponseEntity<List<DictVO>> listDictByType(@NotNull @PathVariable(value = "dictTypeCode") String dictTypeCode) {
        List<DictDTO> list = this.dictService.selectByType(dictTypeCode);
        List<DictVO> result = new ArrayList<>(list.size());
        list.forEach(d -> {
            result.add(new DictVO(d.getDictCode(), d.getDictValue()));
        });
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 新增数据字典
     *
     * @param dictDTO 数据字典
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/data")
    @ApiOperation(value = "新增数据字典", notes = "新增数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_DATA_ADD)")
    public ResponseEntity<ResponseVO> addDict(@Validated @RequestBody DictDTO dictDTO) {
        this.dictService.insert(dictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    /**
     * 编辑数据字典
     *
     * @param dictDTO 数据字典
     * @return ResponseEntity
     */
    @Logging
    @PutMapping(path = "/data")
    @ApiOperation(value = "修改数据字典", notes = "修改数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_DATA_EDIT)")
    public ResponseEntity<ResponseVO> editDict(@Validated @RequestBody DictDTO dictDTO) {
        this.dictService.update(dictDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseEntity
     */
    @Logging
    @DeleteMapping(path = "/data/{id}")
    @ApiOperation(value = "删除数据字典", notes = "根据id删除数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteDict(@PathVariable(value = "id") String id) {
        this.dictService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 删除数据字典
     *
     * @param map 待删除的id列表
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/data/batch")
    @ApiOperation(value = "批量删除数据字典", notes = "根据id列表批量数据字典")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_DATA_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDict(@RequestBody Map<Integer, String> map) {
        this.dictService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }


    /**
     * 分页查询数据字典类型
     *
     * @param dictTypeParam 查询参数
     * @return ResponseEntity
     */
    @Logging
    @GetMapping(path = "/type")
    @ApiOperation(value = "查询数据字典类型", notes = "分页查询数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_TYPE_SELECT)")
    public ResponseEntity<Page<DictTypeDTO>> listDictType(DictTypeParam dictTypeParam) {
        Page<DictTypeDTO> pageDTO = this.dictTypeService.listByPage(dictTypeParam);
        return new ResponseEntity<>(pageDTO, HttpStatus.OK);
    }

    /**
     * 查询全部数据字典类型
     *
     * @return ResponseEntity
     */
    @AnonymousAccess
    @GetMapping(path = "/type/all")
    @ApiOperation(value = "查询数据字典类型", notes = "从缓存中查询所有数据字典类型")
    public ResponseEntity<List<DictVO>> listDictTypeAll() {
        List<DictTypeDTO> list = DictTypeCache.listAll();
        List<DictVO> result = new ArrayList<>();
        for (DictTypeDTO dto : list) {
            result.add(new DictVO(dto.getDictTypeCode(), dto.getDictTypeName()));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 新增数据字典类型
     *
     * @param dictTypeDTO 数据字典类型
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/type")
    @ApiOperation(value = "新增数据字典类型", notes = "新增数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_TYPE_ADD)")
    public ResponseEntity<ResponseVO> addDictType(@Validated @RequestBody DictTypeDTO dictTypeDTO) {
        this.dictTypeService.insert(dictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    /**
     * 编辑数据字典类型
     *
     * @param dictTypeDTO 数据字典类型
     * @return ResponseEntity
     */
    @Logging
    @PutMapping(path = "/type")
    @ApiOperation(value = "修改数据字典类型", notes = "修改数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_TYPE_EDIT)")
    public ResponseEntity<ResponseVO> editDictType(@Validated @RequestBody DictTypeDTO dictTypeDTO) {
        this.dictTypeService.update(dictTypeDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseEntity
     */
    @Logging
    @DeleteMapping(path = "/type/{id}")
    @ApiOperation(value = "删除数据字典类型", notes = "根据id删除数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDictType(@PathVariable(value = "id") String id) {
        this.dictTypeService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * 删除数据字典类型
     *
     * @param map 待删除的id列表
     * @return ResponseEntity
     */
    @Logging
    @PostMapping(path = "/type/batch")
    @ApiOperation(value = "批量删除数据字典类型", notes = "根据id列表批量数据字典类型")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DICT_TYPE_DELETE)")
    public ResponseEntity<ResponseVO> deleteBatchDictType(@RequestBody Map<Integer, String> map) {
        this.dictTypeService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}

