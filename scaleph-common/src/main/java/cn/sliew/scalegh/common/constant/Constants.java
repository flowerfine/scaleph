package cn.sliew.scalegh.common.constant;

import java.io.File;

/**
 * @author gleiyu
 */
public interface Constants {

    /**
     * 默认日期格式
     */
    String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String MS_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    String SEPARATOR = "-";
    String PATH_SEPARATOR = File.separator;

    String DEFAULT_TIMEZONE = "GMT+8";
    String DEFAULT_CHARSET = "UTF-8";

    /**
     * 系统配置相关
     */
    String CFG_EMAIL_CODE = "email";
    String CFG_BASIC_CODE = "basic";
    /**
     * 角色相关
     */
    String USER_DEFINE_ROLE_PREFIX = "role_";
    String ROLE_NORMAL = "sys_normal";
    String ROLE_SYS_ADMIN = "sys_super_admin";
    /**
     * 验证码 key
     */
    String AUTH_CODE_KEY = "authCode-key_";
    /**
     * 在线用户TOKEN标识
     */
    String ONLINE_TOKEN_KEY = "online-token_";
    /**
     * 在线用户标识
     */
    String ONLINE_USER_KEY = "online-user_";
    /**
     * 用户token key
     */
    String TOKEN_KEY = "u_token";

    /**
     * schedule job and group
     */
    String JOB_PREFIX = "job-";
    String JOB_GROUP_PREFIX = "jobGrp-";
    String TRIGGER_PREFIX = "trigger-";
    String TRIGGER_GROUP_PREFIX = "triggerGrp-";
    String INTERNAL_GROUP = "sysInternal";
    String JOB_LOG_KEY = "traceLog";
    String ETL_JOB_PREFIX = "job-";
    String CRON_EVERY_THREE_SECONDS = "/3 * * * * ? ";
    /**
     * 作业流程步骤属性相关
     */
    String CLUSTER_DEPLOY_TARGET = "deploy_target";
    String JOB_ID = "jobId";
    String JOB_NAME = "job.name";
    String JOB_STEP_CODE = "stepCode";
    String JOB_STEP_TITLE = "stepTitle";
    String JOB_GRAPH = "jobGraph";
    String JOB_STEP_ATTR_DATASOURCE = "dataSource";
    String JOB_STEP_ATTR_DRIVER = "driver";
    String JOB_STEP_ATTR_URL = "url";
    String JOB_STEP_ATTR_USERNAME = "username";
    String JOB_STEP_ATTR_PASSWORD = "password";

}
