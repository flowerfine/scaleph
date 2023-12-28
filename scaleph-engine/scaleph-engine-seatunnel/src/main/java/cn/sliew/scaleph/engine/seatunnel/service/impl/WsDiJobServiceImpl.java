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
import cn.sliew.scaleph.common.dict.job.JobAttrType;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dag.service.DagService;
import cn.sliew.scaleph.dag.service.dto.DagInstanceDTO;
import cn.sliew.scaleph.dag.service.vo.DagGraphVO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.ws.WsDiJob;
import cn.sliew.scaleph.dao.mapper.master.ws.WsDiJobMapper;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobAttrService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobGraphService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.convert.WsDiJobConvert;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobAttrDTO;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.param.*;
import cn.sliew.scaleph.engine.seatunnel.service.vo.DiJobAttrVO;
import cn.sliew.scaleph.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsDiJobServiceImpl implements WsDiJobService {

    @Autowired
    private WsFlinkArtifactService wsFlinkArtifactService;
    @Autowired
    private WsDiJobMapper diJobMapper;
    @Autowired
    private WsDiJobGraphService wsDiJobGraphService;
    @Autowired
    private WsDiJobAttrService wsDiJobAttrService;
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

        DagInstanceDTO instanceDTO = new DagInstanceDTO();
        Long dagId = dagService.insert(instanceDTO);
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
        //todo check if there is running job instance
        wsDiJobGraphService.deleteBatch(Collections.singletonList(id));
        wsDiJobAttrService.deleteByJobId(Collections.singletonList(id));
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
        wsDiJobGraphService.queryJobGraph(job);
        job.setJobAttrList(wsDiJobAttrService.listJobAttr(id));
        return job;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobStep(WsDiJobStepParam param) {
        DagGraphVO jobGraphVO = JacksonUtil.parseJsonString(param.getJobGraph(), DagGraphVO.class);
        wsDiJobGraphService.saveJobGraph(param.getJobId(), jobGraphVO);
        WsDiJobDTO wsDiJobDTO = selectOne(param.getJobId());
        dagService.replace(wsDiJobDTO.getDagId(), jobGraphVO);

        WsDiJobStepParam copiedParam = BeanUtil.copy(param, new WsDiJobStepParam());
        copiedParam.setJobId(param.getJobId());
        wsDiJobGraphService.updateJobStep(copiedParam);

        return param.getJobId();
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobGraph(WsDiJobGraphParam param) {
        wsDiJobGraphService.saveJobGraph(param.getJobId(), param.getJobGraph());
        WsDiJobDTO wsDiJobDTO = selectOne(param.getJobId());
        dagService.replace(wsDiJobDTO.getDagId(), param.getJobGraph());
        return param.getJobId();
    }

    @Override
    public DiJobAttrVO listJobAttrs(Long id) {
        DiJobAttrVO vo = new DiJobAttrVO();
        vo.setJobId(id);
        List<WsDiJobAttrDTO> list = wsDiJobAttrService.listJobAttr(id);
        for (WsDiJobAttrDTO jobAttr : list) {
            String str = jobAttr.getJobAttrKey().concat("=").concat(jobAttr.getJobAttrValue());
            String tempStr = null;
            switch (jobAttr.getJobAttrType()) {
                case VARIABLE:
                    tempStr = StringUtils.hasText(vo.getJobAttr()) ? vo.getJobAttr() : "";
                    vo.setJobAttr(tempStr + str + "\n");
                    break;
                case ENV:
                    tempStr = StringUtils.hasText(vo.getJobProp()) ? vo.getJobProp() : "";
                    vo.setJobProp(tempStr + str + "\n");
                    break;
                case PROPERTIES:
                    tempStr = StringUtils.hasText(vo.getEngineProp()) ? vo.getEngineProp() : "";
                    vo.setEngineProp(tempStr + str + "\n");
                    break;
                default:
            }
        }
        return vo;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    @Override
    public Long saveJobAttrs(DiJobAttrVO vo) {
        Map<String, WsDiJobAttrDTO> map = new HashMap<>();
        parseJobAttr(map, vo.getJobAttr(), JobAttrType.VARIABLE, vo.getJobId());
        parseJobAttr(map, vo.getJobProp(), JobAttrType.ENV, vo.getJobId());
        parseJobAttr(map, vo.getEngineProp(), JobAttrType.PROPERTIES, vo.getJobId());
        wsDiJobAttrService.deleteByJobId(Collections.singletonList(vo.getJobId()));
        for (Map.Entry<String, WsDiJobAttrDTO> entry : map.entrySet()) {
            wsDiJobAttrService.upsert(entry.getValue());
        }
        return vo.getJobId();
    }

    private void parseJobAttr(Map<String, WsDiJobAttrDTO> map, String str, JobAttrType jobAttrType, Long jobId) {
        if (StringUtils.hasText(str)) {
            String[] lines = str.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StringUtils.hasText(kv[0]) && StringUtils.hasText(kv[1])) {
                    WsDiJobAttrDTO dto = new WsDiJobAttrDTO();
                    dto.setJobId(jobId);
                    dto.setJobAttrType(jobAttrType);
                    dto.setJobAttrKey(kv[0]);
                    dto.setJobAttrValue(kv[1]);
                    map.put(jobId + jobAttrType.getValue() + kv[0], dto);
                }
            }
        }
    }

    @Override
    public Long totalCnt(String jobType) {
        LambdaQueryWrapper<WsDiJob> queryWrapper = Wrappers.lambdaQuery(WsDiJob.class);
        return diJobMapper.selectCount(queryWrapper);
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        int result = 0;
        wsDiJobGraphService.clone(sourceJobId, targetJobId);
        result += wsDiJobAttrService.clone(sourceJobId, targetJobId);
        return result;
    }
}
