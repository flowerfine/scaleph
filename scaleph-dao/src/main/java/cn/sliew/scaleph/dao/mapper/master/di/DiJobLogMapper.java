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

package cn.sliew.scaleph.dao.mapper.master.di;

import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.dao.entity.master.di.DiJobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据集成-作业运行日志 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-05-06
 */
@Repository
public interface DiJobLogMapper extends BaseMapper<DiJobLog> {

    Page<DiJobLog> selectPage(IPage<?> page,
                              @Param(value = "log") DiJobLog log,
                              @Param(value = "jobType") String jobType
    );

    List<DiJobLog> selectTopN(@Param(value = "jobType") String jobType,
                              @Param(value = "n") Integer n,
                              @Param(value = "startTime") Date startTime);

    List<Map<String, Object>> selectRealtimeJobRuntimeStatus(
        @Param(value = "jobType") String jobType);
}
