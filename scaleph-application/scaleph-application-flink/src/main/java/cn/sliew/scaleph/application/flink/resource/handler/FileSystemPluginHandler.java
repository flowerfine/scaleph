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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.application.flink.resource.definition.job.instance.FlinkJobInstanceConverterFactory;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.util.NetUtils;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import cn.sliew.scaleph.config.storage.S3FileSystemProperties;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodFluent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FileSystemPluginHandler {

    private static final String S3_ENDPOINT = "s3.endpoint";
    private static final String S3_ACCESS_KEY = "s3.access-key";
    private static final String S3_SECRET_KEY = "s3.secret-key";
    private static final String S3_PATH_STYLE_ACCESS = "s3.path.style.access";
    private static final String FS_ALLOWED_FALLBACK_FILESYSTEM = "fs.allowed-fallback-filesystems";

    private static final String FILE_SYSTEM_ENV_NAME = "ENABLE_BUILT_IN_PLUGINS";
    private static final String S3_FILE_SYSTEM_TEMPLATE = "flink-s3-fs-hadoop-%s.jar";

    @Autowired(required = false)
    private S3FileSystemProperties s3FileSystemProperties;

    public void handle(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentSpec spec) {
        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        handlePodTemplate(FlinkJobInstanceConverterFactory.getFlinkVersion(jobDTO), podBuilder);
        spec.setPodTemplate(podBuilder.build());

        Map<String, String> flinkConfiguration = Optional.ofNullable(spec.getFlinkConfiguration()).orElse(new HashMap<>());
        addFileSystemConfigOption(flinkConfiguration);
    }

    public void handle(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        FlinkVersion flinkVersion = FlinkVersion.of(sessionClusterDTO.getKubernetesOptions().getFlinkVersion());
        handlePodTemplate(flinkVersion, podBuilder);
        spec.setPodTemplate(podBuilder.build());

        Map<String, String> flinkConfiguration = Optional.ofNullable(spec.getFlinkConfiguration()).orElse(new HashMap<>());
        addFileSystemConfigOption(flinkConfiguration);
    }

    private void handlePodTemplate(FlinkVersion flinkVersion, PodBuilder builder) {
        builder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToEnv(buildEnableFileSystemEnv(flinkVersion))
                .endContainer();

        spec.endSpec();
    }

    void addFileSystemConfigOption(Map<String, String> flinkConfiguration) {
        if (s3FileSystemProperties != null) {
            flinkConfiguration.put(S3_ENDPOINT, NetUtils.replaceLocalhost(s3FileSystemProperties.getEndpoint()));
            flinkConfiguration.put(S3_ACCESS_KEY, s3FileSystemProperties.getAccessKey());
            flinkConfiguration.put(S3_SECRET_KEY, s3FileSystemProperties.getSecretKey());
            flinkConfiguration.put(S3_PATH_STYLE_ACCESS, "true"); // container
            flinkConfiguration.put(FS_ALLOWED_FALLBACK_FILESYSTEM, "s3"); // container
        }
    }

    private List<EnvVar> buildEnableFileSystemEnv(FlinkVersion flinkVersion) {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName(FILE_SYSTEM_ENV_NAME);
        builder.withValue(String.format(S3_FILE_SYSTEM_TEMPLATE, flinkVersion.getValue()));
        return Collections.singletonList(builder.build());
    }
}
