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

package cn.sliew.scaleph.application.doris.service.param;

import cn.sliew.scaleph.application.doris.operator.spec.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WsDorisOperatorTemplateAddParam {

    @NotNull
    @Schema(description = "project id")
    private Long projectId;

    @NotBlank
    @Schema(description = "name")
    private String name;

    @Schema(description = "admin user")
    private AdminUser admin;

    @NotNull
    @Schema(description = "fe spec")
    private FeSpec feSpec;

    @Schema(description = "be spec")
    private BeSpec beSpec;

    @Schema(description = "cn spec")
    private CnSpec cnSpec;

    @Schema(description = "broker spec")
    private BrokerSpec brokerSpec;

    @Schema(description = "remark")
    private String remark;
}
