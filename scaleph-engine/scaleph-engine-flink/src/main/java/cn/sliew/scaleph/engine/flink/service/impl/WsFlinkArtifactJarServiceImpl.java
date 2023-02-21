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

import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.BeanUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkArtifactJarMapper;
import cn.sliew.scaleph.engine.flink.resource.JarArtifact;
import cn.sliew.scaleph.engine.flink.resource.JarArtifactConverter;
import cn.sliew.scaleph.engine.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.engine.flink.service.convert.WsFlinkArtifactJarConvert;
import cn.sliew.scaleph.engine.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.WsFlinkArtifactJarParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.system.util.I18nUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Service
public class WsFlinkArtifactJarServiceImpl implements WsFlinkArtifactJarService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private WsFlinkArtifactJarMapper flinkArtifactJarMapper;

    @Override
    public Page<WsFlinkArtifactJarDTO> list(WsFlinkArtifactJarParam param) {
        Page<WsFlinkArtifactJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        WsFlinkArtifactJar wsFlinkArtifactJar = BeanUtil.copy(param, new WsFlinkArtifactJar());
        Page<WsFlinkArtifactJar> jarPage = flinkArtifactJarMapper.list(page, wsFlinkArtifactJar);
        Page<WsFlinkArtifactJarDTO> result =
                new Page<>(jarPage.getCurrent(), jarPage.getSize(), jarPage.getTotal());
        result.setRecords(WsFlinkArtifactJarConvert.INSTANCE.toDto(jarPage.getRecords()));
        return result;
    }

    @Override
    public List<WsFlinkArtifactJarDTO> listByArtifact(Long artifactId) {
        List<WsFlinkArtifactJar> list = flinkArtifactJarMapper.selectList(
                Wrappers.lambdaQuery(WsFlinkArtifactJar.class)
                        .eq(WsFlinkArtifactJar::getFlinkArtifactId, artifactId)
        );
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(list);
    }

    @Override
    public WsFlinkArtifactJarDTO selectOne(Long id) {
        WsFlinkArtifactJar record = flinkArtifactJarMapper.selectOne(id);
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public JarArtifact asYaml(Long id) {
        WsFlinkArtifactJarDTO dto = selectOne(id);
        return JarArtifactConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public void upload(WsFlinkArtifactJarDTO param, MultipartFile file) throws IOException {
        String path = getFlinkArtifactPath(param.getVersion(), file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            fileSystemService.upload(inputStream, path);
        }
        WsFlinkArtifactJar record = WsFlinkArtifactJarConvert.INSTANCE.toDo(param);
        record.setFileName(file.getOriginalFilename());
        record.setPath(path);
        flinkArtifactJarMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        WsFlinkArtifactJarDTO dto = selectOne(id);
        try (InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    @Override
    public int update(WsFlinkArtifactJarDTO params) {
        WsFlinkArtifactJar jar = WsFlinkArtifactJarConvert.INSTANCE.toDo(params);
        return flinkArtifactJarMapper.updateById(jar);
    }

    @Override
    public int deleteOne(Long id) throws ScalephException {
        WsFlinkArtifactJar jar = flinkArtifactJarMapper.isUsed(id);
        if (jar != null) {
            throw new ScalephException(I18nUtil.get("response.error.job.artifact.jar"));
        }
        return flinkArtifactJarMapper.deleteById(id);
    }

    private String getFlinkArtifactPath(String version, String fileName) {
        return String.format("%s/%s/%s", getFlinkArtifactJarRootPath(), version, fileName);
    }

    private String getFlinkArtifactJarRootPath() {
        return "job/artifact/jar";
    }
}
