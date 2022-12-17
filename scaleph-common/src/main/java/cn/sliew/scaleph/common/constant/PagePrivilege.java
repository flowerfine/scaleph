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

package cn.sliew.scaleph.common.constant;

public enum PagePrivilege {
    ;

    public static final String PRIVILEGE_PREFIX = "p";

    public static final String STUDIO_SHOW = PRIVILEGE_PREFIX + MenuResource.STUDIO.getCode() + ButtonCode.SHOW;
    public static final String STUDIO_DATA_BOARD_SHOW = PRIVILEGE_PREFIX + PageResource.STUDIO_DATA_BOARD + ButtonCode.SHOW;

    public static final String ADMIN_DEPT_SHOW = PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.SHOW;
    public static final String ADMIN_ROLE_SHOW = PRIVILEGE_PREFIX + PageCode.ADMIN_ROLE + ButtonCode.SHOW;
}
