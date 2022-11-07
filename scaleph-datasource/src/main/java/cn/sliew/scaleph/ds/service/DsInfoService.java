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

package cn.sliew.scaleph.ds.service;

import cn.sliew.scaleph.common.dict.job.DataSourceType;
import cn.sliew.scaleph.ds.modal.AbstractDataSource;
import cn.sliew.scaleph.ds.service.dto.DsInfoDTO;
import cn.sliew.scaleph.ds.service.param.DsInfoListParam;
import cn.sliew.scaleph.resource.service.ResourceDescriptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface DsInfoService extends ResourceDescriptor<DsInfoDTO> {

    Page<DsInfoDTO> list(DsInfoListParam param);

    List<DsInfoDTO> listByType(DataSourceType type);

    DsInfoDTO selectOne(Long id, boolean decrypt);

    int insert(AbstractDataSource dataSource);

    int update(Long id, AbstractDataSource dataSource);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

}
