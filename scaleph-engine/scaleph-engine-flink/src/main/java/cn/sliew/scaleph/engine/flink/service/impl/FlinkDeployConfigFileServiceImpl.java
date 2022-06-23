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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.dao.entity.master.flink.FlinkDeployConfigFile;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkDeployConfigFileMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkDeployConfigFileService;
import cn.sliew.scaleph.engine.flink.service.convert.FileStatusVOConvert;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkDeployConfigFileConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkDeployConfigFileDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileListParam;
import cn.sliew.scaleph.engine.flink.service.vo.FileStatusVO;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.fs.FileStatus;
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

import static cn.sliew.scaleph.common.exception.Rethrower.checkArgument;

@Slf4j
@Service
public class FlinkDeployConfigFileServiceImpl implements FlinkDeployConfigFileService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private FlinkDeployConfigFileMapper flinkDeployConfigFileMapper;

    @Override
    public Page<FlinkDeployConfigFileDTO> list(FlinkDeployConfigFileListParam param) {
        final Page<FlinkDeployConfigFile> page = flinkDeployConfigFileMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(FlinkDeployConfigFile.class)
                        .eq(param.getConfigType() != null, FlinkDeployConfigFile::getConfigType, param.getConfigType())
                        .like(StringUtils.hasText(param.getName()), FlinkDeployConfigFile::getName, param.getName()));

        Page<FlinkDeployConfigFileDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkDeployConfigFileDTO> dtoList = FlinkDeployConfigFileConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public FlinkDeployConfigFileDTO selectOne(Serializable id) {
        final FlinkDeployConfigFile record = flinkDeployConfigFileMapper.selectById(id);
        if (record == null) {
            throw new IllegalStateException("flink deploy config not exists for id: " + id);
        }
        return FlinkDeployConfigFileConvert.INSTANCE.toDto(record);
    }

    @Override
    public void insert(FlinkDeployConfigFileDTO dto) {
        final FlinkDeployConfigFile record = FlinkDeployConfigFileConvert.INSTANCE.toDo(dto);
        flinkDeployConfigFileMapper.insert(record);
    }

    @Override
    public int update(FlinkDeployConfigFileDTO dto) {
        final FlinkDeployConfigFile record = FlinkDeployConfigFileConvert.INSTANCE.toDo(dto);
        return flinkDeployConfigFileMapper.updateById(record);
    }

    @Override
    public int deleteById(Serializable id) {
        try {
            final FlinkDeployConfigFileDTO flinkDeployConfigFileDTO = selectOne(id);
            final String rootPath = getFlinkDeployConfigFileRootPath() + "/" + flinkDeployConfigFileDTO.getName();
            if (fileSystemService.exists(rootPath)) {
                final List<FileStatus> fileStatuses = fileSystemService.listStatus(rootPath);
                for (FileStatus fileStatus : fileStatuses) {
                    deleteDeployConfigFile((Long) id, fileStatus.getPath().getName());
                }
            }
            return flinkDeployConfigFileMapper.deleteById(id);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return -1;
        }
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
        return ids.size();
    }

    @Override
    public List<FileStatusVO> listDeployConfigFile(Long id) throws IOException {
        final FlinkDeployConfigFileDTO flinkDeployConfigFileDTO = selectOne(id);
        final List<FileStatus> fileStatuses = fileSystemService.listStatus(getFlinkDeployConfigFileRootPath() + "/" + flinkDeployConfigFileDTO.getName());
        return FileStatusVOConvert.INSTANCE.toVO(fileStatuses);
    }

    @Override
    public void uploadDeployConfigFile(Long id, MultipartFile[] files) throws IOException {
        checkArgument(files != null && files.length > 0, () -> "upload config file must not be empty");
        final FlinkDeployConfigFileDTO flinkDeployConfigFileDTO = selectOne(id);
        for (MultipartFile file : files) {
            try (final InputStream inputStream = file.getInputStream()) {
                final String flinkDeployConfigFilePath = getFlinkDeployConfigFilePath(flinkDeployConfigFileDTO.getName(), file.getOriginalFilename());
                fileSystemService.upload(inputStream, flinkDeployConfigFilePath);
            }
        }
    }

    @Override
    public void downloadDeployConfigFile(Long id, String fileName, OutputStream outputStream) throws IOException {
        final FlinkDeployConfigFileDTO record = selectOne(id);
        String path = getFlinkDeployConfigFilePath(record.getName(), fileName);
        try (final InputStream inputStream = fileSystemService.get(path)) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
    }

    @Override
    public void deleteDeployConfigFile(Long id, String fileName) throws IOException {
        final FlinkDeployConfigFileDTO record = selectOne(id);
        String path = getFlinkDeployConfigFilePath(record.getName(), fileName);
        fileSystemService.delete(path);
    }

    private String getFlinkDeployConfigFilePath(String name, String fileName) {
        return String.format("%s/%s/%s", getFlinkDeployConfigFileRootPath(), name, fileName);
    }

    private String getFlinkDeployConfigFileRootPath() {
        return "deploy/flink/config";
    }
}
