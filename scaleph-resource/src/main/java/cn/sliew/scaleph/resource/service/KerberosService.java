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

import cn.sliew.scaleph.resource.service.dto.KerberosDTO;
import cn.sliew.scaleph.resource.service.param.KerberosListParam;
import cn.sliew.scaleph.resource.service.param.KerberosUploadParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface KerberosService extends ResourceDescriptor<KerberosDTO> {

    Page<KerberosDTO> list(KerberosListParam param) throws IOException;

    KerberosDTO selectOne(Long id);

    void upload(KerberosUploadParam param, MultipartFile file) throws IOException;

    String download(Long id, OutputStream outputStream) throws IOException;

    int deleteBatch(List<Long> ids) throws IOException;

    void delete(Long id) throws IOException;
}
