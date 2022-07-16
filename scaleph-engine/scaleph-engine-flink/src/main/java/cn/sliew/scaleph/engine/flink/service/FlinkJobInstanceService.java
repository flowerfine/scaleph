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

package cn.sliew.scaleph.engine.flink.service;

import cn.sliew.scaleph.engine.flink.service.dto.FlinkJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkJobInstanceListParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface FlinkJobInstanceService {

    Page<FlinkJobInstanceDTO> list(FlinkJobInstanceListParam param);

    FlinkJobInstanceDTO selectOne(Long id);

    // todo 根据 flinkJobConfigId 删除


    boolean submit(Long flinkJobConfigId) throws Exception;

    /**
     * 暂停时会创建 savepoint，恢复时可以根据任务的上一次 savepoint 进行重启
     */
    boolean suspend(Long id) throws Exception;

    /**
     * 重新提交任务，并使用之前任务实例的 savepoint
     */
    boolean resume(Long flinkJobConfigId, Long id) throws Exception;

    boolean triggerSavepoint(Long id) throws Exception;

    boolean cancel(Long id) throws Exception;

}
