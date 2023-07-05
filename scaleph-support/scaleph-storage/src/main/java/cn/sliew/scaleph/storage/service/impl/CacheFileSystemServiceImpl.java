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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.storage.util.HadoopUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@Primary
public class CacheFileSystemServiceImpl implements FileSystemService, InitializingBean, DisposableBean {

    private Cache<String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1L))
            .removalListener((RemovalListener<String, String>) (cacheKey, path, removalCause) -> {
                try {
                    delete(cacheKey);
                } catch (IOException e) {
                    log.error("clear local file system cache error! cacheKey: {}",
                            JacksonUtil.toJsonString(cacheKey), e);
                }
            })
            .build();

    private LocalFileSystem localFileSystem;

    @Autowired
    private FileSystemServiceImpl fileSystemService;
    @Autowired
    private FileSystemServiceImpl localFileSystemSerivce;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration conf = HadoopUtil.getHadoopConfiguration(null);
        localFileSystem = LocalFileSystem.getLocal(conf);
        localFileSystemSerivce = new FileSystemServiceImpl(localFileSystem);
    }

    @Override
    public void destroy() throws Exception {
        localFileSystem.close();
    }

    @Override
    public FileSystem getFileSystem() {
        return fileSystemService.getFileSystem();
    }

    @Override
    public boolean isDistributedFS() {
        return fileSystemService.isDistributedFS();
    }

    @Override
    public boolean exists(String path) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.exists(path);
        }
        String localPath = replaceSchema(path);
        if (localFileSystemSerivce.exists(localPath)) {
            return true;
        }
        return fileSystemService.exists(path);
    }

    @Override
    public List<String> list(String path) throws IOException {
        return fileSystemService.list(path);
    }

    @Override
    public InputStream get(String path) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.get(path);
        }
        String localPath = replaceSchema(path);
        if (localFileSystemSerivce.exists(localPath)) {
            return localFileSystemSerivce.get(localPath);
        }
        try (InputStream inputStream = fileSystemService.get(path)) {
            localFileSystemSerivce.upload(inputStream, localPath);
            // enable cache and will be evicted automatically in the later hour
            cache.put(localPath, localPath);
        }
        return localFileSystemSerivce.get(localPath);
    }

    @Override
    public Path upload(InputStream inputStream, String path) throws IOException {
        if (fileSystemService.exists(path)) {
            fileSystemService.delete(path);
        }
        return fileSystemService.upload(inputStream, path);
    }

    @Override
    public boolean delete(String path) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.delete(path);
        }

        String localPath = replaceSchema(path);
        if (localFileSystemSerivce.exists(localPath)) {
            localFileSystemSerivce.delete(localPath);
            cache.invalidate(localPath);
        }
        return fileSystemService.delete(path);
    }

    @Override
    public Long getFileSize(String path) throws IOException {
        if (fileSystemService.isDistributedFS() == false) {
            return fileSystemService.getFileSize(path);
        }

        String localPath = replaceSchema(path);
        if (localFileSystemSerivce.exists(localPath)) {
            return localFileSystemSerivce.getFileSize(localPath);
        }
        return fileSystemService.getFileSize(path);
    }

    @Override
    public List<FileStatus> listStatus(String path) throws IOException {
        return fileSystemService.listStatus(path);
    }

    private String replaceSchema(String path) {
        if (path.startsWith(localFileSystem.getScheme())) {
            return path;
        }
        Path filePath = new Path(path);
        return localFileSystem.getWorkingDirectory().toString() + "/" + filePath.toUri().getPath();
    }
}
