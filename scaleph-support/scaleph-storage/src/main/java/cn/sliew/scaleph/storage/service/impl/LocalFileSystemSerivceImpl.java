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
import cn.sliew.scaleph.storage.utils.HadoopUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;

@Slf4j
@Service
public class LocalFileSystemSerivceImpl implements FileSystemService, InitializingBean, DisposableBean {

    private Cache<String, Path> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofHours(1L))
            .removalListener((RemovalListener<String, Path>) (cacheKey, path, removalCause) -> {
                try {
                    delete(cacheKey);
                } catch (IOException e) {
                    log.error("clear local file system cache error! cacheKey: {}, path: {}",
                            JacksonUtil.toJsonString(cacheKey), path, e);
                }
            })
            .build();

    private FileSystem fs;

    @Override
    public void afterPropertiesSet() throws Exception {
        Configuration conf = HadoopUtil.getHadoopConfiguration(null);
        fs = LocalFileSystem.getLocal(conf);
    }

    @Override
    public void destroy() throws Exception {
        fs.close();
    }

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public boolean isDistributedFS() {
        return false;
    }

    @Override
    public boolean exists(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.exists(path);
    }

    @Override
    public List<String> list(String directory) throws IOException {
        throw new UnsupportedOperationException();
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
        // enable cache and will be evicted automatically in the later hour
        cache.put(fileName, path);
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        cache.invalidate(fileName);
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
        throw new UnsupportedOperationException();
    }
}
