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

package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.storage.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.fs.FSDataOutputStream;
import org.apache.flink.core.fs.FileStatus;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Resource
    private FileSystem fs;

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public boolean exists(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.exists(path);
    }

    @Override
    public List<String> list(String directory) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), directory);
        if (fs.exists(path) == false) {
            return Collections.emptyList();
        }
        final FileStatus[] fileStatuses = fs.listStatus(path);
        return Arrays.stream(fileStatuses)
                .map(fileStatus -> fileStatus.getPath().getName())
                .collect(Collectors.toList());
    }

    @Override
    public InputStream get(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.open(path);
    }

    @Override
    public void upload(InputStream inputStream, String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        if (fs.exists(path.getParent()) == false) {
            fs.mkdirs(path.getParent());
        }
        try (final FSDataOutputStream outputStream = fs.create(path, FileSystem.WriteMode.NO_OVERWRITE)) {
            IOUtils.copyBytes(inputStream, outputStream);
        }
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.delete(path, true);
    }

    @Override
    public Long getFileSize(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        final FileStatus fileStatus = fs.getFileStatus(path);
        return fileStatus.getBlockSize();
    }
}
