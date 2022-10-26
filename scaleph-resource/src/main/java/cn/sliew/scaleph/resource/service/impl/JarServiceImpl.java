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
import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.common.nio.TarUtil;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.dao.entity.master.resource.ResourceJar;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceJarMapper;
import cn.sliew.scaleph.resource.service.JarService;
import cn.sliew.scaleph.resource.service.convert.JarConvert;
import cn.sliew.scaleph.resource.service.dto.JarDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.JarListParam;
import cn.sliew.scaleph.resource.service.param.JarUploadParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.util.SystemUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class JarServiceImpl implements JarService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceJarMapper jarMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.JAR;
    }

    @Override
    public Page<JarDTO> list(ResourceListParam param) {
        try {
            JarListParam jarListParam = JarConvert.INSTANCE.convert(param);
            return list(jarListParam);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public JarDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Page<JarDTO> list(JarListParam param) throws IOException {
        final Page<ResourceJar> page = jarMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceJar.class)
                        .eq(StringUtils.hasText(param.getGroup()), ResourceJar::getGroup, param.getGroup())
                        .like(StringUtils.hasText(param.getFileName()), ResourceJar::getFileName, param.getFileName()));
        Page<JarDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<JarDTO> dtoList = JarConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public JarDTO selectOne(Long id) {
        final ResourceJar record = jarMapper.selectById(id);
        checkState(record != null, () -> "jar not exists for id: " + id);
        return JarConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(JarUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = getJarPath(param.getGroup(), fileName);
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, filePath);
        }
        ResourceJar record = new ResourceJar();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(filePath);
        jarMapper.insert(record);
    }



    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final JarDTO dto = selectOne(id);
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
        final JarDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        jarMapper.deleteById(id);
    }

    private String getJarPath(String group, String fileName) {
        return String.format("%s/%s/%s", getJarRootPath(), group, fileName);
    }



    private String getJarRootPath() {
        return "jar";
    }
}
