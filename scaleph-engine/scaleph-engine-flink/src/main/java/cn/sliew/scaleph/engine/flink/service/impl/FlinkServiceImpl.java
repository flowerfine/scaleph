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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.flinkful.cli.base.SessionClient;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.exception.CustomException;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterConfigService;
import cn.sliew.scaleph.engine.flink.service.FlinkReleaseService;
import cn.sliew.scaleph.engine.flink.service.FlinkService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
public class FlinkServiceImpl implements FlinkService {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;

    /**
     * requires:
     * 1. flink release
     * 2. resource provider certificate: hadoop conf or kubeconfig
     * 3. flink options
     */
    @Override
    public void createSessionCluster(Long clusterConfigId) throws Exception {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(clusterConfigId);
        final DictVO resourceProvider = flinkClusterConfigDTO.getResourceProvider();
        if (resourceProvider.getValue().equals(ResourceProvider.YARN.getCode())) {
            createYarnSessionCluster(flinkClusterConfigDTO);
        } else if (resourceProvider.getValue().equals(ResourceProvider.NATIVE_KUBERNETES.getCode())) {
            createKubernetesSessionCluster(flinkClusterConfigDTO);
        } else {
            throw new UnsupportedOperationException("only support YARN or Native Kubernetes session cluster!");
        }
    }

    private void createYarnSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkReleasePath = loadFlinkRelease(flinkClusterConfigDTO.getFlinkReleaseId(), workspace);
    }

    private void createKubernetesSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {

    }

    private Path loadFlinkRelease(Long flinkReleaseId, Path workspace) throws IOException {
        final FlinkReleaseDTO releaseDTO = flinkReleaseService.selectOne(flinkReleaseId);
        final Path tempFile = TempFileUtil.createTempFile(workspace, releaseDTO.getFileName());
        flinkReleaseService.download(flinkReleaseId, Files.newOutputStream(tempFile, StandardOpenOption.WRITE));
    }

    private Path getWorkspace() throws IOException {
        return TempFileUtil.createTempDir();
    }
}
