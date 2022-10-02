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

import java.io.File;

/**
 * @author gleiyu
 */
public enum Constants {
    ;

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String MS_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String SEPARATOR = "-";
    public static final String PATH_SEPARATOR = File.separator;

    public static final String DEFAULT_TIMEZONE = "GMT+8";
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 系统配置相关
     */
    public static final String CFG_EMAIL_CODE = "email";
    public static final String CFG_BASIC_CODE = "basic";
    /**
     * 角色相关
     */
    public static final String USER_DEFINE_ROLE_PREFIX = "role_";
    public static final String ROLE_NORMAL = "sys_normal";
    public static final String ROLE_SYS_ADMIN = "sys_super_admin";
    /**
     * 验证码 key
     */
    public static final String AUTH_CODE_KEY = "authCode-key_";
    /**
     * 在线用户TOKEN标识
     */
    public static final String ONLINE_TOKEN_KEY = "online-token_";
    /**
     * 在线用户标识
     */
    public static final String ONLINE_USER_KEY = "online-user_";
    /**
     * 用户token key
     */
    public static final String TOKEN_KEY = "u_token";

    public static final String CODEC_STR_PREFIX = "Encrypted:";

    /**
     * schedule job and group
     */
    public static final String JOB_PREFIX = "job-";
    public static final String JOB_GROUP_PREFIX = "jobGrp-";
    public static final String TRIGGER_PREFIX = "trigger-";
    public static final String TRIGGER_GROUP_PREFIX = "triggerGrp-";
    public static final String INTERNAL_GROUP = "sysInternal";
    public static final String JOB_LOG_KEY = "traceLog";
    public static final String ETL_JOB_PREFIX = "job-";
    public static final String CRON_EVERY_THREE_SECONDS = "/3 * * * * ? ";
    public static final String JOB_PARAM_JOB_INFO = "JOB_INFO";
    public static final String JOB_PARAM_PROJECT_INFO = "PROJECT_INFO";
    /**
     * 作业流程步骤属性相关
     */
    public static final String CLUSTER_DEPLOY_TARGET = "deploy_target";
    public static final String JOB_ID = "jobId";
    public static final String JOB_NAME = "job.name";
    public static final String JOB_STEP_CODE = "stepCode";
    public static final String JOB_STEP_TITLE = "stepTitle";
    public static final String JOB_STEP_ATTRS = "stepAttrs";
    public static final String JOB_GRAPH = "jobGraph";
    public static final String JOB_STEP_ATTR_DATASOURCE = "dataSource";
    public static final String JOB_STEP_ATTR_DRIVER = "driver";
    public static final String JOB_STEP_ATTR_URL = "url";
    public static final String JOB_STEP_ATTR_USERNAME = "username";
    public static final String JOB_STEP_ATTR_PASSWORD = "password";

}
