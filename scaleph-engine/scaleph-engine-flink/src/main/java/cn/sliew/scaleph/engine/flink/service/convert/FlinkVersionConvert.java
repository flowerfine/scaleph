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

package cn.sliew.scaleph.engine.flink.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlinkVersionConvert extends BaseConvert<FlinkVersion, org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion> {
    FlinkVersionConvert INSTANCE = Mappers.getMapper(FlinkVersionConvert.class);

    @Override
    default FlinkVersion toDo(org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion dto) {
        throw new UnsupportedOperationException();
    }

    @Override
    default org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion toDto(FlinkVersion entity) {
        for (org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion flinkVersion : org.apache.flink.kubernetes.operator.crd.spec.FlinkVersion.values()) {
            if (entity.name().startsWith(flinkVersion.name())) {
                return flinkVersion;
            }
        }

        throw new IllegalStateException("unknown flink version for " + JacksonUtil.toJsonString(entity));
    }
}
