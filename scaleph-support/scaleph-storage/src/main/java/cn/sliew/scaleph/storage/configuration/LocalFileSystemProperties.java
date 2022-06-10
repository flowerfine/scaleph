package cn.sliew.scaleph.storage.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file-system.type")
@ConditionalOnProperty(value = "file-system.type", havingValue = "local")
public class LocalFileSystemProperties extends FileSystemProperties {

    private String rootPath;
}
