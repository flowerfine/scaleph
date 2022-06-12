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

package cn.sliew.scaleph.engine.seatunnel.service;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.engine.flink.FlinkRelease;
import cn.sliew.scaleph.engine.seatunnel.SeatunnelRelease;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * todo move flink from here
 */
public interface SeatunnelStorageService {

    String getRootDir();

    String getHaDir(DiJobDTO diJobDTO);

    String getCheckpointDir(DiJobDTO diJobDTO);

    String getSavepointDir(DiJobDTO diJobDTO);

    @Deprecated
    CompletableFuture<Boolean> downloadFlinkRelease(FlinkRelease flinkRelease);

    @Deprecated
    Path loadFlinkRelease(FlinkRelease flinkRelease) throws IOException;

    CompletableFuture<Boolean> downloadSeatunnelRelease(SeatunnelRelease seatunnelRelease);

    Path loadSeatunnelRelease(SeatunnelRelease seatunnelRelease) throws IOException;

    void uploadSeatunnelConfigFile(DiJobDTO diJobDTO, String configJson) throws IOException;

    Path loadSeatunnelConfigFile(DiJobDTO diJobDTO) throws IOException;

}
