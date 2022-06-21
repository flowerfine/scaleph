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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.sliew.scaleph.common.enums.ContentTypeEnum;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.storage.service.StorageService;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "minio")
public class MinioFileServiceImpl implements StorageService {
    private final String separator = "/";
    private final String bucket;
    private final MinioClient client;

    public MinioFileServiceImpl(@Value("${app.resource.minio.bucket:scaleph}") String bucket,
                                @Value("${app.resource.minio.endpoint:localhost}") String endPoint,
                                @Value("${app.resource.minio.accessKey:''}") String accessKey,
                                @Value("${app.resource.minio.secretKey:''}") String secretKey) {
        this.bucket = bucket;
        this.client = MinioClient.builder()
            .endpoint(endPoint)
            .credentials(accessKey, secretKey)
            .build();
        try {
            if (!this.client.bucketExists(BucketExistsArgs.builder()
                .bucket(this.bucket)
                .build())) {
                this.client.makeBucket(MakeBucketArgs.builder().bucket(this.bucket).build());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String filePath) {
        if ("".equals(filePath)) {
            return true;
        }
        Item item = getObject(filePath);
        return item != null;
    }


    @Override
    public void delete(String filePath, String fileName) {
        if (filePath != null && fileName != null) {
            if (!filePath.endsWith(separator)) {
                filePath += separator;
            }
            if (separator.equals(filePath)) {
                filePath = "";
            }
            try {
                client.removeObject(
                    RemoveObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object(filePath + fileName)
                        .build()
                );
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
    }

    @Override
    public void mkdirs(String filePath) {
        if (filePath != null) {
            if (!filePath.endsWith(separator)) {
                filePath += separator;
            }
            try {
                this.client.putObject(
                    PutObjectArgs
                        .builder()
                        .bucket(this.bucket)
                        .object(filePath)
                        .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                        .build()
                );
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
    }

    @Override
    public String upload(InputStream inputStream, String filePath, String fileName) {
        if (!exists(filePath)) {
            Rethrower.throwAs(new IOException("File " + filePath + " is not exists."));
        } else if (!isDirectory(filePath)) {
            Rethrower.throwAs(
                new IOException(filePath + " is not a directory. Unable to upload file."));
        } else {
            if (!filePath.endsWith(separator)) {
                filePath += separator;
            }
            if (separator.equals(filePath)) {
                filePath = "";
            }
            delete(filePath, fileName);
            try {
                ObjectWriteResponse response = client.putObject(
                    PutObjectArgs
                        .builder()
                        .bucket(bucket)
                        .object(filePath + fileName)
                        .stream(inputStream, -1, 10485760)
                        .contentType(ContentTypeEnum.getContentType(fileName))
                        .build());
                return response.object();
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
        return null;
    }

    @Override
    public boolean isDirectory(String filePath) {
        if ("".equals(filePath)) {
            return true;
        }
        Item item = getObject(filePath);
        return item != null && item.isDir();
    }

    @Override
    public InputStream get(String filePath, String fileName) {
        if (filePath != null && fileName != null) {
            if (!filePath.endsWith(separator)) {
                filePath += separator;
            }
            if (separator.equals(filePath)) {
                filePath = "";
            }
            try {
                Item item = getObject(filePath + fileName);
                return client.getObject(
                    GetObjectArgs.builder().bucket(bucket).object(item.objectName()).build());
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
        return null;
    }

    @Override
    public Long getFileSize(String filePath, String fileName) {
        if (!filePath.endsWith(separator)) {
            filePath += separator;
        }
        if (separator.equals(filePath)) {
            filePath = "";
        }
        Item item = getObject(filePath + fileName);
        return item == null ? 0L : item.size();
    }

    private Item getObject(String filePath) {
        if (filePath == null) {
            return null;
        }
        if (filePath.endsWith(separator)) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        Iterable<Result<Item>> results = client.listObjects(
            ListObjectsArgs.builder().bucket(this.bucket).prefix(filePath).build()
        );
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                if (item.isDir() && item.objectName().equals(filePath + separator)) {
                    return item;
                } else if (item.objectName().equals(filePath)) {
                    return item;
                }
            } catch (Exception e) {
                Rethrower.throwAs(e);
            }
        }
        return null;
    }

}
