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

package cn.sliew.scaleph.application.flink.service;

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobAddParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesJobUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface WsFlinkKubernetesJobService {

    Page<WsFlinkKubernetesJobDTO> list(WsFlinkKubernetesJobListParam param);

    List<Long> listAll();

    WsFlinkKubernetesJobDTO selectOne(Long id);

    int insert(WsFlinkKubernetesJobAddParam param);

    int update(WsFlinkKubernetesJobUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

}
