package cn.sliew.scaleph.api.controller.datadev;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.api.util.I18nUtil;
import cn.sliew.scaleph.api.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.ClusterTypeEnum;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.service.di.DiClusterConfigService;
import cn.sliew.scaleph.service.di.DiJobService;
import cn.sliew.scaleph.service.dto.di.DiClusterConfigDTO;
import cn.sliew.scaleph.service.param.di.DiClusterConfigParam;
import cn.sliew.scaleph.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */

@Slf4j
@Api(tags = "数据开发-集群管理")
@RestController
@RequestMapping(path = "/api/datadev/cluster")
public class ClusterController {

    @Autowired
    private DiClusterConfigService diClusterConfigService;

    @Autowired
    private DiJobService diJobService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询集群列表", notes = "分页查询集群列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_SELECT)")
    public ResponseEntity<Page<DiClusterConfigDTO>> listCluster(DiClusterConfigParam param) {
        Page<DiClusterConfigDTO> page = this.diClusterConfigService.listByPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/all")
    @ApiOperation(value = "查询全部集群", notes = "查询所有集群列表")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_SELECT)")
    public ResponseEntity<List<DictVO>> listAll() {
        List<DictVO> list = this.diClusterConfigService.listAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @ApiOperation(value = "新增集群", notes = "新增集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_ADD)")
    public ResponseEntity<ResponseVO> addCluster(@Validated @RequestBody DiClusterConfigDTO diClusterConfigDTO) {
        if (!checkClusterInfo(diClusterConfigDTO)) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.cluster.conf"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diClusterConfigService.insert(diClusterConfigDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    private boolean checkClusterInfo(DiClusterConfigDTO diClusterConfigDTO) {
        if (diClusterConfigDTO != null && StrUtil.isNotEmpty(diClusterConfigDTO.getClusterConf())) {
            Map<String, String> confMap = new HashMap<>();
            String[] lines = diClusterConfigDTO.getClusterConf().split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StrUtil.isAllNotBlank(kv)) {
                    confMap.put(kv[0], kv[1]);
                }
            }
            if (ClusterTypeEnum.FLINK.getValue().equalsIgnoreCase(diClusterConfigDTO.getClusterType().getValue())) {
                if (!confMap.containsKey(Constants.CLUSTER_DEPLOY_TARGET) ||
                        ResourceProvider.STANDALONE.getName()
                                .equalsIgnoreCase(confMap.get(Constants.CLUSTER_DEPLOY_TARGET))) {
                    return confMap.containsKey(JobManagerOptions.ADDRESS.key())
                            && confMap.containsKey(JobManagerOptions.PORT.key())
                            && confMap.containsKey(RestOptions.PORT.key());
                }
            }
        }
        return false;
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改集群", notes = "修改集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_EDIT)")
    public ResponseEntity<ResponseVO> editCluster(@Validated @RequestBody DiClusterConfigDTO diClusterConfigDTO) {
        this.diClusterConfigService.update(diClusterConfigDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除集群", notes = "删除集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_DELETE)")
    public ResponseEntity<ResponseVO> deleteCluster(@PathVariable(value = "id") Long id) {
        if (this.diJobService.hasRunningJob(new ArrayList<Long>() {{
            add(id);
        }})) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.resource.runningJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diClusterConfigService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @ApiOperation(value = "批量删除集群", notes = "批量删除集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_DELETE)")
    public ResponseEntity<ResponseVO> deleteCluster(@RequestBody Map<Integer, Long> map) {
        if (this.diJobService.hasRunningJob(map.values())) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.resource.runningJob"), ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diClusterConfigService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }
}
