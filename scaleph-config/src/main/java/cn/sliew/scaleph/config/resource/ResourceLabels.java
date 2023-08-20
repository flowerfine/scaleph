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

package cn.sliew.scaleph.config.resource;

public enum ResourceLabels {
    ;

    public static final String SCALEPH = "scaleph";
    public static final String SCALEPH_GROUP = "scaleph.sliew.cn";
    public static final String SCALEPH_VERSION = "v1alpha";
    public static final String SCALEPH_API_VERSION = SCALEPH_GROUP + "/" + SCALEPH_VERSION;

    public static final String SCALEPH_LABEL_PLATFROM = SCALEPH_GROUP + "/" + "platform";
    public static final String SCALEPH_LABEL_NAME = SCALEPH_GROUP + "/" + "name";
    public static final String SCALEPH_LABEL_DEPLOYMENT_ID = SCALEPH_GROUP + "/" + "deploymentId";
    public static final String SCALEPH_LABEL_SESSION_CLUSTER_ID = SCALEPH_GROUP + "/" + "sessionClusterId";
    public static final String SCALEPH_LABEL_JOB_ID = SCALEPH_GROUP + "/" + "jobId";
    public static final String SCALEPH_LABEL_JOB_INSTANCE_ID_NUMBER = SCALEPH_GROUP + "/" + "jobInstanceIdNumber";
    public static final String SCALEPH_LABEL_JOB_INSTANCE_ID = SCALEPH_GROUP + "/" + "jobInstanceId";
    public static final String SCALEPH_LABEL_CREATOR = SCALEPH_GROUP + "/" + "creator";
    public static final String SCALEPH_LABEL_EDITOR = SCALEPH_GROUP + "/" + "editor";

}
