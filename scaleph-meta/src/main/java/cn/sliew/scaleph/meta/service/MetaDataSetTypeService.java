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

package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.meta.service.dto.MetaDataSetTypeDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataSetTypeParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 元数据-参考数据类型 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
public interface MetaDataSetTypeService {

    int insert(MetaDataSetTypeDTO metaDataSetTypeDTO);

    int update(MetaDataSetTypeDTO metaDataSetTypeDTO);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    Page<MetaDataSetTypeDTO> listByPage(MetaDataSetTypeParam param);
}
