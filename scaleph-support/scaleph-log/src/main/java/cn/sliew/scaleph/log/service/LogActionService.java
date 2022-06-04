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

import cn.sliew.scaleph.log.service.dto.LogActionDTO;
import cn.sliew.scaleph.log.service.param.LogActionParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户操作日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface LogActionService {
    /**
     * 新增日志
     *
     * @param logActionDTO 登录日志
     * @return int
     */
    int insert(LogActionDTO logActionDTO);

    /**
     * 分页查询
     *
     * @param logActionParam 查询参数
     * @return page list
     */
    Page<LogActionDTO> listByPage(LogActionParam logActionParam);
}
