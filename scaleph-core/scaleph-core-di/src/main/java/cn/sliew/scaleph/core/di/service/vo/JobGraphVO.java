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

package cn.sliew.scaleph.core.di.service.vo;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 前端job graph
 *
 * @author gleiyu
 */
@Data
@ApiModel(value = "作业图对象", description = "作业图信息")
public class JobGraphVO {
    private String id;
    private String shape;
    private Map<String, Integer> position;
    private Map<String, Object> data;
    private EdgeNodeVO source;
    private EdgeNodeVO target;
}