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
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.ConnectorType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
@RequestMapping("/api/datadev/job/step")
public class DiJobStep2Controller {

    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Logging
    @GetMapping("env")
    @ApiOperation(value = "查询 SeaTunnel env 支持的属性", notes = "获取 env 分类属性")
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return seatunnelConnectorService.getSupportedEnvProperties();
    }

    @Logging
    @GetMapping("{type}")
    @ApiOperation(value = "查询 SeaTunnel connector 列表", notes = "查询 source, transform, sink 类型的 connector")
    public Set<PluginInfo> getAvailableConnectors(@ApiParam(name = "type",
            value = "connector 类型。0: source, 1: transform, 2: sink",
            example = "0", allowableValues = "0, 1, 2") @PathVariable("type") ConnectorType type) {
        return seatunnelConnectorService.getAvailableConnectors(type);
    }

    @Logging
    @GetMapping("properties")
    @ApiOperation(value = "查询 SeaTunnel connector 支持的属性")
    public List<PropertyDescriptor> getSupportedProperties(@Valid PluginInfo pluginInfo) {
        return seatunnelConnectorService.getSupportedProperties(pluginInfo);
    }

}

