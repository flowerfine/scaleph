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
import cn.sliew.scaleph.dao.entity.master.resource.ResourceKerberos;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceKerberosMapper;
import cn.sliew.scaleph.resource.service.KerberosService;
import cn.sliew.scaleph.resource.service.convert.KerberosConvert;
import cn.sliew.scaleph.resource.service.dto.KerberosDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.KerberosListParam;
import cn.sliew.scaleph.resource.service.param.KerberosUploadParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
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
import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class KerberosServiceImpl implements KerberosService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceKerberosMapper kerberosMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.KERBEROS;
    }

    @Override
    public Page<KerberosDTO> list(ResourceListParam param) {
        try {
            KerberosListParam kerberosListParam = KerberosConvert.INSTANCE.convert(param);
            return list(kerberosListParam);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public KerberosDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Page<KerberosDTO> list(KerberosListParam param) throws IOException {
        final Page<ResourceKerberos> page = kerberosMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceKerberos.class)
                        .like(StringUtils.hasText(param.getName()), ResourceKerberos::getName, param.getName())
                        .like(StringUtils.hasText(param.getFileName()), ResourceKerberos::getFileName, param.getFileName()));
        Page<KerberosDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<KerberosDTO> dtoList = KerberosConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public KerberosDTO selectOne(Long id) {
        final ResourceKerberos record = kerberosMapper.selectById(id);
        checkState(record != null, () -> "kerberos not exists for id: " + id);
        return KerberosConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(KerberosUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getKerberosPath(param.getName(), fileName);
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ResourceKerberos record = new ResourceKerberos();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        kerberosMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final KerberosDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    @Override
    public int deleteBatch(List<Long> ids) throws IOException {
        for (Long id : ids) {
            delete(id);
        }
        return ids.size();
    }

    @Override
    public void delete(Long id) throws IOException {
        final KerberosDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        kerberosMapper.deleteById(id);
    }

    private String getKerberosPath(String name, String fileName) {
        return String.format("%s/%s/%s", getKerberosRootPath(), name, fileName);
    }

    private String getKerberosRootPath() {
        return "kerberos";
    }
}
