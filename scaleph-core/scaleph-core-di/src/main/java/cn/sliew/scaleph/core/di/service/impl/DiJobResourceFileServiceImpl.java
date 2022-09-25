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

package cn.sliew.scaleph.core.di.service.impl;

import java.util.List;

import cn.sliew.scaleph.core.di.service.DiJobResourceFileService;
import cn.sliew.scaleph.core.di.service.convert.DiResourceFileConvert;
import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.di.DiJobResourceFile;
import cn.sliew.scaleph.dao.entity.master.di.DiResourceFile;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobResourceFileMapper;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 数据集成-作业资源 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2022-04-19
 */
@Service
public class DiJobResourceFileServiceImpl implements DiJobResourceFileService {

    @Autowired
    private DiJobResourceFileMapper diJobResourceFileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int bindResource(Long jobId, List<DictVO> resources) {
        this.diJobResourceFileMapper.delete(
            new LambdaQueryWrapper<DiJobResourceFile>()
                .eq(DiJobResourceFile::getJobId, jobId)
        );
        int result = 0;
        for (DictVO dto : resources) {
            DiJobResourceFile jobFile = new DiJobResourceFile();
            jobFile.setJobId(jobId);
            jobFile.setResourceFileId(Long.valueOf(dto.getValue()));
            result += this.diJobResourceFileMapper.insert(jobFile);
        }
        return result;
    }

    @Override
    public List<DiResourceFileDTO> listJobResources(Long jobId) {
        List<DiResourceFile> list = this.diJobResourceFileMapper.listJobResources(jobId);
        return DiResourceFileConvert.INSTANCE.toDto(list);
    }

    @Override
    public int clone(Long sourceJobId, Long targetJobId) {
        return this.diJobResourceFileMapper.clone(sourceJobId, targetJobId);
    }
}
