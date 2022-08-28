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
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
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
public class FileSystemServiceImpl implements FileSystemService, InitializingBean {

    private FileSystem localFs;
    @Resource
    private FileSystem fs;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration conf = new Configuration();
        this.localFs = FileSystem.getLocal(conf);
    }

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    private boolean isDistributedFS() {
        return fs.getScheme().equals(FileSystemType.LOCAL.getSchema()) == false;
    }

    @Override
    public boolean exists(String fileName) throws IOException {
        if (localExists(fileName)) {
            return true;
        }
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.exists(path);
    }

    private boolean localExists(String fileName) throws IOException {
        if (isDistributedFS()) {
            Path path = new Path(localFs.getWorkingDirectory(), fileName);
            return localFs.exists(path);
        }
        return false;
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
        final InputStream localInputStream = localGet(fileName);
        if (localInputStream != null) {
            return localInputStream;
        }
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        final FSDataInputStream inputStream = fs.open(path);
        if (isDistributedFS()) {
            localUpload(inputStream, fileName);
            return localGet(fileName);
        }
        return inputStream;
    }

    public InputStream localGet(String fileName) throws IOException {
        if (isDistributedFS() && localExists(fileName)) {
            Path path = new Path(localFs.getWorkingDirectory(), fileName);
            return localFs.open(path);
        }
        return null;
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

    private void localUpload(InputStream inputStream, String fileName) throws IOException{
        if (isDistributedFS()) {
            Path localPath = new Path(localFs.getWorkingDirectory(), fileName);
            if (localFs.exists(localPath.getParent()) == false) {
                localFs.mkdirs(localPath.getParent());
            }
            try (final FSDataOutputStream outputStream = localFs.create(localPath, false)) {
                IOUtils.copyBytes(inputStream, outputStream, 1024);
            }
        }
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        localDelete(fileName);
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.delete(path, true);
    }

    private boolean localDelete(String fileName) throws IOException {
        if (localExists(fileName)) {
            Path path = new Path(localFs.getWorkingDirectory(), fileName);
            return localFs.delete(path, true);
        }
        return false;
    }

    @Override
    public Long getFileSize(String fileName) throws IOException {
        final Long localFileSize = localGetFileSize(fileName);
        if (localFileSize != null) {
            return localFileSize;
        }
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        final FileStatus fileStatus = fs.getFileStatus(path);
        return fileStatus.getLen();
    }

    private Long localGetFileSize(String fileName) throws IOException {
        if (localExists(fileName)) {
            Path path = new Path(localFs.getWorkingDirectory(), fileName);
            final FileStatus fileStatus = localFs.getFileStatus(path);
            return fileStatus.getBlockSize();
        }
        return null;
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
