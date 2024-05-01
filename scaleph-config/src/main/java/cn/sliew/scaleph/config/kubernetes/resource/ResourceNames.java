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

package cn.sliew.scaleph.config.kubernetes.resource;

public enum ResourceNames {
    ;

    public static final String LOCAL_SCHEMA = "local://";

    public static final String FLINK_MAIN_CONTAINER_NAME = "flink-main-container";
    public static final String FILE_FETCHER_CONTAINER_NAME = "scaleph-file-fetcher";

    public static final String POD_TEMPLATE_NAME = "pod-template";
    public static final String JOB_MANAGER_POD_TEMPLATE_NAME = "task-manager-pod-template";
    public static final String TASK_MANAGER_POD_TEMPLATE_NAME = "task-manager-pod-template";

    public static final String FILE_FETCHER_CONTAINER_IMAGE = "ghcr.io/flowerfine/scaleph/scaleph-file-fetcher:v2.0.2";

    public static final String FILE_FETCHER_SCALEPH_VOLUME_NAME = "file-fetcher-scaleph-volume";
    public static final String SCALEPH_JAR_DIRECTORY = "/scaleph/jar/";
    public static final String JAR_LOCAL_PATH = LOCAL_SCHEMA + SCALEPH_JAR_DIRECTORY;

    public static final String FILE_FETCHER_FLINK_VOLUME_NAME = "file-fetcher-flink-volume";
    public static final String LIB_DIRECTORY = "/scaleph/usrlib/";
    public static final String LIB_LOCAL_PATH = LOCAL_SCHEMA + LIB_DIRECTORY;

    public static final String SQL_DIRECTORY = "/scaleph/sql/";
    public static final String SQL_RUNNER_JAR = "sql-runner.jar";
    public static final String SQL_RUNNER_ENTRY_CLASS = "cn.sliew.scaleph.engine.sql.SqlRunner";
    public static final String SQL_LOCAL_PATH = LOCAL_SCHEMA + SQL_DIRECTORY;
    public static final String SQL_RUNNER_LOCAL_PATH = LOCAL_SCHEMA + SQL_DIRECTORY + SQL_RUNNER_JAR;


    public static final String SQL_SCRIPTS_VOLUME_NAME = "sql-scripts-volume";
    public static final String SQL_SCRIPTS_DIRECTORY = "/scaleph/sql-scripts/";
    public static final String SQL_SCRIPTS_LOCAL_PATH = LOCAL_SCHEMA + SQL_SCRIPTS_DIRECTORY;

    public static final String SEATUNNEL_CONF_VOLUME_NAME = "seatunnel-conf-volume";
    public static final String SEATUNNEL_CONF_DIRECTORY = "/scaleph/seatunnel/";
    public static final String SEATUNNEL_CONF_FILE = "conf.json";
    public static final String SEATUNNEL_CONF_FILE_PATH = SEATUNNEL_CONF_DIRECTORY + SEATUNNEL_CONF_FILE;
    public static final String SEATUNNEL_STARTER_PATH = LOCAL_SCHEMA + "/opt/seatunnel/starter/";

    public static final String FLINK_CDC_YAML_VOLUME_NAME = "flink-cdc-yaml-volume";
    public static final String FLINK_CDC_YAML_DIRECTORY = "/scaleph/flink-cdc/";
    public static final String FLINK_CDC_YAML_FILE = "flink-cdc.yaml";
    public static final String FLINK_CDC_YAML_FILE_PATH = FLINK_CDC_YAML_DIRECTORY + FLINK_CDC_YAML_FILE;
    public static final String FLINK_CDC_JAR_PATH = LOCAL_SCHEMA + "/opt/flink-cdc/lib/";

}
