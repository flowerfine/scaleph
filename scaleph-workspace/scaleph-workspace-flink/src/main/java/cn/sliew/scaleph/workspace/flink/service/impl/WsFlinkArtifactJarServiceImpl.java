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

package cn.sliew.scaleph.workspace.flink.service.impl;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkArtifactJarMapper;
import cn.sliew.scaleph.workspace.flink.resource.JarArtifact;
import cn.sliew.scaleph.workspace.flink.resource.JarArtifactConverter;
import cn.sliew.scaleph.workspace.flink.service.WsFlinkArtifactJarService;
import cn.sliew.scaleph.workspace.flink.service.convert.WsFlinkArtifactJarConvert;
import cn.sliew.scaleph.workspace.flink.service.dto.WsFlinkArtifactJarDTO;
import cn.sliew.scaleph.engine.flink.service.param.*;
import cn.sliew.scaleph.workspace.flink.service.param.*;
import cn.sliew.scaleph.workspace.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsFlinkArtifactDTO;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.hadoop.fs.Path;
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
    private WsFlinkArtifactService wsFlinkArtifactService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private WsFlinkArtifactJarMapper flinkArtifactJarMapper;

    @Override
    public Page<WsFlinkArtifactJarDTO> list(WsFlinkArtifactJarListParam param) {
        Page<WsFlinkArtifactJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsFlinkArtifactJar> jarPage = flinkArtifactJarMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsFlinkArtifactJarDTO> result =
                new Page<>(jarPage.getCurrent(), jarPage.getSize(), jarPage.getTotal());
        result.setRecords(WsFlinkArtifactJarConvert.INSTANCE.toDto(jarPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsFlinkArtifactJarDTO> listByArtifact(WsFlinkArtifactJarHistoryParam param) {
        Page<WsFlinkArtifactJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkArtifactJar> queryWrapper = Wrappers.lambdaQuery(WsFlinkArtifactJar.class)
                .eq(WsFlinkArtifactJar::getFlinkArtifactId, param.getFlinkArtifactId())
                .orderByDesc(WsFlinkArtifactJar::getId);
        Page<WsFlinkArtifactJar> wsFlinkArtifactJarPage = flinkArtifactJarMapper.selectPage(page, queryWrapper);
        Page<WsFlinkArtifactJarDTO> result =
                new Page<>(wsFlinkArtifactJarPage.getCurrent(), wsFlinkArtifactJarPage.getSize(), wsFlinkArtifactJarPage.getTotal());
        result.setRecords(WsFlinkArtifactJarConvert.INSTANCE.toDto(wsFlinkArtifactJarPage.getRecords()));
        return result;
    }

    @Override
    public List<WsFlinkArtifactJarDTO> listAll(WsFlinkArtifactJarSelectListParam param) {
        List<WsFlinkArtifactJar> wsFlinkArtifactJars = flinkArtifactJarMapper.listAll(param.getProjectId(), param.getName());
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(wsFlinkArtifactJars);
    }

    @Override
    public WsFlinkArtifactJarDTO selectOne(Long id) {
        WsFlinkArtifactJar record = flinkArtifactJarMapper.selectOne(id);
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsFlinkArtifactJarDTO selectCurrent(Long artifactId) {
        WsFlinkArtifactJar record = flinkArtifactJarMapper.selectCurrent(artifactId);
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public JarArtifact asYaml(Long id) {
        WsFlinkArtifactJarDTO dto = selectOne(id);
        return JarArtifactConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public int deleteOne(Long id) throws ScalephException, IOException {
        WsFlinkArtifactJar jar = flinkArtifactJarMapper.isUsed(id);
        if (jar != null) {
            throw new ScalephException(I18nUtil.get("response.error.job.artifact.jar"));
        }
        WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO = selectOne(id);
        if (wsFlinkArtifactJarDTO.getCurrent() == YesOrNo.YES) {
            throw new ScalephException("Unsupport delete current jar");
        }
        fileSystemService.delete(wsFlinkArtifactJarDTO.getPath());
        return flinkArtifactJarMapper.deleteById(id);
    }

    @Override
    public int deleteAll(Long flinkArtifactId) throws IOException, ScalephException {
        List<WsFlinkArtifactJarDTO> wsFlinkArtifactJarDTOS = listAllByArtifact(flinkArtifactId);
        for (WsFlinkArtifactJarDTO jar : wsFlinkArtifactJarDTOS) {
            fileSystemService.delete(jar.getPath());
            flinkArtifactJarMapper.deleteById(jar.getId());
        }
        return wsFlinkArtifactService.deleteById(flinkArtifactId);
    }

    @Override
    public void upload(WsFlinkArtifactJarUploadParam param, MultipartFile file) throws IOException {
        Path path = null;
        try (InputStream inputStream = file.getInputStream()) {
            path = fileSystemService.upload(inputStream, getFlinkArtifactPath(param.getName(), file.getOriginalFilename()));
        }
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setProjectId(param.getProjectId());
        flinkArtifact.setType(FlinkJobType.JAR);
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());
        flinkArtifact = wsFlinkArtifactService.insert(flinkArtifact);
        WsFlinkArtifactJar record = new WsFlinkArtifactJar();
        record.setFlinkArtifactId(flinkArtifact.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setEntryClass(param.getEntryClass());
        record.setFileName(file.getOriginalFilename());
        record.setPath(path.toString());
        record.setJarParams(param.getJarParams());
        record.setCurrent(YesOrNo.YES);
        flinkArtifactJarMapper.insert(record);
    }

    @Override
    public int update(WsFlinkArtifactJarUpdateParam param, MultipartFile file) throws IOException {
        WsFlinkArtifactJarDTO wsFlinkArtifactJarDTO = selectOne(param.getId());
        WsFlinkArtifactDTO flinkArtifact = new WsFlinkArtifactDTO();
        flinkArtifact.setId(wsFlinkArtifactJarDTO.getWsFlinkArtifact().getId());
        flinkArtifact.setName(param.getName());
        flinkArtifact.setRemark(param.getRemark());

        int upsert = 0;
        WsFlinkArtifactJar record = new WsFlinkArtifactJar();
        if (file != null) {
            Path path = null;
            try (InputStream inputStream = file.getInputStream()) {
                path = fileSystemService.upload(inputStream, getFlinkArtifactPath(param.getName(), file.getOriginalFilename()));
            }
            WsFlinkArtifactJarDTO oldArtifactJarDTO = selectCurrent(flinkArtifact.getId());
            WsFlinkArtifactJar oldRecord = new WsFlinkArtifactJar();
            oldRecord.setId(oldArtifactJarDTO.getId());
            oldRecord.setCurrent(YesOrNo.NO);
            flinkArtifactJarMapper.updateById(oldRecord);

            record.setFlinkArtifactId(flinkArtifact.getId());
            record.setFlinkVersion(param.getFlinkVersion());
            record.setEntryClass(param.getEntryClass());
            record.setFileName(file.getOriginalFilename());
            record.setPath(path.toString());
            record.setJarParams(param.getJarParams());
            record.setCurrent(YesOrNo.YES);
            upsert = flinkArtifactJarMapper.insert(record);
        } else {
            record.setId(param.getId());
            record.setFlinkArtifactId(flinkArtifact.getId());
            record.setFlinkVersion(param.getFlinkVersion());
            record.setEntryClass(param.getEntryClass());
            record.setJarParams(param.getJarParams());
            upsert = flinkArtifactJarMapper.updateById(record);
        }
        wsFlinkArtifactService.update(flinkArtifact);
        return upsert;
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        WsFlinkArtifactJarDTO dto = selectOne(id);
        try (InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }

    private String getFlinkArtifactPath(String name, String fileName) {
        return String.format("%s/%s/%s", getFlinkArtifactJarRootPath(), name, fileName);
    }

    private String getFlinkArtifactJarRootPath() {
        return "job/artifact/jar";
    }

    private List<WsFlinkArtifactJarDTO> listAllByArtifact(Long artifactId) {
        List<WsFlinkArtifactJar> list = flinkArtifactJarMapper.selectList(
                Wrappers.lambdaQuery(WsFlinkArtifactJar.class)
                        .eq(WsFlinkArtifactJar::getFlinkArtifactId, artifactId)
                        .orderByDesc(WsFlinkArtifactJar::getId)
        );
        return WsFlinkArtifactJarConvert.INSTANCE.toDto(list);
    }
}
