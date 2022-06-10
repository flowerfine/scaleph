package cn.sliew.scaleph.storage.configuration;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FileSystemType {

    LOCAL(0, "local", "file://"),
    HDFS(1, "hdfs", "hdfs://"),
    S3(2, "s3", "s3://"),
    OSS(3, "oss", "oss://"),
    ;

    private int code;
    @JsonValue
    private String type;
    private String schema;

    FileSystemType(int code, String type, String schema) {
        this.code = code;
        this.type = type;
        this.schema = schema;
    }
}
