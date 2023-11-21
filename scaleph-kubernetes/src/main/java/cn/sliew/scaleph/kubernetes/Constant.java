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

package cn.sliew.scaleph.kubernetes;

public enum Constant {
    ;
    // "resourceVersion="0" is any resource version.It saves time to access etcd and improves
    // performance.
    // https://kubernetes.io/docs/reference/using-api/api-concepts/#the-resourceversion-parameter
    public static final String KUBERNETES_ZERO_RESOURCE_VERSION = "0";

    public static final String[] JSON_PROPERTY_ORDER = {"apiVersion", "kind", "metadata", "spec", "status"};

    public static final String SCALEPH_GROUP = "scaleph.sliew.cn";
    public static final String SCALEPH_VERSION = "v1alpha";
    public static final String SCALEPH_API_VERSION = SCALEPH_GROUP + "/" + SCALEPH_VERSION;

    public static final String GROUP = "flink.apache.org";
    public static final String VERSION = "v1beta1";
    public static final String API_VERSION = GROUP + "/" + VERSION;

    public static final String FLINK_TEMPLATE = "FlinkTemplate";
    public static final String FLINK_DEPLOYMENT = "FlinkDeployment";
    public static final String FLINK_SESSION_CLUSTER = "FlinkSessionCluster";
    public static final String FLINK_SESSION_JOB = "FlinkSessionJob";
    public static final String FLINK_JOB = "FlinkJob";
    public static final String JAR_ARTIFACT = "JarArtifact";

}
