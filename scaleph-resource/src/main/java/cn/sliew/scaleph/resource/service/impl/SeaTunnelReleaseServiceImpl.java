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

package cn.sliew.scaleph.resource.service.impl;

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceSeaTunnelRelease;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceSeaTunnelReleaseMapper;
import cn.sliew.scaleph.resource.service.SeaTunnelReleaseService;
import cn.sliew.scaleph.resource.service.convert.FileStatusVOConvert;
import cn.sliew.scaleph.resource.service.convert.SeaTunnelReleaseConvert;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelConnectorUploadParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseUploadParam;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.storage.service.RemoteService;
import cn.sliew.scaleph.system.util.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class SeaTunnelReleaseServiceImpl implements SeaTunnelReleaseService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private RemoteService remoteService;
    @Autowired
    private ResourceSeaTunnelReleaseMapper releaseSeaTunnelMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.SEATUNNEL_RELEASE;
    }

    @Override
    public Page<SeaTunnelReleaseDTO> list(ResourceListParam param) {
        try {
            SeaTunnelReleaseListParam seaTunnelReleaseListParam = SeaTunnelReleaseConvert.INSTANCE.convert(param);
            return list(seaTunnelReleaseListParam);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public SeaTunnelReleaseDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Page<SeaTunnelReleaseDTO> list(SeaTunnelReleaseListParam param) throws IOException {
        final Page<ResourceSeaTunnelRelease> page = releaseSeaTunnelMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceSeaTunnelRelease.class)
                        .eq(param.getVersion() != null, ResourceSeaTunnelRelease::getVersion, param.getVersion())
                        .like(StringUtils.hasText(param.getFileName()), ResourceSeaTunnelRelease::getFileName, param.getFileName()));
        Page<SeaTunnelReleaseDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<SeaTunnelReleaseDTO> dtoList = SeaTunnelReleaseConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public SeaTunnelReleaseDTO selectOne(Long id) {
        final ResourceSeaTunnelRelease record = releaseSeaTunnelMapper.selectById(id);
        checkState(record != null, () -> "release seatunnel not exists for id: " + id);
        return SeaTunnelReleaseConvert.INSTANCE.toDto(record);
    }

    @Override
    public SeaTunnelReleaseDTO selectByVersion(SeaTunnelVersion version) {
        LambdaQueryWrapper<ResourceSeaTunnelRelease> queryWrapper = Wrappers.lambdaQuery(ResourceSeaTunnelRelease.class)
                .eq(ResourceSeaTunnelRelease::getVersion, version);
        ResourceSeaTunnelRelease record = releaseSeaTunnelMapper.selectOne(queryWrapper);
        checkState(record != null, () -> "release seatunnel not exists for version: " + version.getValue());
        return SeaTunnelReleaseConvert.INSTANCE.toDto(record);
    }

    @Override
    public List<FileStatusVO> listConnectors(Long id) throws IOException {
        SeaTunnelReleaseDTO dto = selectOne(id);
        List<FileStatus> fileStatuses = fileSystemService.listStatus(getConnectorsPath(dto.getVersion().getValue()));
        return FileStatusVOConvert.INSTANCE.toVO(fileStatuses);
    }

    @Override
    public void upload(SeaTunnelReleaseUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getReleasePath(param.getVersion().getValue(), fileName);
        try (InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ResourceSeaTunnelRelease record = new ResourceSeaTunnelRelease();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        releaseSeaTunnelMapper.insert(record);
    }

    @Override
    public void uploadConnector(SeaTunnelConnectorUploadParam param, MultipartFile file) throws IOException {
        SeaTunnelReleaseDTO dto = selectOne(param.getId());
        String version = dto.getVersion().getValue();
        String connector = SeaTunnelPluginMapping.of(param.getPluginName()).getPluginJarPrefix();
        String connectorPath = getSeaTunnelConnectorPath(version, connector);
        try (InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, connectorPath);
        }
    }

    @Override
    public void fetchConnectors(Long id) throws IOException {
        SeaTunnelReleaseDTO dto = selectOne(id);
        Path workspace = SystemUtil.getRandomWorkspace();
        try {
            Path file = FileUtil.createFile(workspace, dto.getFileName());
            try (OutputStream outputStream = FileUtil.getOutputStream(file)) {
                download(id, outputStream);
            }

            Path seatunnelPath = FileUtil.listFiles(TarUtil.untar(file)).get(0);
            Path pluginMapping = SeaTunnelReleaseUtil.getPluginMapping(seatunnelPath);
            Properties pluginMappingProperties = PropertiesLoaderUtils.loadProperties(new PathResource(pluginMapping));

            Set<String> connectors = new HashSet<>();
            pluginMappingProperties.forEach((plugin, connector) -> {
                if (SeaTunnelReleaseUtil.isV2Connectors((String) plugin)) {
                    connectors.add((String) connector);
                }
            });
            doFetch(dto.getVersion().getValue(), connectors);
        } finally {
            FileUtil.deleteDir(workspace);
        }
    }

    private void doFetch(String version, Set<String> connectors) {
        connectors.forEach(connector -> {
            String connectorUrl = SeaTunnelReleaseUtil.seatunnelConnectorUrl(SeaTunnelReleaseUtil.STARTER_REPO_URL, version, connector);
            String connectorFile = getSeaTunnelConnectorPath(version, connector);
            try {
                remoteService.fetch(connectorUrl, connectorFile);
            } catch (IOException e) {
                Rethrower.throwAs(e);
            }
        });
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final SeaTunnelReleaseDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    @Override
    public String downloadConnector(Long id, String connector, OutputStream outputStream) throws IOException {
        final SeaTunnelReleaseDTO dto = selectOne(id);
        final String fileName = String.format("%s/%s", getConnectorsPath(dto.getVersion().getValue()), connector);
        try (InputStream inputStream = fileSystemService.get(fileName)) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return connector;
    }

    @Override
    public int deleteBatch(List<Long> ids) throws IOException {
        for (Serializable id : ids) {
            delete((Long) id);
        }
        return ids.size();
    }

    @Override
    public void delete(Long id) throws IOException {
        final SeaTunnelReleaseDTO dto = selectOne(id);
        String releaseVersionPath = getVersionPath(dto.getVersion().getValue());
        fileSystemService.delete(releaseVersionPath);
        releaseSeaTunnelMapper.deleteById(id);
    }

    private String getVersionPath(String version) {
        return String.format("%s/%s", getRootPath(), version);
    }

    private String getReleasePath(String version, String fileName) {
        return String.format("%s/%s", getVersionPath(version), fileName);
    }

    private String getConnectorsPath(String version) {
        return String.format("%s/connectors/seatunnel", getVersionPath(version));
    }

    private String getSeaTunnelConnectorPath(String version, String connector) {
        String connectorJar = SeaTunnelReleaseUtil.convertToJar(version, connector);
        return String.format("%s/%s", getConnectorsPath(version), connectorJar);
    }

    private String getRootPath() {
        return "release/seatunnel";
    }
}
