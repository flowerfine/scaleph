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

package cn.sliew.scaleph.system.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.common.dict.DictDefinition;
import cn.sliew.scaleph.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import cn.sliew.scaleph.system.service.dto.SysDictDTO;
import cn.sliew.scaleph.system.service.param.SysDictParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
public interface SysDictService {

    int insert(SysDictDTO sysDictDTO);

    int update(SysDictDTO sysDictDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    int deleteByType(String dictCodeType);

    SysDictDTO selectOne(Long id);

    List<SysDictDTO> selectByType(String dictTypeCode);

    List<DictInstance> selectByType2(DictType type);

    List<SysDictDTO> selectAll();

    Page<SysDictDTO> listByPage(SysDictParam param);
}
