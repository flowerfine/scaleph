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

import cn.sliew.scaleph.config.storage.FileSystemType;
import cn.sliew.scaleph.storage.service.FileSystemService;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileSystemServiceImpl implements FileSystemService {

    private FileSystem fs;

    public FileSystemServiceImpl(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public boolean isDistributedFS() {
        return fs.getScheme().equals(FileSystemType.LOCAL.getSchema()) == false;
    }

    @Override
    public boolean exists(String path) throws IOException {
        return fs.exists(new Path(path));
    }

    @Override
    public List<String> list(String path) throws IOException {
        if (exists(path) == false) {
            return Collections.emptyList();
        }
        FileStatus[] fileStatuses = fs.listStatus(new Path(path));
        return Arrays.stream(fileStatuses)
                .map(fileStatus -> fileStatus.getPath().getName())
                .collect(Collectors.toList());
    }

    @Override
    public InputStream get(String path) throws IOException {
        return fs.open(new Path(path));
    }

    @Override
    public Path upload(InputStream inputStream, String path) throws IOException {
        Path filePath = new Path(fs.getWorkingDirectory(), path);
        if (fs.exists(filePath.getParent()) == false) {
            fs.mkdirs(filePath.getParent());
        }
        try (FSDataOutputStream outputStream = fs.create(filePath, false)) {
            IOUtils.copyBytes(inputStream, outputStream, 1024);
        }
        return filePath;
    }

    @Override
    public boolean delete(String path) throws IOException {
        return fs.delete(new Path(path), true);
    }

    @Override
    public Long getFileSize(String path) throws IOException {
        return fs.getFileStatus(new Path(path)).getLen();
    }

    @Override
    public List<FileStatus> listStatus(String path) throws IOException {
        if (exists(path) == false) {
            return Collections.emptyList();
        }
        Path filePath = new Path(fs.getWorkingDirectory(), path);
        return Arrays.asList(fs.listStatus(filePath));
    }
}
