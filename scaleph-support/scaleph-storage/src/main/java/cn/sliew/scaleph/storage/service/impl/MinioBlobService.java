package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.common.enums.ContentTypeEnum;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.storage.service.BlobService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class MinioBlobService implements BlobService {

    private final String separator = "/";
    private final String bucket;
    private final MinioClient client;

    public MinioBlobService(@Value("${app.resource.minio.bucket:scaleph}") String bucket,
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
                this.client.makeBucket(MakeBucketArgs.builder()
                        .bucket(this.bucket)
                        .build());
            }
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public InputStream get(String fileName) throws IOException {
        try {
            return client.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public void upload(InputStream inputStream, String fileName) throws IOException {
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, -1, 10485760)
                    .contentType(ContentTypeEnum.getContentType(fileName))
                    .build());
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void delete(String fileName) throws IOException {
        try {
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public Long getFileSize(String fileName) throws IOException {
        Iterable<Result<Item>> results = client.listObjects(ListObjectsArgs.builder()
                .bucket(this.bucket)
                .prefix(fileName)
                .build());
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                if (item.objectName().equals(fileName) && item.isDir() == false) {
                    return item.size();
                }
            } catch (Exception e) {
                Rethrower.throwAs(e);
                return 0L;
            }
        }
        return 0L;
    }
}
