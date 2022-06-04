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

package cn.sliew.scaleph.core.di.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.dto.DiResourceFileDTO;
import cn.sliew.scaleph.core.di.service.param.DiResourceFileParam;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集成-资源 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-13
 */
public interface DiResourceFileService {

    int insert(DiResourceFileDTO dto);

    int update(DiResourceFileDTO dto);

    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<DiResourceFileDTO> listByPage(DiResourceFileParam param);

    List<DiResourceFileDTO> listByIds(Collection<? extends Serializable> ids);

    List<DictVO> listByProjectId(Long projectId);
}
