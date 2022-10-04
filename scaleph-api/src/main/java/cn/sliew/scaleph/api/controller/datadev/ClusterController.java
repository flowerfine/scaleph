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

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.common.enums.ClusterTypeEnum;
import cn.sliew.scaleph.common.enums.ErrorShowTypeEnum;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.enums.ResponseCodeEnum;
import cn.sliew.scaleph.core.di.service.DiClusterConfigService;
import cn.sliew.scaleph.core.di.service.DiJobService;
import cn.sliew.scaleph.core.di.service.dto.DiClusterConfigDTO;
import cn.sliew.scaleph.core.di.service.param.DiClusterConfigParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import cn.sliew.scaleph.system.util.I18nUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@Slf4j
@Api(tags = "数据开发-集群管理")
@RestController
@RequestMapping(path = {"/api/datadev/cluster","/api/di/cluster"})
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
    public ResponseEntity<ResponseVO> addCluster(
            @Validated @RequestBody DiClusterConfigDTO diClusterConfigDTO) {
        if (!checkClusterInfo(diClusterConfigDTO)) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.cluster.conf"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
        this.diClusterConfigService.insert(diClusterConfigDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @ApiOperation(value = "修改集群", notes = "修改集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_EDIT)")
    public ResponseEntity<ResponseVO> editCluster(
            @Validated @RequestBody DiClusterConfigDTO diClusterConfigDTO) {
        if (!checkClusterInfo(diClusterConfigDTO)) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.cluster.conf"), ErrorShowTypeEnum.NOTIFICATION),
                    HttpStatus.OK);
        }
        this.diClusterConfigService.update(diClusterConfigDTO);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "删除集群", notes = "删除集群")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_CLUSTER_DELETE)")
    public ResponseEntity<ResponseVO> deleteCluster(@PathVariable(value = "id") Long id) {
        if (this.diJobService.hasRunningJob(Collections.singletonList(id))) {
            return new ResponseEntity<>(ResponseVO.error(ResponseCodeEnum.ERROR_CUSTOM.getCode(),
                    I18nUtil.get("response.error.di.resource.runningJob"),
                    ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
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
                    I18nUtil.get("response.error.di.resource.runningJob"),
                    ErrorShowTypeEnum.NOTIFICATION), HttpStatus.OK);
        }
        this.diClusterConfigService.deleteBatch(map);
        return new ResponseEntity<>(ResponseVO.sucess(), HttpStatus.OK);
    }

    /**
     * Determine whether the cluster has configured the necessary parameters based on the cluster type and the deployment mode
     *
     * @param diClusterConfigDTO cluster info
     * @return true/false
     */
    private boolean checkClusterInfo(DiClusterConfigDTO diClusterConfigDTO) {
        //todo https://github.com/flowerfine/scaleph/issues/93
        if (diClusterConfigDTO != null && StringUtils.hasText(diClusterConfigDTO.getClusterConf())) {
            Map<String, String> confMap = new HashMap<>();
            String[] lines = diClusterConfigDTO.getClusterConf().split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StringUtils.hasText(kv[0]) && StringUtils.hasText(kv[1])) {
                    confMap.put(kv[0], kv[1]);
                }
            }
            if (ClusterTypeEnum.FLINK.getValue()
                    .equalsIgnoreCase(diClusterConfigDTO.getClusterType().getValue())) {
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
}
