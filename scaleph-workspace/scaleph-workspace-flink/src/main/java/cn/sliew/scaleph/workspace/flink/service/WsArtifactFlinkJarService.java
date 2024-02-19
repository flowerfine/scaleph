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

package cn.sliew.scaleph.workspace.flink.service;

import cn.sliew.scaleph.workspace.flink.resource.JarArtifact;
import cn.sliew.scaleph.workspace.flink.service.dto.WsArtifactFlinkJarDTO;
import cn.sliew.scaleph.workspace.flink.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface WsArtifactFlinkJarService {

    Page<WsArtifactFlinkJarDTO> list(WsArtifactFlinkJarListParam param);

    Page<WsArtifactFlinkJarDTO> listByArtifact(WsArtifactFlinkJarArtifactParam param);

    List<WsArtifactFlinkJarDTO> listAll(WsArtifactFlinkJarSelectListParam param);

    WsArtifactFlinkJarDTO selectOne(Long id);

    WsArtifactFlinkJarDTO selectCurrent(Long artifactId);

    JarArtifact asYaml(Long id);

    void upload(WsArtifactFlinkJarUploadParam param, MultipartFile file) throws IOException;

    int update(WsArtifactFlinkJarUpdateParam param, MultipartFile file) throws IOException;

    String download(Long id, OutputStream outputStream) throws IOException;

    int deleteOne(Long id) throws IOException;

    int deleteArtifact(Long artifactId) throws IOException;

}
