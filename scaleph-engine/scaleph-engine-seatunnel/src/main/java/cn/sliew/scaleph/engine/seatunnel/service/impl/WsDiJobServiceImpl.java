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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.dto.DagDTO;
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dag.service.param.DagSimpleAddParam;
import cn.sliew.scaleph.dag.service.param.DagSimpleUpdateParam;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsDiJob;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDiJobMapper;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobAttrService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobGraphService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobAttrVOConvert;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobConvert;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobLinkConvert2;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobStepConvert2;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobLinkDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobStepDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.*;
import cn.sliew.scaleph.engine.seatunnel.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsDiJobServiceImpl implements WsDiJobService {

    @Autowired
    private WsFlinkArtifactService wsFlinkArtifactService;
    @Autowired
    private WsDiJobMapper diJobMapper;
    @Autowired
    private DagService dagService;

    @Override
    public Page<WsDiJobDTO> listByPage(WsDiJobListParam param) {
        Page<WsDiJob> jobs = diJobMapper.selectPage(new Page<>(param.getCurrent(), param.getPageSize()), param.getProjectId(), param.getJobEngine(), param.getName());
        Page<WsDiJobDTO> result = new Page<>(jobs.getCurrent(), jobs.getSize(), jobs.getTotal());
        List<WsDiJobDTO> dtoList = WsDiJobConvert.INSTANCE.toDto(jobs.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<WsDiJobDTO> listAll(WsDiJobSelectListParam param) {
        List<WsDiJob> jobs = diJobMapper.listAll(param.getProjectId(), param.getName());
        return WsDiJobConvert.INSTANCE.toDto(jobs);
    }

    @Override
    public WsDiJobDTO selectOne(Long id) {
        WsDiJob record = diJobMapper.selectOne(id);
        checkState(record != null, () -> "di job not exists for id: " + id);
        return WsDiJobConvert.INSTANCE.toDto(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public WsDiJobDTO insert(WsDiJobAddParam param) {
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setProjectId(param.getProjectId());
        flinkArtifact.setType(FlinkJobType.JAR);
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        flinkArtifact = wsFlinkArtifactService.insert(flinkArtifact);

        Long dagId = dagService.insert(new DagSimpleAddParam());
        WsDiJob record = new WsDiJob();
        record.setFlinkArtifactId(flinkArtifact.getId());
        record.setJobId(UUID.randomUUID().toString());
        record.setDagId(dagId);
        record.setJobEngine(param.getJobEngine());
        record.setCurrent(YesOrNo.YES);
        diJobMapper.insert(record);
        return selectOne(record.getId());
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int update(WsDiJobUpdateParam param) {
        WsDiJobDTO jobDTO = selectOne(param.getId());
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setId(jobDTO.getWsFlinkArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        wsFlinkArtifactService.update(flinkArtifact);

        WsDiJob record = new WsDiJob();
        record.setId(param.getId());
        record.setCurrent(YesOrNo.YES);
        return diJobMapper.updateById(record);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int delete(Long id) {
        WsDiJobDTO wsDiJobDTO = selectOne(id);
        dagService.delete(wsDiJobDTO.getDagId());
        if (wsDiJobDTO.getCurrent() == YesOrNo.YES) {
            wsFlinkArtifactService.deleteById(wsDiJobDTO.getWsFlinkArtifact().getId());
        }
        return diJobMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public int deleteBatch(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public WsDiJobDTO queryJobGraph(Long id) {
        WsDiJobDTO job = selectOne(id);
        DagDTO dagDTO = dagService.selectOne(job.getDagId());
        fillGraph(job, dagDTO);
        return job;
    }

    private void fillGraph(WsDiJobDTO job, DagDTO dagDTO) {
        List<WsDiJobLinkDTO> jobLinkDTOS = dagDTO.getLinks().stream().map(link -> {
            WsDiJobLinkDTO dto = WsDiJobLinkConvert2.INSTANCE.toDto(link);
            dto.setJobId(job.getId());
            return dto;
        }).collect(Collectors.toList());
        List<WsDiJobStepDTO> jobStepDTOS = dagDTO.getSteps().stream().map(step -> {
            WsDiJobStepDTO dto = WsDiJobStepConvert2.INSTANCE.toDto(step);
            dto.setJobId(job.getId());
            return dto;
        }).collect(Collectors.toList());
        job.setJobLinkList(jobLinkDTOS);
        job.setJobStepList(jobStepDTOS);
        // 属性信息
        DiJobAttrVO jobAttrVO = WsDiJobAttrVOConvert.INSTANCE.toDto(dagDTO.getDagAttrs());
        jobAttrVO.setJobId(job.getId());
        job.setJobAttrList(jobAttrVO);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobStep(WsDiJobStepParam param) {
        WsDiJobDTO wsDiJobDTO = selectOne(param.getJobId());
        DagGraphVO jobGraphVO = JacksonUtil.parseJsonString(param.getJobGraph(), DagGraphVO.class);
        dagService.replace(wsDiJobDTO.getDagId(), jobGraphVO);
        return param.getJobId();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobGraph(WsDiJobGraphParam param) {
        WsDiJobDTO wsDiJobDTO = selectOne(param.getJobId());
        dagService.replace(wsDiJobDTO.getDagId(), param.getJobGraph());
        return param.getJobId();
    }

    @Override
    public DiJobAttrVO listJobAttrs(Long id) {
        WsDiJobDTO wsDiJobDTO = selectOne(id);
        DagInstanceDTO instanceDTO = dagService.selectSimpleOne(wsDiJobDTO.getDagId());
        DiJobAttrVO vo = WsDiJobAttrVOConvert.INSTANCE.toDto(instanceDTO.getDagAttrs());
        vo.setJobId(id);
        return vo;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobAttrs(DiJobAttrVO vo) {
        WsDiJobDTO wsDiJobDTO = selectOne(vo.getJobId());
        DagSimpleUpdateParam param = new DagSimpleUpdateParam();
        param.setId(wsDiJobDTO.getDagId());
        param.setDagAttrs(WsDiJobAttrVOConvert.INSTANCE.toDo(vo));
        dagService.update(param);
        return vo.getJobId();
    }

    @Override
    public Long totalCnt(String jobType) {
        return diJobMapper.selectCount(Wrappers.emptyWrapper());
    }

}
