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

package cn.sliew.scaleph.core.di.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.dto.DiJobLogDTO;
import cn.sliew.scaleph.core.di.service.param.DiJobLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集成-作业运行日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-05-06
 */
public interface DiJobLogService {

    int insert(DiJobLogDTO dto);

    int update(DiJobLogDTO dto);

    Page<DiJobLogDTO> listByPage(DiJobLogParam param);

    DiJobLogDTO selectByJobInstanceId(String jobInstanceId);

    List<DiJobLogDTO> listRunningJobInstance(String jobCode);

    List<DiJobLogDTO> listTop100BatchJob(Date startTime);

    Map<String, String> groupRealtimeJobRuntimeStatus();
}
