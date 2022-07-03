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
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.ResourceProvider;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.engine.flink.enums.ConfigType;
import cn.sliew.scaleph.engine.flink.enums.FlinkClusterStatus;
import cn.sliew.scaleph.engine.flink.service.*;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkClusterInstanceDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkDeployConfigFileDTO;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkSessionClusterAddParam;
import cn.sliew.scaleph.engine.flink.service.vo.FileStatusVO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.*;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinkServiceImpl implements FlinkService {

    @Autowired
    private FlinkClusterConfigService flinkClusterConfigService;
    @Autowired
    private FlinkReleaseService flinkReleaseService;
    @Autowired
    private FlinkDeployConfigFileService flinkDeployConfigFileService;
    @Autowired
    private FlinkClusterInstanceService flinkClusterInstanceService;

    /**
     * requires:
     * 1. flink release
     * 2. resource provider certificate: hadoop conf or kubeconfig
     * 3. flink options
     */
    @Override
    public void createSessionCluster(FlinkSessionClusterAddParam param) throws Exception {
        final FlinkClusterConfigDTO flinkClusterConfigDTO = flinkClusterConfigService.selectOne(param.getFlinkClusterConfigId());
        final DictVO resourceProvider = flinkClusterConfigDTO.getResourceProvider();
        ClusterClient clusterClient;
        if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.YARN.getCode()))) {
            clusterClient = createYarnSessionCluster(flinkClusterConfigDTO);
        } else if (resourceProvider.getValue().equals(String.valueOf(ResourceProvider.NATIVE_KUBERNETES.getCode()))) {
            clusterClient = createKubernetesSessionCluster(flinkClusterConfigDTO);
        } else {
            clusterClient = createExistingSessionCluster(flinkClusterConfigDTO);
        }

        FlinkClusterInstanceDTO dto = new FlinkClusterInstanceDTO();
        dto.setFlinkClusterConfigId(flinkClusterConfigDTO.getId());
        dto.setName(flinkClusterConfigDTO.getName() + "-" + RandomStringUtils.randomAlphabetic(8));
        dto.setClusterId(clusterClient.getClusterId().toString());
        dto.setWebInterfaceUrl(clusterClient.getWebInterfaceURL());
        dto.setStatus(DictVO.toVO(DictConstants.FLINK_CLUSTER_STATUS, String.valueOf(FlinkClusterStatus.RUNNING.getCode())));
        dto.setRemark(param.getRemark());
        flinkClusterInstanceService.insert(dto);
    }

    private ClusterClient createYarnSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkDeployConfigPath = loadDeployConfig(flinkClusterConfigDTO.getDeployConfigFileId(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, flinkDeployConfigPath);
        ClusterClient<ApplicationId> clusterClient = SessionClient.create(DeploymentTarget.YARN_SESSION, configuration);
        FileUtils.deleteDirectory(workspace.toFile());
        return clusterClient;
    }

    private ClusterClient createKubernetesSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkDeployConfigPath = loadDeployConfig(flinkClusterConfigDTO.getDeployConfigFileId(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, flinkDeployConfigPath);
        ClusterClient<String> clusterClient = SessionClient.create(DeploymentTarget.NATIVE_KUBERNETES_SESSION, configuration);
        FileUtils.deleteDirectory(workspace.toFile());
        return clusterClient;
    }

    private ClusterClient createExistingSessionCluster(FlinkClusterConfigDTO flinkClusterConfigDTO) throws Exception {
        final Path workspace = getWorkspace();
        final Path flinkDeployConfigPath = loadDeployConfig(flinkClusterConfigDTO.getDeployConfigFileId(), workspace);
        final Configuration configuration = buildConfiguration(flinkClusterConfigDTO, flinkDeployConfigPath);
        // 落库

        FileUtils.deleteDirectory(workspace.toFile());
        return null;
    }

    /**
     * hadoop conf
     * kubeconfig
     * flink-conf.yaml
     *
     * @see ConfigType
     */
    private Configuration buildConfiguration(FlinkClusterConfigDTO flinkClusterConfigDTO, Path flinkDeployConfigPath) throws IOException {
        final FlinkDeployConfigFileDTO flinkDeployConfigFileDTO = flinkDeployConfigFileService.selectOne(flinkClusterConfigDTO.getDeployConfigFileId());
        Configuration dynamicProperties;
        if (CollectionUtils.isEmpty(flinkClusterConfigDTO.getConfigOptions())) {
            dynamicProperties = new Configuration();
        } else {
            dynamicProperties = Configuration.fromMap(flinkClusterConfigDTO.getConfigOptions());
        }

        if (flinkDeployConfigFileDTO.getConfigType().getValue().equals(String.valueOf(ConfigType.FLINK_CONF.getCode()))) {
            final List<Path> childs = Files.list(flinkDeployConfigPath).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(childs)) {
                return dynamicProperties;
            }
            final Path flinkConf = childs.get(0);
            return GlobalConfiguration.loadConfiguration(flinkConf.toAbsolutePath().toString(), dynamicProperties);
        }

        if (flinkDeployConfigFileDTO.getConfigType().getValue().equals(String.valueOf(ConfigType.HADOOP_CONF.getCode()))) {
            dynamicProperties.set(CoreOptions.FLINK_HADOOP_CONF_DIR, flinkDeployConfigPath.toAbsolutePath().toString());
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            dynamicProperties.set(TaskManagerOptions.NUM_TASK_SLOTS, 2);
            return dynamicProperties;
        }

        if (flinkDeployConfigFileDTO.getConfigType().getValue().equals(String.valueOf(ConfigType.KUBECONFIG.getCode()))) {
            final List<Path> childs = Files.list(flinkDeployConfigPath).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(childs)) {
                return dynamicProperties;
            }
            final Path kubeConfigFile = childs.get(0);
            dynamicProperties.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, kubeConfigFile.toAbsolutePath().toString());
            dynamicProperties.setLong(JobManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            dynamicProperties.setLong(TaskManagerOptions.TOTAL_PROCESS_MEMORY.key(), MemorySize.ofMebiBytes(2048).getBytes());
            dynamicProperties.set(TaskManagerOptions.NUM_TASK_SLOTS, 2);
            return dynamicProperties;
        }
        return dynamicProperties;
    }


    private Path getWorkspace() throws IOException {
        return TempFileUtil.createTempDir();
    }

    private Path loadFlinkRelease(Long flinkReleaseId, Path workspace) throws IOException {
        final FlinkReleaseDTO releaseDTO = flinkReleaseService.selectOne(flinkReleaseId);
        final Path tempFile = TempFileUtil.createTempFile(workspace, releaseDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
            flinkReleaseService.download(flinkReleaseId, outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        return Files.list(untarDir).collect(Collectors.toList()).get(0);
    }

    private Path loadDeployConfig(Long flinkDeployConfigFileId, Path workspace) throws IOException {
        final FlinkDeployConfigFileDTO flinkDeployConfigFileDTO = flinkDeployConfigFileService.selectOne(flinkDeployConfigFileId);
        final List<FileStatusVO> fileStatusVOS = flinkDeployConfigFileService.listDeployConfigFile(flinkDeployConfigFileId);
        final Path tempDir = TempFileUtil.createTempDir(workspace, flinkDeployConfigFileDTO.getName());
        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            final Path deployConfigFile = tempDir.resolve(fileStatusVO.getName());
            Files.createFile(deployConfigFile, TempFileUtil.attributes);
            try (final OutputStream outputStream = Files.newOutputStream(deployConfigFile, StandardOpenOption.WRITE)) {
                flinkDeployConfigFileService.downloadDeployConfigFile(flinkDeployConfigFileId, fileStatusVO.getName(), outputStream);
            }
        }
        return tempDir;
    }


}
