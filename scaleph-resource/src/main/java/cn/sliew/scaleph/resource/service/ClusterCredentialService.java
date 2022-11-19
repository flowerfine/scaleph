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

package cn.sliew.scaleph.resource.service;

import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

public interface ClusterCredentialService extends ResourceDescriptor<ClusterCredentialDTO> {

    Page<ClusterCredentialDTO> list(ClusterCredentialListParam param);

    ClusterCredentialDTO selectOne(Long id);

    ClusterCredentialDTO insert(ClusterCredentialDTO dto);

    int update(ClusterCredentialDTO dto);

    int deleteById(Long id);

    int deleteBatch(List<Long> ids);

    List<FileStatusVO> listCredentialFile(Long id) throws IOException;

    void uploadCredentialFile(Long id, MultipartFile[] files) throws IOException;

    void downloadCredentialFile(Long id, String fileName, OutputStream outputStream) throws IOException;

    void deleteCredentialFile(Long id, String fileName) throws IOException;

    void deleteCredentialFiles(Long id, List<String> fileNames) throws IOException;
}
