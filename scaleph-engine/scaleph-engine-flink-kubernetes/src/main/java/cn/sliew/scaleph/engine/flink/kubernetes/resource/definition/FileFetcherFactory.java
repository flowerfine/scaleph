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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition;

import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizer;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizerFactory;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Quantity;

public class FileFetcherFactory implements ResourceCustomizerFactory<PodBuilder> {

    private static final String FILE_FETCHER_CONTAINER_NAME = "scaleph-file-fetcher";
    private static final String FILE_FETCHER_VOLUME_NAME = "file-fetcher-volume";

    private static final double MIN_FILE_FETCHER_CORES = 0.1;
    private static final double MAX_FILE_FETCHER_CORES = 1;
    private static final Quantity FILE_FETCHER_CONTAINER_MEMORY = Quantity.parse("512Mi");

    private static final String TARGET_DIRECTORY = "/flink/usrlib/";
    
    private WsFlinkKubernetesJobDTO wsFlinkKubernetesJob;

    public FileFetcherFactory(WsFlinkKubernetesJobDTO wsFlinkKubernetesJob) {
        this.wsFlinkKubernetesJob = wsFlinkKubernetesJob;
    }

    @Override
    public ResourceCustomizer<PodBuilder> create() {
        return builder -> {
            addAdditionalJars(builder);
            addArtifactJar(builder);
        };
    }

    private void addArtifactJar(PodBuilder builder) {
        switch (wsFlinkKubernetesJob.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddAdditionalJars(builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void addAdditionalJars(PodBuilder builder) {
        switch (wsFlinkKubernetesJob.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddAdditionalJars(builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void doAddAdditionalJars(PodBuilder builder) {

    }
}
