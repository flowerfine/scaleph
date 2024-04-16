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

import cn.sliew.scaleph.application.flink.service.dto.FlinkImageOption;
import cn.sliew.scaleph.application.flink.service.dto.FlinkVersionOption;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.application.flink.resource.definition.template.FlinkTemplate;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateAddParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface WsFlinkKubernetesTemplateService {

    Page<WsFlinkKubernetesTemplateDTO> list(WsFlinkKubernetesTemplateListParam param);

    WsFlinkKubernetesTemplateDTO selectOne(Long id);

    List<FlinkVersionOption> getFlinkVersionOptions();

    List<FlinkImageOption> getFlinkImageOptions(FlinkVersion flinkVersion);

    FlinkTemplate asYaml(WsFlinkKubernetesTemplateDTO dto);

    FlinkTemplate asYamlWithDefault(WsFlinkKubernetesTemplateDTO dto);

    WsFlinkKubernetesTemplateDTO mergeDefault(WsFlinkKubernetesTemplateDTO dto);

    int insert(WsFlinkKubernetesTemplateAddParam param);

    int update(WsFlinkKubernetesTemplateUpdateParam param);

    int updateTemplate(WsFlinkKubernetesTemplateDTO param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);
}
