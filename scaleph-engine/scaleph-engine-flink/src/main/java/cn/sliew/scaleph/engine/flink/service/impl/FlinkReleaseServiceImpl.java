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

import cn.sliew.scaleph.dao.mapper.master.flink.FlinkReleaseMapper;
import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.flink.service.FlinkReleaseService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkReleaseConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseLoadParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseUploadParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Slf4j
@Service
public class FlinkReleaseServiceImpl implements FlinkReleaseService, InitializingBean {

    private OkHttpClient httpClient;

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private FlinkReleaseMapper flinkReleaseMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(3L))
                .readTimeout(Duration.ofHours(1L))
                .writeTimeout(Duration.ofHours(1L))
                .callTimeout(Duration.ofHours(1L))
                .build();
    }

    @Override
    public Page<FlinkReleaseDTO> list(FlinkReleaseListParam param) throws IOException {
        final Page<cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease> page = flinkReleaseMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease.class)
                        .eq(StringUtils.hasText(param.getVersion()), cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease::getVersion, param.getVersion())
                        .like(StringUtils.hasText(param.getFileName()), cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease::getFileName, param.getFileName()));
        Page<FlinkReleaseDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkReleaseDTO> dtoList = FlinkReleaseConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkReleaseDTO selectOne(Long id) {
        final cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease record = flinkReleaseMapper.selectById(id);
        checkState(record != null, () -> "flink release not exists for id: " + id);
        return FlinkReleaseConvert.INSTANCE.toDto(record);
    }

    @Override
    public void delete(Long id) throws IOException {
        final FlinkReleaseDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        flinkReleaseMapper.deleteById(id);
    }

    @Override
    public CompletableFuture<Boolean> load(FlinkReleaseLoadParam param) throws IOException {
        final FlinkRelease release = FlinkRelease.version(param.getVersion());
        Request request = new Request.Builder()
                .url(release.getUrl())
                .build();
        CompletableFuture<Boolean> future = new CompletableFuture();
        final Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("download flink release from {} error!", release.getUrl(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (final InputStream inputStream = response.body().byteStream()) {
                    String filePath = getFlinkReleasePath(release.getVersion());
                    fileSystemService.upload(inputStream, filePath);
                    cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease record = new cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease();
                    record.setVersion(release.getVersion());
                    record.setFileName(release.getName());
                    record.setPath(filePath);
                    flinkReleaseMapper.insert(record);
                    future.complete(true);
                }
            }
        };
        httpClient.newCall(request).enqueue(callback);
        return future;
    }

    @Override
    public void upload(FlinkReleaseUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getFlinkReleasePath(param.getVersion());
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease record = new cn.sliew.scaleph.dao.entity.master.flink.FlinkRelease();
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

    private String getFlinkReleasePath(String version) {
        return String.format("%s/%s/%s", getFlinkReleaseRootPath(), version, RandomStringUtils.randomAlphabetic(8));
    }

    private String getFlinkReleaseRootPath() {
        return "release/flink";
    }
}
