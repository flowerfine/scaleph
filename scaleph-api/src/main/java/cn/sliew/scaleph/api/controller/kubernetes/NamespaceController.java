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

package cn.sliew.scaleph.api.controller.kubernetes;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.kubernetes.service.NamespaceService;
import cn.sliew.scaleph.system.model.ResponseVO;
import io.fabric8.kubernetes.api.model.Namespace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@Tag(name = "Kubernetes管理-namespace")
@RestController
@RequestMapping(path = {"/api/kubernetes/namespace/{clusterCredentialId}"})
public class NamespaceController {

    @Autowired
    private NamespaceService namespaceService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 namespace 列表", description = "查询 namespace 列表")
    public ResponseEntity<ResponseVO<List<Namespace>>> get(@PathVariable("clusterCredentialId") Long clusterCredentialId) throws ParseException {
        List<Namespace> result = namespaceService.getNamespaces(clusterCredentialId);
        return new ResponseEntity<>(ResponseVO.success(result), HttpStatus.OK);
    }

}
