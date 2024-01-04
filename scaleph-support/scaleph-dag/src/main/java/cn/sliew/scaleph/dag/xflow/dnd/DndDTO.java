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

package cn.sliew.scaleph.dag.xflow.dnd;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "折叠面板节点", description = "折叠面板节点")
public class DndDTO {

    @Schema(description = "分组")
    private String category;

    @Schema(description = "唯一标识")
    private String key;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String docString;

    @Schema(description = "是否是叶子节点")
    private Boolean isLeaf;

    @Schema(description = "元信息")
    private DndMeta meta;

    @Schema(description = "连接桩 Port")
    private List<DndPortDTO> ports;

    @Schema(description = "子节点")
    private List<DndDTO> children;
}
