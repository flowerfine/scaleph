package cn.sliew.scalegh.api.controller.datadev;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.api.annotation.Logging;
import cn.sliew.scalegh.api.vo.ResponseVO;
import cn.sliew.scalegh.common.enums.ResponseCodeEnum;
import cn.sliew.scalegh.meta.util.JdbcUtil;
import cn.sliew.scalegh.service.dto.meta.DataSourceMetaDTO;
import cn.sliew.scalegh.service.meta.DataSourceMetaService;
import cn.sliew.scalegh.service.param.meta.DataSourceMetaParam;
import cn.sliew.scalegh.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@Slf4j
@Api(tags = "数据开发-数据源")
@RestController
@RequestMapping(path = "/api/datadev/datasource")
public class DataSourceController {
    @Autowired
    private DataSourceMetaService dataSourceMetaService;

    @Logging
    @GetMapping
    @ApiOperation(value = "分页查询数据源", notes = "分页查询数据源信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<Page<DataSourceMetaDTO>> listDataSource(DataSourceMetaParam param) {
        Page<DataSourceMetaDTO> page = this.dataSourceMetaService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "type/{type}")
    @ApiOperation(value = "按类型查询数据源列表", notes = "按类型查询数据源信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<List<DictVO>> listDataSourceByType(@PathVariable(value = "type") String type) {
        List<DictVO> dsList = new ArrayList<>();
        if (StrUtil.isNotEmpty(type)) {
            List<DataSourceMetaDTO> list = this.dataSourceMetaService.listByType(type);
            for (DataSourceMetaDTO dto : list) {
                DictVO d = new DictVO(String.valueOf(dto.getId()), dto.getDataSourceName());
                dsList.add(d);
            }
        }
        return new ResponseEntity<>(dsList, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增数据源", notes = "新增数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_ADD)")
    public ResponseEntity<ResponseVO> addDataSource(@Validated @RequestBody DataSourceMetaDTO dataSourceMetaDTO) {
        this.dataSourceMetaService.insert(dataSourceMetaDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改数据源", notes = "修改数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_EDIT)")
    public ResponseEntity<ResponseVO> editDataSource(@Validated @RequestBody DataSourceMetaDTO dataSourceMetaDTO) {
        if (dataSourceMetaDTO.getPasswdChanged() == null || !dataSourceMetaDTO.getPasswdChanged()) {
            dataSourceMetaDTO.setPassword(null);
        }
        this.dataSourceMetaService.update(dataSourceMetaDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除数据源", notes = "删除数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDataSource(@PathVariable(value = "id") String id) {
        this.dataSourceMetaService.deleteById(Long.valueOf(id));
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除数据源", notes = "批量删除数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDataSource(@RequestBody Map<Integer, String> map) {
        this.dataSourceMetaService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/passwd/{id}")
    @ApiOperation(value = "查看数据源密码", notes = "查看数据源明文密码")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SECURITY)")
    public ResponseEntity<ResponseVO> showPassword(@PathVariable(value = "id") Long id) throws Exception {
        DataSourceMetaDTO dataSourceMetaDTO = this.dataSourceMetaService.selectOne(id);
        if (dataSourceMetaDTO == null || StringUtils.isEmpty(dataSourceMetaDTO.getPassword())) {
            return new ResponseEntity<>(ResponseVO.sucess(""), HttpStatus.OK);
        } else {
            String passwd = Base64.decodeStr(dataSourceMetaDTO.getPassword());
            return new ResponseEntity<>(ResponseVO.sucess(passwd), HttpStatus.OK);
        }
    }

    @Logging
    @PostMapping("/test")
    @ApiOperation(value = "测试数据源连通性", notes = "测试数据源连通性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scalegh.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<ResponseVO> connectionTest(@Validated @RequestBody DataSourceMetaDTO dataSourceMetaDTO) throws Exception {
        //判断前台是否改过密码，改过则用前台最新的密码
        if (dataSourceMetaDTO.getPasswdChanged() == null || !dataSourceMetaDTO.getPasswdChanged()) {
            DataSourceMetaDTO oldDsInfo = this.dataSourceMetaService.selectOne(dataSourceMetaDTO.getId());
            dataSourceMetaDTO.setPassword(Base64.decodeStr(oldDsInfo.getPassword()));
        }
        try {
            boolean result = JdbcUtil.testConnection(dataSourceMetaDTO);
            return result ? new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK) :
                    new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CONNECTION), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseVO.error(e.getMessage()), HttpStatus.OK);
        }
    }
}
