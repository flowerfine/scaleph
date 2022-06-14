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

import cn.sliew.scaleph.dao.entity.master.flink.FlinkDeployConfigFile;
import cn.sliew.scaleph.dao.mapper.master.flink.FlinkDeployConfigFileMapper;
import cn.sliew.scaleph.engine.flink.service.FlinkDeployConfigFileService;
import cn.sliew.scaleph.engine.flink.service.convert.FlinkDeployConfigFileConvert;
import cn.sliew.scaleph.engine.flink.service.dto.FlinkDeployConfigFileDTO;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileListParam;
import cn.sliew.scaleph.engine.flink.service.param.FlinkDeployConfigFileUploadParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                        .eq(param.getConfigType() != null, FlinkDeployConfigFile::getConfigType, param.getConfigType()));

        Page<FlinkDeployConfigFileDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<FlinkDeployConfigFileDTO> dtoList = FlinkDeployConfigFileConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public void upload(FlinkDeployConfigFileUploadParam param, MultipartFile[] files) throws IOException {
        checkArgument(files != null && files.length > 0, () -> "upload config file must not be empty");
        for (MultipartFile file : files) {
            final String flinkDeployConfigFilePath = getFlinkDeployConfigFilePath(param.getName(), file.getOriginalFilename());
            fileSystemService.upload(file.getInputStream(), flinkDeployConfigFilePath);
        }
        FlinkDeployConfigFile record = new FlinkDeployConfigFile();
        BeanUtils.copyProperties(param, record);
        record.setPath(getFlinkDeployConfigFilePath(record.getName(), ""));
        flinkDeployConfigFileMapper.insert(record);
    }

    private String getFlinkDeployConfigFilePath(String name, String fileName) {
        return String.format("%s/%s/%s", getFlinkDeployConfigFileRootPath(), name, fileName);
    }

    private String getFlinkDeployConfigFileRootPath() {
        return "release/flink/deploy";
    }
}
