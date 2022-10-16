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

import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkArtifactJar;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkArtifactJarVO;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkArtifactJarMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkArtifactJarVOConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkArtifactJarUploadParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.zafarkhaja.semver.Version;
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
        Page<FlinkArtifactJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        final FlinkArtifactJar flinkArtifactJar = BeanUtil.copy(param, new FlinkArtifactJar());
        final Page<FlinkArtifactJarVO> flinkArtifactJarVOPage = flinkArtifactJarMapper.list(page, flinkArtifactJar);
        Page<FlinkArtifactJarDTO> result =
                new Page<>(flinkArtifactJarVOPage.getCurrent(), flinkArtifactJarVOPage.getSize(), flinkArtifactJarVOPage.getTotal());
        List<FlinkArtifactJarDTO> dtoList = FlinkArtifactJarVOConvert.INSTANCE.toDto(flinkArtifactJarVOPage.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkArtifactJarDTO selectOne(Long id) {
        final FlinkArtifactJarVO record = flinkArtifactJarMapper.getById(id);
        checkState(record != null, () -> "flink artifact jar not exists for id: " + id);
        return FlinkArtifactJarVOConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(FlinkArtifactJarUploadParam param, MultipartFile file) throws IOException {
        //todo Version.valueOf(param.getVersion()) will throw exception when version is not a special case
        Version version;
        if (StringUtils.hasText(param.getVersion())) {
            version = Version.valueOf(param.getVersion());
        } else {
            final String maxVersion = flinkArtifactJarMapper.findMaxVersion(param.getFlinkArtifactId());
            if (StringUtils.hasText(maxVersion)) {
                version = Version.valueOf(maxVersion).incrementPatchVersion();
            } else {
                version = Version.forIntegers(1, 0, 0);
            }
        }
        String path = getFlinkArtifactPath(version.toString(), file.getOriginalFilename());
        try (final InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, path);
        }
        FlinkArtifactJar record = new FlinkArtifactJar();
        BeanUtils.copyProperties(param, record);
        record.setVersion(version.toString());
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

    private String getFlinkArtifactPath(String version, String fileName) {
        return String.format("%s/%s/%s", getFlinkArtifactJarRootPath(), version, fileName);
    }

    private String getFlinkArtifactJarRootPath() {
        return "job/artifact/jar";
    }
}
