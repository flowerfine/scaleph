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

package cn.sliew.scaleph.workflow.service;

import cn.sliew.scaleph.workflow.service.dto.WorkflowScheduleDTO;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleAddParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleListParam;
import cn.sliew.scaleph.workflow.service.param.WorkflowScheduleUpdateParam;

import java.util.List;

public interface WorkflowScheduleService {

    List<WorkflowScheduleDTO> list(WorkflowScheduleListParam param);

    WorkflowScheduleDTO get(Long id);

    void insert(WorkflowScheduleAddParam param);

    void update(Long id, WorkflowScheduleUpdateParam param);

    void delete(Long id);

    void deleteBatch(List<Long> ids);

    void enable(Long id);

    void disable(Long id);

}
