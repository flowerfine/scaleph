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

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.dao.entity.master.di.DiJob;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * @author gleiyu
 */
@Mapper(uses = {DiDirectoryConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DiJobConvert extends BaseConvert<DiJob, DiJobDTO> {
    DiJobConvert INSTANCE = Mappers.getMapper(DiJobConvert.class);

    @Override
    @Mapping(source = "directory.id", target = "directoryId")
    DiJob toDo(DiJobDTO dto);

    @Override
    @Mapping(source = "directoryId", target = "directory.id")
    DiJobDTO toDto(DiJob entity);
}
