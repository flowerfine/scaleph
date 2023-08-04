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

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import cn.sliew.scaleph.common.util.SystemUtil;
import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryParamsDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.exception.ScalephSqlGatewayNotFoundException;
import cn.sliew.scaleph.engine.sql.gateway.internal.ScalephCatalogManager;
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.JarService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.dto.JarDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WsFlinkSqlGatewayServiceImpl implements WsFlinkSqlGatewayService {

    /**
     * Store {@link ScalephCatalogManager}s in this map. </br>
     * In case multi {@link ScalephCatalogManager}s can be enabled in the future
     */
    private static final Map<String, ScalephCatalogManager> CATALOG_MANAGER_MAP =
            new ConcurrentHashMap<>();
    private final KubernetesService kubernetesService;
    private final ClusterCredentialService clusterCredentialService;
    private final JarService jarService;

    @Autowired
    public WsFlinkSqlGatewayServiceImpl(KubernetesService kubernetesService,
                                        ClusterCredentialService clusterCredentialService,
                                        JarService jarService) {
        this.kubernetesService = kubernetesService;
        this.clusterCredentialService = clusterCredentialService;
        this.jarService = jarService;
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @return
     */
    @Override
    public Optional<ScalephCatalogManager> getCatalogManager(String clusterId) {
        return Optional.ofNullable(CATALOG_MANAGER_MAP.get(clusterId));
    }

    /**
     * {@inheritDoc}
     *
     * @param kubeCredentialId Cluster credential id
     * @param clusterId        Flink K8S session cluster id
     * @return
     * @throws Exception
     */
    @Override
    public Optional<ScalephCatalogManager> createCatalogManager(Long kubeCredentialId, String clusterId) {
        try {
            ClusterCredentialDTO clusterCredential = clusterCredentialService.selectOne(kubeCredentialId);
            Path path = kubernetesService.downloadConfig(clusterCredential);
            Configuration configuration = GlobalConfiguration.loadConfiguration();
            configuration.set(KubernetesConfigOptions.CLUSTER_ID, clusterId);
            configuration.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, path.toString());
            if (StringUtils.hasText(clusterCredential.getContext())) {
                configuration.set(KubernetesConfigOptions.CONTEXT, clusterCredential.getContext());
            }
            configuration.set(DeploymentOptions.TARGET, "kubernetes-session");
            ScalephCatalogManager catalogManager = ScalephCatalogManager.create(configuration);
            CATALOG_MANAGER_MAP.put(clusterId, catalogManager);
            return Optional.of(catalogManager);
        } catch (Exception e) {
            log.error("Error create SqlGateway for session id: " + clusterId, e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @throws Exception
     */
    @Override
    public void destroyCatalogManager(String clusterId) {
        getCatalogManager(clusterId).ifPresent(catalogManager -> {
            try {
                catalogManager.close();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
            CATALOG_MANAGER_MAP.remove(clusterId);
        });
    }

    @Override
    public GatewayInfo getGatewayInfo(String clusterId) {
        return GatewayInfo.INSTANCE;
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @return
     */
    @Override
    public Set<CatalogInfo> getCatalogInfo(String clusterId, boolean includeSystemFunctions) {
        return getCatalogManager(clusterId)
                .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                .getCatalogInfo(includeSystemFunctions);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @param params    Sql query params
     * @return
     */
    @Override
    public String executeSql(String clusterId, WsFlinkSqlGatewayQueryParamsDTO params) {
        return getCatalogManager(clusterId)
                .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                .executeStatement(params.getSql(), params.getConfiguration());
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId         Flink K8S session cluster id
     * @param operationHandleId Operation handle id
     * @param token             token
     * @param maxRows           Max rows to fetch
     * @return
     */
    @Override
    public ResultSet fetchResults(String clusterId,
                                  String operationHandleId,
                                  Long token, int maxRows) {
        OperationHandle operationHandle = new OperationHandle(UUID.fromString(operationHandleId));
        ScalephCatalogManager catalogManager = getCatalogManager(clusterId).orElseThrow(ScalephSqlGatewayNotFoundException::new);
        ResultSet resultSet;
        if (token == null || token < 0) {
            resultSet = catalogManager.fetchResults(operationHandle, FetchOrientation.FETCH_NEXT, maxRows);
        } else {
            resultSet = catalogManager.fetchResults(operationHandle, token, maxRows);
        }
        return resultSet;
    }

    @Override
    public Boolean cancel(String clusterId, String operationHandleId) {
        try {
            getCatalogManager(clusterId)
                    .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                    .cancelOperation(new OperationHandle(UUID.fromString(operationHandleId)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> completeStatement(String clusterId, String statement, int position) throws Exception {
        return getCatalogManager(clusterId)
                .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                .completeStatement(statement, position);
    }

    @Override
    public Boolean addDependencies(String clusterId, List<Long> jarIdList) {
        try {
            List<URI> jars = jarIdList.stream().map(jarId -> {
                        JarDTO jarDTO = jarService.getRaw(jarId);
                        try {
                            Path localPath = SystemUtil.getLocalStorageDir().resolve("jars");
                            if (Files.notExists(localPath)) {
                                Files.createDirectories(localPath);
                            }
                            Path fileName = localPath.resolve(jarDTO.getFileName());
                            if (Files.notExists(fileName)) {
                                try (OutputStream os = Files.newOutputStream(fileName)) {
                                    jarService.download(jarId, os);
                                }
                            } else {
                                log.info("Jar file " + fileName + " already exists!");
                            }
                            return fileName;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).map(Path::toUri)
                    .collect(Collectors.toList());
            getCatalogManager(clusterId)
                    .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                    .addDependencies(jars);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean addCatalog(String clusterId, String catalogName, Map<String, String> options) {
        try {
            getCatalogManager(clusterId)
                    .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                    .addCatalog(catalogName, options);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean removeCatalog(String clusterId, String catalogName) {
        try {
            getCatalogManager(clusterId)
                    .orElseThrow(ScalephSqlGatewayNotFoundException::new)
                    .removeCatalog(catalogName);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
