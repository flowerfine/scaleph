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

package cn.sliew.scaleph.dao.mapper.master.workflow;

import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstance;
import cn.sliew.scaleph.dao.entity.master.workflow.WorkflowTaskInstanceVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * workflow task instance Mapper 接口
 * </p>
 */
@Repository
public interface WorkflowTaskInstanceMapper extends BaseMapper<WorkflowTaskInstance> {

    Page<WorkflowTaskInstanceVO> list(Page<WorkflowTaskInstance> page, @Param("workflowTaskDefinitionId") Long workflowTaskDefinitionId);

    WorkflowTaskInstanceVO get(@Param("id") Long id);

}
