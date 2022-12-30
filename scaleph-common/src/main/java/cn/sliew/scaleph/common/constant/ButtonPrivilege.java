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

public enum ButtonPrivilege {
    ;

    public static final String ADMIN_DEPT_ADD = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.ADD;
    public static final String ADMIN_DEPT_EDIT = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.EDIT;
    public static final String ADMIN_DEPT_DELETE = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.DELETE;
    public static final String ADMIN_DEPT_SELECT = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.SELECT;
    public static final String ADMIN_DEPT_AUTHORIZE = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.AUTHORIZE;
    public static final String ADMIN_DEPT_UNAUTHORIZE = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_DEPT + ButtonCode.UNAUTHORIZE;

    public static final String ADMIN_ROLE_ADD = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_ROLE + ButtonCode.ADD;
    public static final String ADMIN_ROLE_EDIT = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_ROLE + ButtonCode.EDIT;
    public static final String ADMIN_ROLE_DELETE = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_ROLE + ButtonCode.DELETE;
    public static final String ADMIN_ROLE_AUTHORIZE = PagePrivilege.PRIVILEGE_PREFIX + PageCode.ADMIN_ROLE + ButtonCode.AUTHORIZE;

}
