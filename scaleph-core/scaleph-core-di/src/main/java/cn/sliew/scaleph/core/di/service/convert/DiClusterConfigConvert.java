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

package cn.sliew.scaleph.core.di.service.convert;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiClusterConfigDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiClusterConfig;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import cn.sliew.scaleph.system.service.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiClusterConfigConvert extends BaseConvert<DiClusterConfig, DiClusterConfigDTO> {
    DiClusterConfigConvert INSTANCE = Mappers.getMapper(DiClusterConfigConvert.class);

    @Override
    default DiClusterConfig toDo(DiClusterConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        DiClusterConfig config = new DiClusterConfig();
        config.setId(dto.getId());
        config.setClusterName(dto.getClusterName());
        config.setClusterType(DictVoConvert.INSTANCE.toDo(dto.getClusterType()));
        config.setClusterHome(dto.getClusterHome());
        config.setClusterVersion(dto.getClusterVersion());
        config.setRemark(dto.getRemark());
        String conf = dto.getClusterConf();
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isNotEmpty(conf)) {
            String[] lines = conf.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StrUtil.isAllNotBlank(kv)) {
                    map.put(kv[0], kv[1]);
                }
            }
        }
        StringBuffer buffer = new StringBuffer();
        map.forEach((k, v) -> {
            buffer.append(k).append("=").append(v).append("\n");
        });
        config.setClusterConf(buffer.toString());
        config.setCreateTime(dto.getCreateTime());
        config.setCreator(dto.getCreator());
        config.setUpdateTime(dto.getUpdateTime());
        config.setEditor(dto.getEditor());
        return config;
    }

    @Override
    default DiClusterConfigDTO toDto(DiClusterConfig entity) {
        if (entity == null) {
            return null;
        }
        DiClusterConfigDTO dto = new DiClusterConfigDTO();
        dto.setId(entity.getId());
        dto.setClusterName(entity.getClusterName());
        dto.setClusterType(DictVO.toVO(DictConstants.CLUSTER_TYPE, entity.getClusterType()));
        dto.setClusterHome(entity.getClusterHome());
        dto.setClusterVersion(entity.getClusterVersion());
        dto.setClusterConf(entity.getClusterConf());
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setCreator(entity.getCreator());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setEditor(entity.getEditor());
        return dto;
    }
}
