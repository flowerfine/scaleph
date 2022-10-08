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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.nio.TempFileUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceFlinkRelease;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceFlinkReleaseMapper;
import cn.sliew.scaleph.resource.service.FlinkReleaseService;
import cn.sliew.scaleph.resource.service.convert.FlinkReleaseConvert;
import cn.sliew.scaleph.resource.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.FlinkReleaseListParam;
import cn.sliew.scaleph.resource.service.param.FlinkReleaseUploadParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.vo.CacheKey;
import cn.sliew.scaleph.resource.service.vo.CacheResource;
import cn.sliew.scaleph.resource.service.vo.Resource;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class FlinkReleaseServiceImpl implements FlinkReleaseService {

    private Cache<CacheKey, Path> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(5L))
            .removalListener((RemovalListener<CacheKey, Path>) (cacheKey, path, removalCause) -> {
                try {
                    TempFileUtil.deleteDir(path);
                } catch (IOException e) {
                    log.error("clear flink release temp file cache error! cacheKey: {}, path: {}",
                            JacksonUtil.toJsonString(cacheKey), path, e);
                }
            })
            .build();
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceFlinkReleaseMapper flinkReleaseMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.FLINK_RELEASE;
    }

    @Override
    public Page<FlinkReleaseDTO> list(ResourceListParam param) {
        try {
            FlinkReleaseListParam flinkReleaseListParam = FlinkReleaseConvert.INSTANCE.convert(param);
            return list(flinkReleaseListParam);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public FlinkReleaseDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Resource obtain(Long id) throws Exception {
        final FlinkReleaseDTO flinkReleaseDTO = getRaw(id);
        final Path tempFile = TempFileUtil.createTempFile(flinkReleaseDTO.getFileName());
        try (final OutputStream outputStream = Files.newOutputStream(tempFile, StandardOpenOption.WRITE)) {
            download(id, outputStream);
        }
        final Path untarDir = TarUtil.untar(tempFile);
        final Path value = Files.list(untarDir).collect(Collectors.toList()).get(0);
        final CacheKey key = new CacheKey(getResourceType(), id);
        cache.put(key, value);
        return new CacheResource(cache, key);
    }

    @Override
    public Page<FlinkReleaseDTO> list(FlinkReleaseListParam param) throws IOException {
        final Page<ResourceFlinkRelease> page = flinkReleaseMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceFlinkRelease.class)
                        .eq(param.getVersion() != null, ResourceFlinkRelease::getVersion, param.getVersion())
                        .like(StringUtils.hasText(param.getFileName()), ResourceFlinkRelease::getFileName, param.getFileName()));
        Page<FlinkReleaseDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkReleaseDTO> dtoList = FlinkReleaseConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkReleaseDTO selectOne(Long id) {
        final ResourceFlinkRelease record = flinkReleaseMapper.selectById(id);
        checkState(record != null, () -> "flink release not exists for id: " + id);
        return FlinkReleaseConvert.INSTANCE.toDto(record);
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
        final FlinkReleaseDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        flinkReleaseMapper.deleteById(id);
    }

    @Override
    public void upload(FlinkReleaseUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getFlinkReleasePath(param.getVersion().getValue(), fileName);
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ResourceFlinkRelease record = new ResourceFlinkRelease();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        flinkReleaseMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final FlinkReleaseDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    private String getFlinkReleasePath(String version, String fileName) {
        return String.format("%s/%s/%s", getFlinkReleaseRootPath(), version, fileName);
    }

    private String getFlinkReleaseRootPath() {
        return "release/flink";
    }
}
