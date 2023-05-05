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

package cn.sliew.scaleph.engine.flink.service;

import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.engine.flink.resource.JarArtifact;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarHistoryParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarUpdateParam;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarUploadParam;
import cn.sliew.scaleph.system.snowflake.exception.UidGenerateException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface WsFlinkArtifactJarService {

    Page<WsFlinkArtifactJarDTO> list(WsFlinkArtifactJarParam param);

    Page<WsFlinkArtifactJarDTO> listByArtifact(WsFlinkArtifactJarHistoryParam param);

    List<WsFlinkArtifactJarDTO> listAllByArtifact(Long artifactId);

    WsFlinkArtifactJarDTO selectOne(Long id);

    JarArtifact asYaml(Long id);

    int deleteOne(Long id) throws ScalephException, IOException;

    int deleteAll(Long flinkArtifactId) throws IOException;

    void upload(WsFlinkArtifactJarUploadParam param, MultipartFile file) throws IOException, UidGenerateException;

    int update(WsFlinkArtifactJarUpdateParam param, MultipartFile file) throws UidGenerateException, IOException;

    String download(Long id, OutputStream outputStream) throws IOException;

}
