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

package cn.sliew.scaleph.application.doris.operator.spec;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fabric8.kubernetes.api.model.*;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * BaseSpec describe the foundation spec of pod about doris components.
 */
@Data
public class BaseSpec {

    /**
     * annotation for fe pods. user can config monitor annotation for collect to monitor system.
     */
    private Map<String, String> annotations;

    /**
     * (Optional) If specified, the pod’s nodeSelector，displayName=“Map of nodeSelectors to match when scheduling pods on nodes”
     */
    @Nullable
    private Map<String, String> nodeSelector;

    /**
     * podLabels for user selector or classify pods
     */
    @Nullable
    private Map<String, String> podLabels;

    /**
     * Image for a doris cn deployment.
     */
    private String image;

    /**
     * ImagePullSecrets is an optional list of references to secrets in the same namespace to use for pulling any of the images used by this PodSpec. If specified, these secrets will be passed to individual puller implementations for them to use. More info: https://kubernetes.io/docs/concepts/containers/images#specifying-imagepullsecrets-on-a-pod
     */
    @Nullable
    private List<LocalObjectReference> imagePullSecrets;

    /**
     * serviceAccount for cn access cloud service.
     */
    private String serviceAccount;

    /**
     * Replicas is the number of desired cn Pod.
     */
    @Nullable
    private Integer replicas;

    /**
     * (Members of ResourceRequirements are embedded into this type.)
     */
    @JsonProperty("limits")
    private Map<String, Quantity> limits;

    @JsonProperty("requests")
    private Map<String, Quantity> requests;

    /**
     * (Optional) Tolerations for scheduling pods onto some dedicated nodes
     */
    @Nullable
    private List<Toleration> tolerations;

    /**
     * the reference for cn configMap.
     */
    @Nullable
    private ConfigMapInfo configMapInfo;

    /**
     * cnEnvVars is a slice of environment variables that are added to the pods, the default is empty.
     */
    @Nullable
    private List<EnvVar> envVars;

    /**
     * SystemInitialization for fe, be and cn setting system parameters.
     */
    private SystemInitialization systemInitialization;

    /**
     *
     */
    private List<PersistentVolume> persistentVolumes;

    /**
     * If specified, the pod’s scheduling constraints.
     */
    @Nullable
    private Affinity affinity;

    /**
     * HostAliases is an optional list of hosts and IPs that will be injected into the pod’s hosts file if specified. This is only valid for non-hostNetwork pods.
     */
    @Nullable
    private List<HostAlias> hostAliases;

    /**
     * expose the be listen ports
     */
    private ExportService service;

    /**
     * specify register fe addresses
     */
    private FeAddress feAddress;

    /**
     * A special supplemental group that applies to all containers in a pod. Some volume types allow the Kubelet to change the ownership of that volume to be owned by the pod:
     */
    private Long fsGroup;
}
