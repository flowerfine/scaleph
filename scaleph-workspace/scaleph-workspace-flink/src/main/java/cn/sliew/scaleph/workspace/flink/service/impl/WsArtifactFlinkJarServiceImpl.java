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
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkJar;
import cn.sliew.scaleph.dao.mapper.master.ws.WsArtifactFlinkJarMapper;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.workspace.flink.resource.JarArtifact;
import cn.sliew.scaleph.workspace.flink.resource.JarArtifactConverter;
import cn.sliew.scaleph.workspace.flink.service.WsArtifactFlinkJarService;
import cn.sliew.scaleph.workspace.flink.service.convert.WsArtifactFlinkJarConvert;
import cn.sliew.scaleph.workspace.flink.service.dto.WsArtifactFlinkJarDTO;
import cn.sliew.scaleph.workspace.flink.service.param.*;
import cn.sliew.scaleph.workspace.project.service.WsArtifactService;
import cn.sliew.scaleph.workspace.project.service.dto.WsArtifactDTO;
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

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsArtifactFlinkJarServiceImpl implements WsArtifactFlinkJarService {

    @Autowired
    private WsArtifactService wsArtifactService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private WsArtifactFlinkJarMapper wsArtifactFlinkJarMapper;

    @Override
    public Page<WsArtifactFlinkJarDTO> list(WsArtifactFlinkJarListParam param) {
        Page<WsArtifactFlinkJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsArtifactFlinkJar> jarPage = wsArtifactFlinkJarMapper.list(page, param.getProjectId(), param.getName(), param.getFlinkVersion());
        Page<WsArtifactFlinkJarDTO> result =
                new Page<>(jarPage.getCurrent(), jarPage.getSize(), jarPage.getTotal());
        result.setRecords(WsArtifactFlinkJarConvert.INSTANCE.toDto(jarPage.getRecords()));
        return result;
    }

    @Override
    public Page<WsArtifactFlinkJarDTO> listByArtifact(WsArtifactFlinkJarArtifactParam param) {
        Page<WsArtifactFlinkJar> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsArtifactFlinkJar> queryWrapper = Wrappers.lambdaQuery(WsArtifactFlinkJar.class)
                .eq(WsArtifactFlinkJar::getArtifactId, param.getArtifactId())
                .orderByDesc(WsArtifactFlinkJar::getId);
        Page<WsArtifactFlinkJar> wsFlinkArtifactJarPage = wsArtifactFlinkJarMapper.selectPage(page, queryWrapper);
        Page<WsArtifactFlinkJarDTO> result =
                new Page<>(wsFlinkArtifactJarPage.getCurrent(), wsFlinkArtifactJarPage.getSize(), wsFlinkArtifactJarPage.getTotal());
        result.setRecords(WsArtifactFlinkJarConvert.INSTANCE.toDto(wsFlinkArtifactJarPage.getRecords()));
        return result;
    }

    @Override
    public List<WsArtifactFlinkJarDTO> listAll(WsArtifactFlinkJarSelectListParam param) {
        List<WsArtifactFlinkJar> wsFlinkArtifactJars = wsArtifactFlinkJarMapper.listAll(param.getProjectId(), param.getName());
        return WsArtifactFlinkJarConvert.INSTANCE.toDto(wsFlinkArtifactJars);
    }

    @Override
    public WsArtifactFlinkJarDTO selectOne(Long id) {
        WsArtifactFlinkJar record = wsArtifactFlinkJarMapper.selectOne(id);
        return WsArtifactFlinkJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsArtifactFlinkJarDTO selectCurrent(Long artifactId) {
        WsArtifactFlinkJar record = wsArtifactFlinkJarMapper.selectCurrent(artifactId);
        return WsArtifactFlinkJarConvert.INSTANCE.toDto(record);
    }

    @Override
    public JarArtifact asYaml(Long id) {
        WsArtifactFlinkJarDTO dto = selectOne(id);
        return JarArtifactConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public void upload(WsArtifactFlinkJarUploadParam param, MultipartFile file) throws IOException {
        Path path = null;
        try (InputStream inputStream = file.getInputStream()) {
            path = fileSystemService.upload(inputStream, getFlinkArtifactPath(param.getName(), file.getOriginalFilename()));
        }
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setProjectId(param.getProjectId());
        artifactDTO.setType(FlinkJobType.JAR);
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        artifactDTO = wsArtifactService.insert(artifactDTO);
        WsArtifactFlinkJar record = new WsArtifactFlinkJar();
        record.setArtifactId(artifactDTO.getId());
        record.setFlinkVersion(param.getFlinkVersion());
        record.setEntryClass(param.getEntryClass());
        record.setFileName(file.getOriginalFilename());
        record.setPath(path.toString());
        record.setJarParams(param.getJarParams());
        record.setCurrent(YesOrNo.YES);
        wsArtifactFlinkJarMapper.insert(record);
    }

    /**
     * 如果是重新上传了一个 jar，则生成一条新的记录
     * 如果未上传，则仅更新配置信息即可
     */
    @Override
    public int update(WsArtifactFlinkJarUpdateParam param, MultipartFile file) throws IOException {
        WsArtifactFlinkJarDTO wsFlinkArtifactJarDTO = selectOne(param.getId());
        WsArtifactDTO artifactDTO = new WsArtifactDTO();
        artifactDTO.setId(wsFlinkArtifactJarDTO.getArtifact().getId());
        artifactDTO.setName(param.getName());
        artifactDTO.setRemark(param.getRemark());
        wsArtifactService.update(artifactDTO);

        int upsert = 0;
        WsArtifactFlinkJar record = new WsArtifactFlinkJar();
        if (file != null) {
            Path path = null;
            try (InputStream inputStream = file.getInputStream()) {
                path = fileSystemService.upload(inputStream, getFlinkArtifactPath(param.getName(), file.getOriginalFilename()));
            }
            WsArtifactFlinkJarDTO oldArtifactJarDTO = selectCurrent(artifactDTO.getId());
            WsArtifactFlinkJar oldRecord = new WsArtifactFlinkJar();
            oldRecord.setId(oldArtifactJarDTO.getId());
            oldRecord.setCurrent(YesOrNo.NO);
            wsArtifactFlinkJarMapper.updateById(oldRecord);

            record.setArtifactId(artifactDTO.getId());
            record.setFlinkVersion(param.getFlinkVersion());
            record.setEntryClass(param.getEntryClass());
            record.setFileName(file.getOriginalFilename());
            record.setPath(path.toString());
            record.setJarParams(param.getJarParams());
            record.setCurrent(YesOrNo.YES);
            upsert = wsArtifactFlinkJarMapper.insert(record);
        } else {
            record.setId(param.getId());
            record.setArtifactId(artifactDTO.getId());
            record.setFlinkVersion(param.getFlinkVersion());
            record.setEntryClass(param.getEntryClass());
            record.setJarParams(param.getJarParams());
            upsert = wsArtifactFlinkJarMapper.updateById(record);
        }
        return upsert;
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        WsArtifactFlinkJarDTO dto = selectOne(id);
        try (InputStream inputStream = fileSystemService.get(dto.getPath())) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
        return dto.getFileName();
    }


    @Override
    public int deleteOne(Long id) throws IOException {
        WsArtifactFlinkJarDTO wsFlinkArtifactJarDTO = selectOne(id);
        checkState(wsFlinkArtifactJarDTO.getCurrent() != YesOrNo.YES, () -> "Unsupport delete current jar");

        fileSystemService.delete(wsFlinkArtifactJarDTO.getPath());
        return wsArtifactFlinkJarMapper.deleteById(id);
    }

    @Override
    public int deleteArtifact(Long artifactId) throws IOException {
        List<WsArtifactFlinkJarDTO> wsFlinkArtifactJarDTOS = listAllByArtifact(artifactId);
        for (WsArtifactFlinkJarDTO jar : wsFlinkArtifactJarDTOS) {
            fileSystemService.delete(jar.getPath());
            wsArtifactFlinkJarMapper.deleteById(jar.getId());
        }
        return wsArtifactService.deleteById(artifactId);
    }

    private String getFlinkArtifactPath(String name, String fileName) {
        return String.format("%s/%s/%s", "artifact/flink/jar", name, fileName);
    }

    private List<WsArtifactFlinkJarDTO> listAllByArtifact(Long artifactId) {
        List<WsArtifactFlinkJar> list = wsArtifactFlinkJarMapper.selectList(
                Wrappers.lambdaQuery(WsArtifactFlinkJar.class)
                        .eq(WsArtifactFlinkJar::getArtifactId, artifactId)
                        .orderByDesc(WsArtifactFlinkJar::getId)
        );
        return WsArtifactFlinkJarConvert.INSTANCE.toDto(list);
    }
}
