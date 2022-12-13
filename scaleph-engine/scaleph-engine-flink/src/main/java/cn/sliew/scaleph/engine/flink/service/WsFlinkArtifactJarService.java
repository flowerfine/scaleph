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
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public interface WsFlinkArtifactJarService {

    Page<WsFlinkArtifactJarDTO> list(WsFlinkArtifactJarParam param);

    WsFlinkArtifactJarDTO selectOne(Long id);

    int deleteOne(Long id) throws ScalephException;

    void upload(WsFlinkArtifactJarDTO param, MultipartFile file) throws IOException;

    int update(WsFlinkArtifactJarDTO params);

    String download(Long id, OutputStream outputStream) throws IOException;
}
