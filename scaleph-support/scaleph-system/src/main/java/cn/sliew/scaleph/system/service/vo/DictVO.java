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

package cn.sliew.scaleph.system.service.vo;

import cn.sliew.carp.framework.common.dict.DictInstance;
import cn.sliew.scaleph.common.dict.DictType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author gleiyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "数据字典", description = "数据字典对象，用来前后端枚举值交互")
public class DictVO implements Serializable {
    private static final long serialVersionUID = 1357098965682678688L;

    private String value;
    private String label;

    public static DictVO toVO(String dictTypeCode, String dictCode) {
        if (StringUtils.hasText(dictTypeCode) && StringUtils.hasText(dictCode)) {
            DictType dictType = DictType.of(dictTypeCode);
            List<DictInstance> dictInstances = EnumUtils.getEnumList(dictType.getInstanceClass());
            Optional<DictInstance> optional = dictInstances.stream()
                    .filter(dictInstance -> dictInstance.getValue().equals(dictCode))
                    .findAny();
            if (optional.isPresent()) {
                return new DictVO(dictCode, optional.get().getLabel());
            }
            throw new EnumConstantNotPresentException(dictType.getInstanceClass(), dictCode);
        }
        return null;
    }
}
