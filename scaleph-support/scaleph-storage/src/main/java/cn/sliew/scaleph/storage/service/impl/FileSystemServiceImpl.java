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

import cn.sliew.scaleph.storage.configuration.FileSystemType;
import cn.sliew.scaleph.storage.service.FileSystemService;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Resource
    private FileSystem fs;

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public boolean isDistributedFS() {
        return fs.getScheme().equals(FileSystemType.LOCAL.getSchema()) == false;
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
        try (final FSDataOutputStream outputStream = fs.create(path, false)) {
            IOUtils.copyBytes(inputStream, outputStream, 1024);
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
        return fileStatus.getLen();
    }

    @Override
    public List<FileStatus> listStatus(String directory) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), directory);
        if (fs.exists(path)) {
            return Arrays.asList(fs.listStatus(path));
        }
        return Collections.emptyList();
    }
}
