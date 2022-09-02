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

package cn.sliew.scaleph.resource.service.impl;

import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceSeaTunnelRelease;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceSeaTunnelReleaseMapper;
import cn.sliew.scaleph.resource.service.SeaTunnelReleaseService;
import cn.sliew.scaleph.resource.service.convert.SeaTunnelReleaseConvert;
import cn.sliew.scaleph.resource.service.dto.SeaTunnelReleaseDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseListParam;
import cn.sliew.scaleph.resource.service.param.SeaTunnelReleaseUploadParam;
import cn.sliew.scaleph.resource.service.vo.ResourceVO;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class SeaTunnelReleaseServiceImpl implements SeaTunnelReleaseService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceSeaTunnelReleaseMapper releaseSeaTunnelMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.SEATUNNEL_RELEASE;
    }

    @Override
    public Page<ResourceVO> list(ResourceListParam param) {
        try {
            SeaTunnelReleaseListParam seaTunnelReleaseListParam = SeaTunnelReleaseConvert.INSTANCE.convert(param);
            Page<SeaTunnelReleaseDTO> page = list(seaTunnelReleaseListParam);
            Page<ResourceVO> result =
                    new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
            List<ResourceVO> dtoList = SeaTunnelReleaseConvert.INSTANCE.convert(page.getRecords());
            result.setRecords(dtoList);
            return result;
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public SeaTunnelReleaseDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Page<SeaTunnelReleaseDTO> list(SeaTunnelReleaseListParam param) throws IOException {
        final Page<ResourceSeaTunnelRelease> page = releaseSeaTunnelMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceSeaTunnelRelease.class)
                        .eq(StringUtils.hasText(param.getVersion()), ResourceSeaTunnelRelease::getVersion, param.getVersion())
                        .like(StringUtils.hasText(param.getFileName()), ResourceSeaTunnelRelease::getFileName, param.getFileName()));
        Page<SeaTunnelReleaseDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<SeaTunnelReleaseDTO> dtoList = SeaTunnelReleaseConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public SeaTunnelReleaseDTO selectOne(Long id) {
        final ResourceSeaTunnelRelease record = releaseSeaTunnelMapper.selectById(id);
        checkState(record != null, () -> "release seatunnel not exists for id: " + id);
        return SeaTunnelReleaseConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(SeaTunnelReleaseUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getReleaseSeaTunnelPath(param.getVersion(), fileName);
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ResourceSeaTunnelRelease record = new ResourceSeaTunnelRelease();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        releaseSeaTunnelMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final SeaTunnelReleaseDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    @Override
    public int deleteBatch(List<Long> ids) throws IOException {
        for (Serializable id : ids) {
            delete((Long) id);
        }
        return ids.size();
    }

    @Override
    public void delete(Long id) throws IOException {
        final SeaTunnelReleaseDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        releaseSeaTunnelMapper.deleteById(id);
    }

    private String getReleaseSeaTunnelPath(String version, String fileName) {
        return String.format("%s/%s/%s", getReleaseSeaTunnelRootPath(), version, fileName);
    }

    private String getReleaseSeaTunnelRootPath() {
        return "release/seatunnel";
    }
}
