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

import cn.sliew.scaleph.dao.entity.master.resource.ResourceClusterCredential;
import cn.sliew.scaleph.dao.mapper.master.resource.ResourceClusterCredentialMapper;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.convert.ClusterCredentialConvert;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import cn.sliew.scaleph.resource.service.enums.ResourceType;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialListParam;
import cn.sliew.scaleph.resource.service.param.ClusterCredentialUploadParam;
import cn.sliew.scaleph.resource.service.param.ResourceListParam;
import cn.sliew.scaleph.storage.service.FileSystemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.hadoop.fs.Path;
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
import java.util.stream.Collectors;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class ClusterCredentialServiceImpl implements ClusterCredentialService {

    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private ResourceClusterCredentialMapper resourceClusterCredentialMapper;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.CLUSTER_CREDENTIAL;
    }

    @Override
    public Page<ClusterCredentialDTO> list(ResourceListParam param) {
        ClusterCredentialListParam clusterCredentialListParam = ClusterCredentialConvert.INSTANCE.convert(param);
        return list(clusterCredentialListParam);
    }

    @Override
    public ClusterCredentialDTO getRaw(Long id) {
        return selectOne(id);
    }

    @Override
    public Page<ClusterCredentialDTO> list(ClusterCredentialListParam param) {
        Page<ResourceClusterCredential> page = resourceClusterCredentialMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(ResourceClusterCredential.class)
                        .like(StringUtils.hasText(param.getName()), ResourceClusterCredential::getName, param.getName()));

        Page<ClusterCredentialDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<ClusterCredentialDTO> dtoList = ClusterCredentialConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<Long> listAll() {
        LambdaQueryWrapper<ResourceClusterCredential> queryWrapper = Wrappers.lambdaQuery(ResourceClusterCredential.class)
                .select(ResourceClusterCredential::getId);
        List<ResourceClusterCredential> records = resourceClusterCredentialMapper.selectList(queryWrapper);
        return records.stream().map(ResourceClusterCredential::getId).collect(Collectors.toList());
    }

    @Override
    public ClusterCredentialDTO selectOne(Long id) {
        ResourceClusterCredential record = resourceClusterCredentialMapper.selectById(id);
        checkState(record != null, () -> "cluster credential not exists for id: " + id);
        return ClusterCredentialConvert.INSTANCE.toDto(record);
    }

    @Override
    public void upload(ClusterCredentialUploadParam param, MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path path = null;
        try (InputStream inputStream = file.getInputStream()) {
            path = fileSystemService.upload(inputStream, getCredentialFilePath(param.getName(), fileName));
        }
        ResourceClusterCredential record = new ResourceClusterCredential();
        BeanUtils.copyProperties(param, record);
        record.setFileName(fileName);
        record.setPath(path.toString());
        resourceClusterCredentialMapper.insert(record);
    }

    @Override
    public String download(Long id, OutputStream outputStream) throws IOException {
        ClusterCredentialDTO dto = selectOne(id);
        try (InputStream inputStream = fileSystemService.get(dto.getPath())) {
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
        ClusterCredentialDTO dto = selectOne(id);
        fileSystemService.delete(dto.getPath());
        resourceClusterCredentialMapper.deleteById(id);
    }

    private String getCredentialFilePath(String name, String fileName) {
        return String.format("%s/%s/%s", getCredentialFileRootPath(), name, fileName);
    }

    private String getCredentialFileRootPath() {
        return "cluster/credential";
    }
}
