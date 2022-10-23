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
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Primary
public class CacheFileSystemServiceImpl implements FileSystemService {

    @Autowired
    private FileSystemServiceImpl fileSystemService;
    @Autowired
    private LocalFileSystemSerivceImpl localFileSystemSerivce;

    @Override
    public FileSystem getFileSystem() {
        return fileSystemService.getFileSystem();
    }

    @Override
    public boolean isDistributedFS() {
        return fileSystemService.isDistributedFS();
    }

    @Override
    public boolean exists(String fileName) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.exists(fileName);
        }
        if (localFileSystemSerivce.exists(fileName)) {
            return true;
        }
        return fileSystemService.exists(fileName);
    }

    @Override
    public List<String> list(String directory) throws IOException {
        return fileSystemService.list(directory);
    }

    @Override
    public InputStream get(String fileName) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.get(fileName);
        }
        if (localFileSystemSerivce.exists(fileName)) {
            return localFileSystemSerivce.get(fileName);
        }
        try (InputStream inputStream = fileSystemService.get(fileName)) {
            localFileSystemSerivce.upload(inputStream, fileName);
        }
        return localFileSystemSerivce.get(fileName);
    }

    @Override
    public void upload(InputStream inputStream, String fileName) throws IOException {
        if (fileSystemService.exists(fileName)) {
            fileSystemService.delete(fileName);
        }
        fileSystemService.upload(inputStream, fileName);
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.delete(fileName);
        }

        if (localFileSystemSerivce.exists(fileName)) {
            localFileSystemSerivce.delete(fileName);
        }
        return fileSystemService.delete(fileName);
    }

    @Override
    public Long getFileSize(String fileName) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.getFileSize(fileName);
        }

        if (localFileSystemSerivce.exists(fileName)) {
            return localFileSystemSerivce.getFileSize(fileName);
        }
        return fileSystemService.getFileSize(fileName);
    }

    @Override
    public List<FileStatus> listStatus(String directory) throws IOException {
        return fileSystemService.listStatus(directory);
    }
}
