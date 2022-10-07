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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceClusterCredentialMapper;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.convert.ClusterCredentialConvert;
import cn.sliew.scaleph.resource.service.convert.FileStatusVOConvert;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.vo.CacheKey;
import cn.sliew.scaleph.resource.service.vo.CacheResource;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import cn.sliew.scaleph.resource.service.vo.Resource;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.List;

import static cn.sliew.scaleph.common.exception.Rethrower.checkArgument;

@Slf4j
@Service
public class ClusterCredentialServiceImpl implements ClusterCredentialService {

    private Cache<CacheKey, Path> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5L))
            .removalListener((RemovalListener<CacheKey, Path>) (cacheKey, path, removalCause) -> {
                try {
                    TempFileUtil.deleteDir(path);
                } catch (IOException e) {
                    log.error("clear cluster credential temp file cache error! cacheKey: {}, path: {}",
                            JacksonUtil.toJsonString(cacheKey), path, e);
                }
            })
            .build();

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceClusterCredentialMapper resourceClusterCredentialMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CLUSTER_CREDENTIAL;
    }

    @Override
    public Page<ClusterCredentialDTO> list(ResourceListParam param) {
        ClusterCredentialListParam clusterCredentialListParam = ClusterCredentialConvert.INSTANCE.convert(param);
        return list(clusterCredentialListParam);
    }

    @Override
    public ClusterCredentialDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Resource<Path> obtain(Long id) throws Exception {
        final ClusterCredentialDTO clusterCredentialDTO = getRaw(id);
        final List<FileStatusVO> fileStatusVOS = listCredentialFile(id);
        final Path value = TempFileUtil.createTempDir(clusterCredentialDTO.getName());

        for (FileStatusVO fileStatusVO : fileStatusVOS) {
            final Path deployConfigFile = TempFileUtil.createTempFile(value, fileStatusVO.getName());
            try (final OutputStream outputStream = Files.newOutputStream(deployConfigFile, StandardOpenOption.WRITE)) {
                downloadCredentialFile(id, fileStatusVO.getName(), outputStream);
            }
        }
        final CacheKey key = new CacheKey(getResourceType(), id);
        cache.put(key, value);
        return new CacheResource<>(cache, key);
    }

    @Override
    public Page<ClusterCredentialDTO> list(ClusterCredentialListParam param) {
        final Page<ResourceClusterCredential> page = resourceClusterCredentialMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceClusterCredential.class)
                        .eq(param.getConfigType() != null, ResourceClusterCredential::getConfigType, param.getConfigType())
                        .like(StringUtils.hasText(param.getName()), ResourceClusterCredential::getName, param.getName()));

        Page<ClusterCredentialDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<ClusterCredentialDTO> dtoList = ClusterCredentialConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public ClusterCredentialDTO selectOne(Serializable id) {
        final ResourceClusterCredential record = resourceClusterCredentialMapper.selectById(id);
        if (record == null) {
            throw new IllegalStateException("flink deploy config not exists for id: " + id);
        }
        return ClusterCredentialConvert.INSTANCE.toDto(record);
    }

    @Override
    public void insert(ClusterCredentialDTO dto) {
        final ResourceClusterCredential record = ClusterCredentialConvert.INSTANCE.toDo(dto);
        resourceClusterCredentialMapper.insert(record);
    }

    @Override
    public int update(ClusterCredentialDTO dto) {
        final ResourceClusterCredential record = ClusterCredentialConvert.INSTANCE.toDo(dto);
        return resourceClusterCredentialMapper.updateById(record);
    }

    @Override
    public int deleteById(Serializable id) {
        try {
            final ClusterCredentialDTO flinkDeployConfigFileDTO = selectOne(id);
            final String rootPath = getCredentialFileRootPath() + "/" + flinkDeployConfigFileDTO.getName();
            if (fileSystemService.exists(rootPath)) {
                final List<FileStatus> fileStatuses = fileSystemService.listStatus(rootPath);
                for (FileStatus fileStatus : fileStatuses) {
                    deleteCredentialFile((Long) id, fileStatus.getPath().getName());
                }
            }
            return resourceClusterCredentialMapper.deleteById(id);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return -1;
        }
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
        return ids.size();
    }

    @Override
    public List<FileStatusVO> listCredentialFile(Long id) throws IOException {
        final List<FileStatus> fileStatuses = fileSystemService.listStatus(getCredentialFileRootPath() + "/" + id);
        return FileStatusVOConvert.INSTANCE.toVO(fileStatuses);
    }

    @Override
    public void uploadCredentialFile(Long id, MultipartFile[] files) throws IOException {
        checkArgument(files != null && files.length > 0, () -> "upload config file must not be empty");
        for (MultipartFile file : files) {
            try (final InputStream inputStream = file.getInputStream()) {
                final String flinkDeployConfigFilePath = getCredentialFilePath(id, file.getOriginalFilename());
                fileSystemService.upload(inputStream, flinkDeployConfigFilePath);
            }
        }
    }

    @Override
    public void downloadCredentialFile(Long id, String fileName, OutputStream outputStream) throws IOException {
        String path = getCredentialFilePath(id, fileName);
        try (final InputStream inputStream = fileSystemService.get(path)) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
    }

    @Override
    public void deleteCredentialFile(Long id, String fileName) throws IOException {
        String path = getCredentialFilePath(id, fileName);
        fileSystemService.delete(path);
    }

    @Override
    public void deleteCredentialFiles(Long id, List<String> fileNames) throws IOException {
        for (String fileName : fileNames) {
            deleteCredentialFile(id, fileName);
        }
    }

    private String getCredentialFilePath(Long id, String fileName) {
        return String.format("%s/%d/%s", getCredentialFileRootPath(), id, fileName);
    }

    private String getCredentialFileRootPath() {
        return "cluster/credential/file";
    }
}
