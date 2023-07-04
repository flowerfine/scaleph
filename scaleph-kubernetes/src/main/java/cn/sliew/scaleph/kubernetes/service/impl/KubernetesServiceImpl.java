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

package cn.sliew.scaleph.kubernetes.service.impl;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.util.SystemUtil;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class KubernetesServiceImpl implements KubernetesService {

    private ConcurrentMap<Long, KubernetesClient> cache = new ConcurrentHashMap<>(4);

    @Autowired
    private ClusterCredentialService clusterCredentialService;

    @Override
    public KubernetesClient getClient(Long clusterCredentialId) {
        return cache.computeIfAbsent(clusterCredentialId, this::buildClient);
    }

    private KubernetesClient buildClient(Long clusterCredentialId) {
        try {
            Config config = getConfig(clusterCredentialId);
            return new KubernetesClientBuilder().withConfig(config).build();
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public Path downloadConfig(ClusterCredentialDTO clusterCredential) throws IOException {
        Long clusterCredentialId = clusterCredential.getId();
        Path kubeConfigPath = SystemUtil.getKubeConfigPath();
        Path kubeConfig = FileUtil.createFile(kubeConfigPath, clusterCredentialId.toString());
        try (OutputStream outputStream = FileUtil.getOutputStream(kubeConfig)) {
            clusterCredentialService.download(clusterCredentialId, outputStream);
        }
        return kubeConfig;
    }

    @Override
    public Config getConfig(Long clusterCredentialId) throws IOException {
        ClusterCredentialDTO clusterCredentialDTO = clusterCredentialService.selectOne(clusterCredentialId);
        Path kubeConfig = downloadConfig(clusterCredentialDTO);
        try (InputStream inputStream = FileUtil.getInputStream(kubeConfig)) {
            String kubeContent = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));
            if (StringUtils.hasText(clusterCredentialDTO.getContext())) {
                return Config.fromKubeconfig(clusterCredentialDTO.getContext(), kubeContent, null);
            }
            return Config.fromKubeconfig(null, kubeContent, null);
        }
    }
}
