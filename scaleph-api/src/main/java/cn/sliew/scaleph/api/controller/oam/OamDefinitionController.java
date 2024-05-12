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

package cn.sliew.scaleph.api.controller.oam;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.application.oam.model.definition.ComponentDefinition;
import cn.sliew.scaleph.application.oam.model.definition.PolicyDefinition;
import cn.sliew.scaleph.application.oam.model.definition.TraitDefinition;
import cn.sliew.scaleph.application.oam.service.OamComponentDefinitionService;
import cn.sliew.scaleph.application.oam.service.OamPolicyDefinitionService;
import cn.sliew.scaleph.application.oam.service.OamTraitDefinitionService;
import cn.sliew.scaleph.application.oam.service.OamWorkloadDefinitionService;
import cn.sliew.scaleph.system.model.PaginationParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAM管理-Definition管理")
@RestController
@RequestMapping(path = "/api/oam/definition")
public class OamDefinitionController {

    @Autowired
    private OamComponentDefinitionService oamComponentDefinitionService;
    @Autowired
    private OamPolicyDefinitionService oamPolicyDefinitionService;
    @Autowired
    private OamTraitDefinitionService oamTraitDefinitionService;
    @Autowired
    private OamWorkloadDefinitionService oamWorkloadDefinitionService;

    @Logging
    @GetMapping("components")
    @Operation(summary = "查询 ComponentDefinition 列表", description = "查询 ComponentDefinition 列表")
    public ResponseEntity<Page<ComponentDefinition>> listComponents(@Valid PaginationParam param) {
        Page<ComponentDefinition> result = oamComponentDefinitionService.listByPage(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("policies")
    @Operation(summary = "查询 PolicyDefinition 列表", description = "查询 PolicyDefinition 列表")
    public ResponseEntity<Page<PolicyDefinition>> listPolicies(@Valid PaginationParam param) {
        Page<PolicyDefinition> result = oamPolicyDefinitionService.listByPage(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Logging
    @GetMapping("traits")
    @Operation(summary = "查询 TraitDefinition 列表", description = "查询 TraitDefinition 列表")
    public ResponseEntity<Page<TraitDefinition>> listTraits(@Valid PaginationParam param) {
        Page<TraitDefinition> result = oamTraitDefinitionService.listByPage(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
