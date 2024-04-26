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

package cn.sliew.scaleph.application.oam.service.dto;

import cn.sliew.scaleph.system.model.BaseDTO;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Policy Definition 信息
 */
@Data
public class OamPolicyDefinitionDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "定义id。主要用于 kubernetes 中 metadata 使用")
    private String definitionId;

    private String name;

    private String definitionRef;

    private String schematic;

    @Schema(description = "备注")
    private String remark;
}
