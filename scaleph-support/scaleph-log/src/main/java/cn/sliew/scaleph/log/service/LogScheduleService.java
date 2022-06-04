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

package cn.sliew.scaleph.log.service;

import cn.sliew.scaleph.log.service.dto.LogScheduleDTO;
import cn.sliew.scaleph.log.service.param.LogScheduleParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 定时任务运行日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-16
 */
public interface LogScheduleService {
    /**
     * 新增
     *
     * @param logSchedule 日志对象
     * @return int
     */
    int insert(LogScheduleDTO logSchedule);

    /**
     * 查询日志
     *
     * @param id id
     * @return 日志对象
     */
    LogScheduleDTO selectOne(Long id);

    /**
     * 分页查询
     *
     * @param logScheduleParam 参数
     * @return page
     */
    Page<LogScheduleDTO> listByPage(LogScheduleParam logScheduleParam);
}
