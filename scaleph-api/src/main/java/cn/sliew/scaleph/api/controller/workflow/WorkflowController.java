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

package cn.sliew.scaleph.api.controller.workflow;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.system.vo.ResponseVO;
import cn.sliew.scaleph.workflow.service.WorkflowDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowInstanceService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskDefinitionService;
import cn.sliew.scaleph.workflow.service.WorkflowTaskInstanceService;
import cn.sliew.scaleph.workflow.service.dto.WorkflowDefinitionDTO;
import cn.sliew.scaleph.workflow.service.dto.WorkflowTaskDefinitionDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowDefinitionListParam;
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

import javax.validation.Valid;
import java.util.List;

@Api(tags = "workflow管理-workflow")
@RestController
@RequestMapping(path = {"/api/workflow"})
public class WorkflowController {

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;
    @Autowired
    private WorkflowTaskDefinitionService workflowTaskDefinitionService;
    @Autowired
    private WorkflowInstanceService workflowInstanceService;
    @Autowired
    private WorkflowTaskInstanceService workflowTaskInstanceService;

    @Logging
    @GetMapping
    @ApiOperation(value = "查询 workflow 定义列表", notes = "查询 workflow 定义列表")
    public ResponseEntity<Page<WorkflowDefinitionDTO>> listWorkflowDefinitions(@Valid WorkflowDefinitionListParam param) {
        final Page<WorkflowDefinitionDTO> result = workflowDefinitionService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @GetMapping("{workflowDefinitionId}")
    @ApiOperation(value = "查询 workflow 定义列表", notes = "查询 workflow 定义列表")
    public ResponseEntity<ResponseVO<List<WorkflowTaskDefinitionDTO>>> listWorkflowTaskDefinitions(@PathVariable("workflowDefinitionId") Long workflowDefinitionId) {
        List<WorkflowTaskDefinitionDTO> result = workflowTaskDefinitionService.list(workflowDefinitionId);
        return new ResponseEntity<>(ResponseVO.sucess(result), HttpStatus.OK);
    }

}
