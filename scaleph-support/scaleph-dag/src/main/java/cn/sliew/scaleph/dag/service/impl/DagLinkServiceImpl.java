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

package cn.sliew.scaleph.dag.service.impl;

import cn.sliew.scaleph.common.dict.workflow.WorkflowTaskInstanceStage;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dag.service.DagLinkService;
import cn.sliew.scaleph.dag.service.convert.DagLinkConvert;
import cn.sliew.scaleph.dag.service.convert.DagLinkVOConvert;
import cn.sliew.scaleph.dag.service.dto.DagLinkDTO;
import cn.sliew.scaleph.dao.entity.master.dag.DagLink;
import cn.sliew.scaleph.dao.entity.master.dag.DagLinkVO;
import cn.sliew.scaleph.dao.mapper.master.dag.DagLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DagLinkServiceImpl implements DagLinkService {

    @Autowired
    private DagLinkMapper dagLinkMapper;

    @Override
    public List<DagLinkDTO> listLinks(Long dagInstanceId) {
        List<DagLinkVO> dagLinkVOS = dagLinkMapper.listByDagInstanceId(dagInstanceId);
        return DagLinkVOConvert.INSTANCE.toDto(dagLinkVOS);
    }

    @Override
    public int insert(DagLinkDTO linkDTO) {
        DagLink record = DagLinkConvert.INSTANCE.toDo(linkDTO);
        record.setInstanceId(UUIDUtil.randomUUId());
        record.setStatus(WorkflowTaskInstanceStage.PENDING.getValue());
        return dagLinkMapper.insert(record);
    }

    @Override
    public int update(DagLinkDTO linkDTO) {
        DagLink record = DagLinkConvert.INSTANCE.toDo(linkDTO);
        return dagLinkMapper.updateById(record);
    }
}
