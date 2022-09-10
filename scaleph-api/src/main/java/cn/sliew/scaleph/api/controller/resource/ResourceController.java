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

package cn.sliew.scaleph.api.controller.resource;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.resource.service.ResourceService;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "资源管理-资源服务")
@RestController
@RequestMapping(path = "/api/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询支持的资源类型", notes = "查询支持的资源类型")
    public ResponseEntity<List<ResourceType>> getTypes() {
        final List<ResourceType> resourceTypes = resourceService.getSupportedResources();
        return new ResponseEntity<>(resourceTypes, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{resourceType}")
    @ApiOperation(value = "查询资源列表", notes = "查询资源列表")
    public ResponseEntity<Page> list(@PathVariable("resourceType") ResourceType resourceType,
                                     ResourceListParam param) {
        final Page resourceVOS = resourceService.list(resourceType, param);
        return new ResponseEntity<>(resourceVOS, HttpStatus.OK);
    }

    @Logging
    @GetMapping("/{resourceType}/{id}")
    @ApiOperation(value = "查询资源详情", notes = "查询资源详情")
    public ResponseEntity<Object> list(@PathVariable("resourceType") ResourceType resourceType,
                                       @PathVariable("id") Long id) {
        final Object resource = resourceService.getRaw(resourceType, id);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
