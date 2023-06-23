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

package cn.sliew.scaleph.system.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gleiyu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页参数")
public class PaginationParam implements Serializable {
    private static final long serialVersionUID = -860020632404225667L;

    @Schema(description = "页码", example = "1")
    private Long current = 1L;

    @Schema(description = "页面大小", example = "10")
    private Long pageSize = 10L;

    @Schema(description = "排序", example = "[{direction: \"ASC\"" + "field: \"fieldName\"}]")
    private List<SortArg> sorter;

    public List<OrderItem> buildSortItems() {
        List<OrderItem> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(sorter)) {
            for (SortArg arg : sorter) {
                if (StrUtil.isAllNotBlank(arg.getField())) {
                    if ("desc".equalsIgnoreCase(arg.getDirection())) {
                        list.add(OrderItem.desc(StrUtil.toUnderlineCase(arg.getField())));
                    } else if ("asc".equalsIgnoreCase(arg.getDirection())) {
                        list.add(OrderItem.asc(StrUtil.toUnderlineCase(arg.getField())));
                    } else {
                        list.add(OrderItem.asc(StrUtil.toUnderlineCase(arg.getField())));
                    }
                }
            }
        }
        return list;
    }

    public Long getCurrent() {
        return current == null || current < 1L ? 1L : current;
    }

    public Long getPageSize() {
        return pageSize == null || pageSize < 1L ? 10L : pageSize;
    }

}
