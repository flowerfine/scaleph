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
import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.SeatunnelRelease;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelStorageService;
import cn.sliew.scaleph.storage.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * https://docs.ververica.com/platform_operations/blob_storage.html#id14
 */
@Slf4j
@Service
public class SeatunnelStorageServiceImpl implements SeatunnelStorageService {

    @Autowired
    private FileSystemService fileSystemService;

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
        return null;
    }

    private String getFlinkReleasePath(FlinkRelease flinkRelease) {
        // /{root}/releases/namespaces/default/flink/{fullVersion}/
        return String.format("releases/namespaces/default/flink/%s/",
                flinkRelease.getVersion());
    }

    @Override
    public Path loadFlinkRelease(FlinkRelease flinkRelease) throws IOException {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> downloadSeatunnelRelease(SeatunnelRelease seatunnelRelease) {
        return null;
    }

    private String getSeatunnelReleasePath(SeatunnelRelease seatunnelRelease) {
        // /{root}/releases/namespaces/default/seatunnel/{version}/
        return String.format("releases/namespaces/default/seatunnel/%s/",
                seatunnelRelease.getVersion());
    }

    @Override
    public Path loadSeatunnelRelease(SeatunnelRelease seatunnelRelease) throws IOException {
        return null;
    }

    @Override
    public void uploadSeatunnelConfigFile(DiJobDTO diJobDTO, String configJson) throws IOException {

    }

    private String getSeatunnelConfigFile(DiJobDTO diJobDTO) {
        return String.format("seatunnel-config/namespaces/default/jobs/%s/%s/configs/",
                diJobDTO.getProjectId(), diJobDTO.getId());
    }

    @Override
    public Path loadSeatunnelConfigFile(DiJobDTO diJobDTO) throws IOException {
        return null;
    }
}
