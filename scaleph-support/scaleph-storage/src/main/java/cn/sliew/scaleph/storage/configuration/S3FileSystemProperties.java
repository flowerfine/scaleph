package cn.sliew.scaleph.storage.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3FileSystemProperties extends FileSystemProperties {

    private String bucket;

    private String region;

    private String endpoint;

    private String accessKey;

    private String secretKey;
}
