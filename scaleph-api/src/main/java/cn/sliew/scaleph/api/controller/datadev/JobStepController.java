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
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据集成-作业步骤信息2 前端控制器
 * </p>
 */
@Slf4j
@Api(tags = "数据开发-作业管理-step管理")
@RestController
@RequestMapping({"/api/datadev/job/step", "/api/di/job/step"})
public class JobStepController {

    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Logging
    @GetMapping("env")
    @ApiOperation(value = "查询 SeaTunnel env 支持的属性", notes = "获取 env 分类属性")
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return seatunnelConnectorService.getSupportedEnvProperties();
    }

    @Logging
    @GetMapping("{stepType}")
    @ApiOperation(value = "查询 SeaTunnel connector 列表", notes = "查询 source, transform, sink 类型的 connector")
    public Set<PluginInfo> getAvailableConnectors(@ApiParam(name = "stepType",
            value = "job step 类型。source, trans, sink",
            example = "source", allowableValues = "source, trans, sink") @PathVariable("stepType") SeaTunnelPluginType stepType) {
        return seatunnelConnectorService.getAvailableConnectors(stepType);
    }

//    @Logging
//    @GetMapping("{name}/properties")
//    @ApiOperation(value = "查询 SeaTunnel connector 支持的属性")
//    public List<PropertyDescriptor> getSupportedProperties(@PathVariable("name") String name) {
//        return seatunnelConnectorService.getSupportedProperties(name);
//    }

}

