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

import java.io.Serializable;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.system.cache.DictCache;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gleiyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "数据字典", description = "数据字典对象，用来前后端枚举值交互")
public class DictVO implements Serializable {
    private static final long serialVersionUID = 1357098965682678688L;

    private String value;
    private String label;

    public static DictVO toVO(String dictTypeCode, String dictCode) {
        String dictValue = DictCache.getValueByKey(dictTypeCode + Constants.SEPARATOR + dictCode);
        return new DictVO(dictCode, dictValue);
    }
}
