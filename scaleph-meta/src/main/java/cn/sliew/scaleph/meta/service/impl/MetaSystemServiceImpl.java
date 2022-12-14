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

package cn.sliew.scaleph.meta.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.DataSourceConstants;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.dao.entity.master.meta.MetaSystem;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataSetMapper;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaSystemMapper;
import cn.sliew.scaleph.meta.service.MetaSystemService;
import cn.sliew.scaleph.meta.service.convert.MetaSystemConvert;
import cn.sliew.scaleph.meta.service.dto.MetaSystemDTO;
import cn.sliew.scaleph.meta.service.param.MetaSystemParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 业务系统元数据服务实现类
 *
 * @author gleiyu
 */
@Service
public class MetaSystemServiceImpl implements MetaSystemService {
    @Autowired
    private MetaSystemMapper metaSystemMapper;
    @Autowired
    private MetaDataSetMapper metaDataSetMapper;

    @Override
    public int insert(MetaSystemDTO metaSystem) {
        MetaSystem meta = MetaSystemConvert.INSTANCE.toDo(metaSystem);
        return this.metaSystemMapper.insert(meta);
    }

    @Override
    public int update(MetaSystemDTO metaSystem) {
        MetaSystem meta = MetaSystemConvert.INSTANCE.toDo(metaSystem);
        return this.metaSystemMapper.updateById(meta);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteById(Long id) {
        this.metaDataSetMapper.delete(
                new LambdaQueryWrapper<MetaDataSet>().eq(MetaDataSet::getSystemId, id)
        );
        return this.metaSystemMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = DataSourceConstants.MASTER_TRANSACTION_MANAGER_FACTORY)
    public int deleteBatch(List<Long> ids) {
        this.metaDataSetMapper.delete(
                new LambdaQueryWrapper<MetaDataSet>().in(MetaDataSet::getSystemId, ids)
        );
        return this.metaSystemMapper.deleteBatchIds(ids);
    }

    @Override
    public Page<MetaSystemDTO> listByPage(MetaSystemParam param) {
        Page<MetaSystem> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<MetaSystem> queryWrapper = Wrappers.lambdaQuery(MetaSystem.class)
                .like(StrUtil.isNotEmpty(param.getSystemCode()), MetaSystem::getSystemCode, param.getSystemCode())
                .like(StrUtil.isNotEmpty(param.getSystemName()), MetaSystem::getSystemName, param.getSystemName());
        Page<MetaSystem> list = this.metaSystemMapper.selectPage(page, queryWrapper);
        Page<MetaSystemDTO> result = new Page(list.getCurrent(), list.getSize(), list.getTotal());
        List<MetaSystemDTO> dtoList = MetaSystemConvert.INSTANCE.toDto(list.getRecords());
        result.setRecords(dtoList);
        return result;
    }

}
