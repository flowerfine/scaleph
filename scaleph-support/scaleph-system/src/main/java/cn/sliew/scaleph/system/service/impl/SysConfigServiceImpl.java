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

package cn.sliew.scaleph.system.service.impl;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.dao.entity.master.system.SysConfig;
import cn.sliew.scaleph.dao.mapper.master.system.SysConfigMapper;
import cn.sliew.scaleph.system.service.SysConfigService;
import cn.sliew.scaleph.system.service.convert.SysConfigConvert;
import cn.sliew.scaleph.system.service.dto.SysConfigDTO;
import cn.sliew.scaleph.system.service.vo.BasicConfigVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 系统配置信息表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-10-10
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public int insert(SysConfigDTO sysConfigDTO) {
        SysConfig sysConfig = SysConfigConvert.INSTANCE.toDo(sysConfigDTO);
        return this.sysConfigMapper.insert(sysConfig);
    }

    @Override
    public int updateByCode(SysConfigDTO sysConfigDTO) {
        SysConfig sysConfig = SysConfigConvert.INSTANCE.toDo(sysConfigDTO);
        return this.sysConfigMapper.update(sysConfig, new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getCfgCode, sysConfig.getCfgCode()));
    }

    @Override
    public int deleteByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return 0;
        } else {
            return this.sysConfigMapper.delete(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getCfgCode, code));
        }

    }

    @Override
    public SysConfigDTO selectByCode(String code) {
        SysConfig sysConfig =
            this.sysConfigMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getCfgCode, code));
        return SysConfigConvert.INSTANCE.toDto(sysConfig);
    }

    @Override
    public String getSeatunnelHome() {
        SysConfigDTO sysConfig = this.selectByCode(Constants.CFG_BASIC_CODE);
        if (sysConfig != null) {
            BasicConfigVO config =
                JacksonUtil.parseJsonString(sysConfig.getCfgValue(), BasicConfigVO.class);
            return config.getSeatunnelHome();
        } else {
            return null;
        }
    }
}
