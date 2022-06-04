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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cn.hutool.core.io.FileUtil;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = "local")
public class NioFileServiceImpl implements StorageService {

    private String parentDir;

    public NioFileServiceImpl(
        @Value("${app.resource.local.parentDir:/tmp}") String defaultFilePath) {
        this.parentDir = defaultFilePath;
    }

    @Override
    public boolean exists(String filePath) {
        return Files.exists(Paths.get(parentDir, filePath));
    }

    @Override
    public void delete(String filePath, String fileName) {
        try {
            Files.deleteIfExists(Paths.get(parentDir, filePath, fileName));
        } catch (IOException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void mkdirs(String filePath) {
        Path directory = Paths.get(parentDir, filePath);
        if (Files.exists(directory) && !isDirectory(filePath)) {
            Rethrower.throwAs(new IOException("File " + directory.toUri() +
                " exists and is not a directory. Unable to create directory."));
        } else {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                Rethrower.throwAs(e);
            }
        }
    }


    @Override
    public String upload(InputStream inputStream, String filePath, String fileName) {
        Path path = Paths.get(parentDir, filePath);
        if (!exists(filePath)) {
            Rethrower.throwAs(new IOException("File " + path.toUri() + "is not exists."));
        } else if (!Files.isDirectory(path)) {
            Rethrower.throwAs(
                new IOException(path.toUri() + " is not a directory. Unable to upload file."));
        } else {
            delete(filePath, fileName);
            try {
                Path file = Files.createFile(Paths.get(parentDir, filePath, fileName));
                FileUtil.writeFromStream(inputStream, file.toFile());
                return file.toUri().toString();
            } catch (IOException e) {
                Rethrower.throwAs(e);
            }
        }
        return null;
    }

    @Override
    public boolean isDirectory(String filePath) {
        return Files.isDirectory(Paths.get(parentDir, filePath));
    }

    @Override
    public InputStream get(String filePath, String fileName) {
        Path path = Paths.get(this.parentDir, filePath, fileName);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            File file = FileUtil.file(path.toUri());
            return FileUtil.getInputStream(file);
        } else {
            return null;
        }
    }

    @Override
    public Long getFileSize(String filePath, String fileName) {
        Path path = Paths.get(this.parentDir, filePath, fileName);
        if (Files.exists(path) && !Files.isDirectory(path)) {
            File file = FileUtil.file(path.toUri());
            return FileUtil.size(file);
        } else {
            return 0L;
        }
    }
}
