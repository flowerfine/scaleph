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

package cn.sliew.scaleph.engine.doris.service;

import cn.sliew.scaleph.engine.doris.service.dto.WsDorisTemplateDTO;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateAddParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateListParam;
import cn.sliew.scaleph.engine.doris.service.param.WsDorisTemplateUpdateParam;
import cn.sliew.scaleph.engine.doris.service.resource.DorisTemplate;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface WsDorisTemplateService {

    Page<WsDorisTemplateDTO> list(WsDorisTemplateListParam param);

    WsDorisTemplateDTO selectOne(Long id);

    DorisTemplate asYaml(Long id);

    int insert(WsDorisTemplateAddParam param);

    int update(WsDorisTemplateUpdateParam param);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);
}
