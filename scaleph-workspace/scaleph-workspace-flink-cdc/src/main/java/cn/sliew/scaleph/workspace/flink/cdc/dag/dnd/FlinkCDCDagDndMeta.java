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

package cn.sliew.scaleph.workspace.flink.cdc.dag.dnd;

import cn.sliew.scaleph.dag.xflow.dnd.DndMeta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Flink CDC 节点元数据", description = "Flink CDC 节点元数据")
public class FlinkCDCDagDndMeta extends DndMeta {

    /**
     * @see cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginName
     */
    @Schema(description = "connector name")
    private String name;

    /**
     * @see cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginType
     */
    @Schema(description = "connector type")
    private String type;
}
