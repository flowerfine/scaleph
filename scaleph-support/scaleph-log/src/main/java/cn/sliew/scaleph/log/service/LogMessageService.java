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

import cn.sliew.scaleph.log.service.dto.LogMessageDTO;
import cn.sliew.scaleph.log.service.param.LogMessageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 站内信表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface LogMessageService {
    /**
     * 新增
     *
     * @param logMessageDTO 消息对象
     * @return int
     */
    int insert(LogMessageDTO logMessageDTO);

    /**
     * 修改
     *
     * @param logMessageDTO dict
     * @return int
     */
    int update(LogMessageDTO logMessageDTO);

    /**
     * 查询未读消息数量
     *
     * @param receiver 收件人
     * @return 消息数量
     */
    Long countUnReadMsg(String receiver);

    /**
     * 分页查询
     *
     * @param logMessageParam 查询参数
     * @return page list
     */
    Page<LogMessageDTO> listByPage(LogMessageParam logMessageParam);

    /**
     * 全部读取
     *
     * @param receiver 收件人
     * @return int
     */
    int readAll(String receiver);
}
