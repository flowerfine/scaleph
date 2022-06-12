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

import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.flink.service.FlinkReleaseService;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkReleaseDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseLoadParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkReleaseUploadParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlinkReleaseServiceImpl implements FlinkReleaseService, InitializingBean {

    private OkHttpClient httpClient;

    @Autowired
    private FileSystemService fileSystemService;

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
    public List<FlinkReleaseDTO> listRelease() throws IOException {
        List<FlinkReleaseDTO> result = new ArrayList<>();
        List<String> versions = fileSystemService.list(getFlinkReleaseRootPath());
        for (String version : versions) {
            final List<String> names = fileSystemService.list(getFlinkReleaseRootPath() + "/" + version);
            for (String name : names) {
                FlinkReleaseDTO releaseDTO = new FlinkReleaseDTO();
                releaseDTO.setVersion(version);
                releaseDTO.setName(name);
                result.add(releaseDTO);
            }
        }
        return result.stream().sorted().collect(Collectors.toList());
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
                final InputStream inputStream = response.body().byteStream();
                String filePath = getFlinkReleasePath(release.getVersion(), release.getName());
                fileSystemService.upload(inputStream, filePath);
                future.complete(true);
            }
        };
        httpClient.newCall(request).enqueue(callback);
        return future;
    }

    @Override
    public void upload(FlinkReleaseUploadParam param, InputStream inputStream) throws IOException {
        String filePath = getFlinkReleasePath(param.getVersion(), param.getName());
        fileSystemService.upload(inputStream, filePath);
    }

    @Override
    public String download(String version, OutputStream outputStream) throws IOException {
        final List<String> names = fileSystemService.list(getFlinkReleaseRootPath() + "/" + version);
        if (names.size() > 1 || names.size() == 0) {
            throw new IllegalStateException("flink release not exists for " + version);
        }
        final InputStream inputStream = fileSystemService.get(getFlinkReleasePath(version, names.get(0)));
        FileCopyUtils.copy(inputStream, outputStream);
        return names.get(0);
    }

    private String getFlinkReleasePath(String version, String fileName) {
        return String.format("%s/%s/%s", getFlinkReleaseRootPath(), version, fileName);
    }

    private String getFlinkReleaseRootPath() {
        return "release/flink";
    }
}
