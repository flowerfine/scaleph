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

package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.seatunnel.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.SeatunnelRelease;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelStorageService;
import cn.sliew.scaleph.storage.service.BlobService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * https://docs.ververica.com/platform_operations/blob_storage.html#id14
 */
@Slf4j
@Service
public class SeatunnelStorageServiceImpl implements SeatunnelStorageService {

    private final FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(
            new HashSet<>(Arrays.asList(
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_EXECUTE,
                    PosixFilePermission.GROUP_READ,
                    PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.GROUP_EXECUTE)));

    private OkHttpClient httpClient;

    @Autowired
    private BlobService blobService;

    public SeatunnelStorageServiceImpl() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(3L))
                .readTimeout(Duration.ofHours(1L))
                .writeTimeout(Duration.ofHours(1L))
                .callTimeout(Duration.ofHours(1L))
                .build();
    }

    @Override
    public String getRootDir() {
        final String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir.endsWith("/")) {
            return tmpdir.substring(0, tmpdir.length() - 1);
        }
        return tmpdir;
    }

    @Override
    public String getHaDir(DiJobDTO diJobDTO) {
        // /{root}/flink-checkpoints/namespaces/default/jobs/{projectId}/{jobId}/ha/
        return String.format("%s/flink-checkpoints/namespaces/default/jobs/%s/%s/ha/",
                getRootDir(), diJobDTO.getProjectId(), diJobDTO.getId());
    }

    @Override
    public String getCheckpointDir(DiJobDTO diJobDTO) {
        // /{root}/flink-checkpoints/namespaces/default/jobs/{projectId}/{jobId}/checkpoints/
        return String.format("%s/flink-checkpoints/namespaces/default/jobs/%s/%s/checkpoints/",
                getRootDir(), diJobDTO.getProjectId(), diJobDTO.getId());
    }

    @Override
    public String getSavepointDir(DiJobDTO diJobDTO) {
        // /{root}/flink-savepoints/namespaces/default/deployments/{projectId}/{jobId}/
        return String.format("%s/flink-savepoints/namespaces/default/deployments/%s/%s/",
                getRootDir(), diJobDTO.getProjectId(), diJobDTO.getId());
    }

    @Override
    public CompletableFuture<Boolean> downloadFlinkRelease(FlinkRelease flinkRelease) {
        Request request = new Request.Builder()
                .url(flinkRelease.getReleaseUrl())
                .build();
        CompletableFuture<Boolean> future = new CompletableFuture();
        final Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("download flink release {} from {} error!",
                        flinkRelease.getFullVersion(), flinkRelease.getReleaseUrl(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final InputStream inputStream = response.body().byteStream();
                String filePath = getFlinkReleasePath(flinkRelease);
                String fileName = flinkRelease.getReleaseName();
                blobService.upload(inputStream, filePath + fileName);
                future.complete(true);
            }
        };
        httpClient.newCall(request).enqueue(callback);
        return future;
    }

    private String getFlinkReleasePath(FlinkRelease flinkRelease) {
        // /{root}/releases/namespaces/default/flink/{fullVersion}/
        return String.format("releases/namespaces/default/flink/%s/",
                flinkRelease.getFullVersion());
    }

    @Override
    public Path loadFlinkRelease(FlinkRelease flinkRelease) throws IOException {
        String filePath = getFlinkReleasePath(flinkRelease);
        String fileName = flinkRelease.getReleaseName();
        final InputStream inputStream = blobService.get(filePath + fileName);
        final Path tempFile = createTempFile(createTempDir(), fileName);
        Files.copy(inputStream, tempFile, StandardCopyOption.ATOMIC_MOVE);
        return tempFile;
    }

    @Override
    public CompletableFuture<Boolean> downloadSeatunnelRelease(SeatunnelRelease seatunnelRelease) {
        Request request = new Request.Builder()
                .url(seatunnelRelease.getReleaseUrl())
                .build();
        CompletableFuture<Boolean> future = new CompletableFuture();
        final Callback callback = new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                log.error("download seatunnel release {} from {} error!",
                        seatunnelRelease.getVersion(), seatunnelRelease.getReleaseUrl(), e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final InputStream inputStream = response.body().byteStream();
                String filePath = getSeatunnelReleasePath(seatunnelRelease);
                String fileName = seatunnelRelease.getReleaseName();
                blobService.upload(inputStream, filePath + fileName);
                future.complete(true);
            }
        };
        httpClient.newCall(request).enqueue(callback);
        return future;
    }

    private String getSeatunnelReleasePath(SeatunnelRelease seatunnelRelease) {
        // /{root}/releases/namespaces/default/seatunnel/{version}/
        return String.format("releases/namespaces/default/seatunnel/%s/",
                seatunnelRelease.getVersion());
    }

    @Override
    public Path loadSeatunnelRelease(SeatunnelRelease seatunnelRelease) throws IOException {
        String filePath = getSeatunnelReleasePath(seatunnelRelease);
        String fileName = seatunnelRelease.getReleaseName();
        final InputStream inputStream = blobService.get(filePath + fileName);
        final Path tempFile = createTempFile(createTempDir(), fileName);
        Files.copy(inputStream, tempFile, StandardCopyOption.ATOMIC_MOVE);
        return tempFile;
    }

    @Override
    public void uploadSeatunnelConfigFile(DiJobDTO diJobDTO, String configJson) throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(configJson.getBytes(StandardCharsets.UTF_8));
        blobService.upload(inputStream, getSeatunnelConfigFile(diJobDTO));
    }

    private String getSeatunnelConfigFile(DiJobDTO diJobDTO) {
        return String.format("seatunnel-config/namespaces/default/jobs/%s/%s/configs/",
                diJobDTO.getProjectId(), diJobDTO.getId());
    }

    @Override
    public Path loadSeatunnelConfigFile(DiJobDTO diJobDTO) throws IOException {
        final String configFile = getSeatunnelConfigFile(diJobDTO);
        final Path tempFile = createTempFile(createTempDir(), getSeatunnelConfigFile(diJobDTO));
        final InputStream inputStream = blobService.get(configFile);
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    private Path createTempDir() throws IOException {
        return Files.createTempDirectory(null, attributes);
    }

    private Path createTempFile(Path tempDir, String fileName) throws IOException {
        return Files.createTempFile(tempDir, fileName, null, attributes);
    }
}
