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

import cn.sliew.scaleph.engine.flink.service.dto.FlinkDeployConfigFileDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileUpdateParam;
import cn.sliew.scaleph.engine.flink.service.vo.FileStatusVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface FlinkDeployConfigFileService {

    Page<FlinkDeployConfigFileDTO> list(FlinkDeployConfigFileListParam param);

    FlinkDeployConfigFileDTO selectOne(Serializable id);

    void insert(FlinkDeployConfigFileDTO dto);

    int update(FlinkDeployConfigFileUpdateParam param);

    int deleteById(Serializable id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    List<FileStatusVO> listDeployConfigFile(Long id) throws IOException;

    void uploadDeployConfigFile(Long id, MultipartFile[] files) throws IOException;

    void downloadDeployConfigFile(Long id, String fileName, OutputStream outputStream) throws IOException;

    void deleteDeployConfigFile(Long id, String fileName) throws IOException;
}
