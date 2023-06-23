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

package cn.sliew.scaleph.dao.entity.master.meta;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 元数据-参考数据
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "meta_data_set", resultMap = "MetaDataSetMap")
public class MetaDataSet extends BaseDO {

    private static final long serialVersionUID = -583630679647470414L;

    private Long dataSetTypeId;

    @TableField(exist = false)
    private MetaDataSetType dataSetType;

    private String dataSetCode;

    private String dataSetValue;

    private Long systemId;

    @TableField(exist = false)
    private MetaSystem system;

    private YesOrNo isStandard;

    private String remark;

}
