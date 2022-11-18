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

import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.param.SeaTunnelConnectorUploadParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseUploadParam;
import cn.sliew.scaleph.resource.service.vo.FileStatusVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SeaTunnelReleaseService extends ResourceDescriptor<SeaTunnelReleaseDTO> {

    Page<SeaTunnelReleaseDTO> list(SeaTunnelReleaseListParam param) throws IOException;

    SeaTunnelReleaseDTO selectOne(Long id);

    SeaTunnelReleaseDTO selectByVersion(SeaTunnelVersion version);

    List<FileStatusVO> listConnectors(Long id) throws IOException;

    SeaTunnelReleaseDTO upload(SeaTunnelReleaseUploadParam param, MultipartFile file) throws IOException;

    void uploadConnector(SeaTunnelConnectorUploadParam param, MultipartFile file) throws IOException;

    /**
     * seatunnel release not contains connectors since 2.2.0-beta
     * instead provides a connector jar install shell script
     */
    void fetchConnectors(Long id) throws IOException;

    String download(Long id, OutputStream outputStream) throws IOException;

    String downloadConnector(Long id, String connector, OutputStream outputStream) throws IOException;

    int deleteBatch(List<Long> ids) throws IOException;

    void delete(Long id) throws IOException;
}
