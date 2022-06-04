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

package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum JobStatusEnum {

    /**
     * 草稿  只有当前未发布版本为草稿状态，一个作业可能有0个或者1个草稿版本
     */
    DRAFT("1", "草稿"),
    /**
     * 发布  每个作业只有一个发布版本
     */
    RELEASE("2", "发布"),
    /**
     * 归档  当作业新版本发布后，上一次的发布版本变更为归档
     */
    ARCHIVE("3", "归档"),
    ;

    private String value;
    private String label;

    JobStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
