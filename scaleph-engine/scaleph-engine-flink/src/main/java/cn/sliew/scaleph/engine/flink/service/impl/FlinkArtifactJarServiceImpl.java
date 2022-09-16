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

package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.scaleph.dao.entity.master.flink.FlinkArtifactJar;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkArtifactJarMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkArtifactJarConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarUploadParam;
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
public class FlinkArtifactJarServiceImpl implements FlinkArtifactJarService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private FlinkArtifactJarMapper flinkArtifactJarMapper;

    @Override
    public Page<FlinkArtifactJarDTO> list(FlinkArtifactJarListParam param) {

        final Page<FlinkArtifactJar> page = flinkArtifactJarMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkArtifactJar.class)
                        .eq(param.getFlinkArtifactId() != null, FlinkArtifactJar::getFlinkArtifactId, param.getFlinkArtifactId())
                        .orderByDesc(FlinkArtifactJar::getVersion, FlinkArtifactJar::getId));
        Page<FlinkArtifactJarDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkArtifactJarDTO> dtoList = FlinkArtifactJarConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkArtifactJarDTO selectOne(Long id) {
        final FlinkArtifactJar record = flinkArtifactJarMapper.selectById(id);
        checkState(record != null, () -> "flink artifact jar not exists for id: " + id);
        return FlinkArtifactJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(FlinkArtifactJarUploadParam param, MultipartFile file) throws IOException {
        String path = getFlinkArtifactPath(file.getOriginalFilename());
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, path);
        }
        FlinkArtifactJar record = new FlinkArtifactJar();
        BeanUtils.copyProperties(param, record);
        if (StringUtils.hasText(param.getVersion())) {
            // 校验

        } else {
            // 查询最大的版本号
        }
        record.setFileName(file.getOriginalFilename());
        record.setPath(path);
        flinkArtifactJarMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        final FlinkArtifactJarDTO dto = selectOne(id);
        try (final InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    private String getFlinkArtifactPath(String fileName) {
        return String.format("%s/%s", getFlinkArtifactJarRootPath(), fileName);
    }

    private String getFlinkArtifactJarRootPath() {
        return "job/artifact/jar";
    }
}
