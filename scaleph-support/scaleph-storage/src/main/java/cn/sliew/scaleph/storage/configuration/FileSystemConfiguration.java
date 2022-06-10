package cn.sliew.scaleph.storage.configuration;

import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.fs.s3hadoop.S3AFileSystemFactory;
import org.apache.flink.fs.s3hadoop.S3FileSystemFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableConfigurationProperties(LocalFileSystemProperties.class)
public class FileSystemConfiguration {


    @Bean
    @ConditionalOnProperty(value = "file-system.type", havingValue = "local")
    public FileSystem localFileSystem(LocalFileSystemProperties localFileSystemProperties) {
        return FileSystem.getLocalFileSystem();
    }

    @Bean
    @ConfigurationProperties(prefix = "file-system")
    @ConditionalOnProperty(value = "file-system.type", havingValue = "s3")
    public S3FileSystemProperties s3FileSystemProperties() {
        return new S3FileSystemProperties();
    }

    @Bean
    @ConditionalOnProperty(value = "file-system.type", havingValue = "s3")
    public FileSystem s3FileSystem(S3FileSystemProperties s3FileSystemProperties) throws URISyntaxException, IOException {
        S3FileSystemFactory factory = new S3FileSystemFactory();
        org.apache.flink.configuration.Configuration config = new org.apache.flink.configuration.Configuration();
        config.setString("s3.endpoint", s3FileSystemProperties.getEndpoint());
        config.setString("s3.access-key", s3FileSystemProperties.getAccessKey());
        config.setString("s3.secret-key", s3FileSystemProperties.getSecretKey());
        factory.configure(config);
        return factory.create(new URI(FileSystemType.S3.getSchema() + s3FileSystemProperties.getBucket()));
    }
}
