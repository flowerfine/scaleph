package cn.sliew.scaleph.storage.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OSSFileSystemProperties extends FileSystemProperties {

    private String bucket;

    private String endpoint;

    private String accessKey;

    private String secretKey;
}
