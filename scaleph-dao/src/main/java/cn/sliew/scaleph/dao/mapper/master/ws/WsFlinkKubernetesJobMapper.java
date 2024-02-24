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

package cn.sliew.scaleph.dao.mapper.master.ws;

import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkRuntimeExecutionMode;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * flink kubernetes job Mapper 接口
 */
@Repository
public interface WsFlinkKubernetesJobMapper extends BaseMapper<WsFlinkKubernetesJob> {

    Page<WsFlinkKubernetesJob> list(Page<WsFlinkKubernetesJob> page,
                                    @Param("projectId") Long projectId,
                                    @Param("executionMode") FlinkRuntimeExecutionMode executionMode,
                                    @Param("flinkJobType") FlinkJobType flinkJobType,
                                    @Param("deploymentKind") DeploymentKind deploymentKind,
                                    @Param("flinkJobState") FlinkJobState flinkJobState,
                                    @Param("name") String name);

    WsFlinkKubernetesJob selectOne(@Param("id") Long id);

}
