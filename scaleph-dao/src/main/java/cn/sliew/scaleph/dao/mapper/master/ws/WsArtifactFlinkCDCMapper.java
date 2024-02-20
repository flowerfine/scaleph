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

import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkCDC;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * artifact flink-cdc Mapper 接口
 */
@Repository
public interface WsArtifactFlinkCDCMapper extends BaseMapper<WsArtifactFlinkCDC> {

    Page<WsArtifactFlinkCDC> list(Page<WsArtifactFlinkCDC> page,
                                  @Param("projectId") Long projectId,
                                  @Param("name") String name,
                                  @Param("flinkVersion") FlinkVersion flinkVersion);

    List<WsArtifactFlinkCDC> listAll(@Param("projectId") Long projectId,
                                     @Param("name") String name);

    WsArtifactFlinkCDC selectOne(@Param("id") Long id);

    WsArtifactFlinkCDC selectCurrent(@Param("artifactId") Long artifactId);
}
