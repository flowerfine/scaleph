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

package cn.sliew.scaleph.application.doris.service.dto;

import cn.sliew.scaleph.application.doris.operator.spec.*;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "WsDorisOperatorTemplate对象", description = "doris operator template")
public class WsDorisOperatorTemplateDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "项目id")
    private Long projectId;

    private String name;

    private String templateId;

    @Schema(description = "admin user")
    private AdminUser admin;

    @Schema(description = "fe spec")
    private FeSpec feSpec;

    @Schema(description = "be spec")
    private BeSpec beSpec;

    @Schema(description = "cn spec")
    private CnSpec cnSpec;

    @Schema(description = "broker spec")
    private BrokerSpec brokerSpec;

    private String remark;
}
