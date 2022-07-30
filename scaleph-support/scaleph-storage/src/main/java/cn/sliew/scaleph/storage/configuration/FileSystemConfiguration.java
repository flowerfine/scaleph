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

package cn.sliew.scaleph.storage.configuration;

import org.apache.hadoop.fs.FileSystem;
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
    public FileSystem localFileSystem(LocalFileSystemProperties localFileSystemProperties) throws IOException {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        return FileSystem.getLocal(conf);
    }

    @SuppressWarnings("all")
    @Bean
    @ConfigurationProperties(prefix = "file-system")
    @ConditionalOnProperty(value = "file-system.type", havingValue = "s3")
    public S3FileSystemProperties s3FileSystemProperties() {
        return new S3FileSystemProperties();
    }

    @Bean
    @ConditionalOnProperty(value = "file-system.type", havingValue = "s3")
    public FileSystem s3FileSystem(S3FileSystemProperties s3FileSystemProperties) throws URISyntaxException, IOException {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.s3a.endpoint", s3FileSystemProperties.getEndpoint());
        conf.set("fs.s3a.access.key", s3FileSystemProperties.getAccessKey());
        conf.set("fs.s3a.secret.key", s3FileSystemProperties.getSecretKey());
        conf.setBoolean("fs.s3a.path-style-access", true);
        URI uri = new URI(FileSystemType.S3.getSchema() + s3FileSystemProperties.getBucket());
        return FileSystem.get(uri, conf);
    }

    @SuppressWarnings("all")
    @Bean
    @ConfigurationProperties(prefix = "file-system")
    @ConditionalOnProperty(value = "file-system.type", havingValue = "oss")
    public OSSFileSystemProperties ossFileSystemProperties() {
        return new OSSFileSystemProperties();
    }

    @Bean
    @ConditionalOnProperty(value = "file-system.type", havingValue = "oss")
    public FileSystem ossFileSystem(OSSFileSystemProperties ossFileSystemProperties) throws IOException {
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.oss.endpoint", ossFileSystemProperties.getEndpoint());
        conf.set("fs.oss.accessKeyId", ossFileSystemProperties.getAccessKey());
        conf.set("fs.oss.accessKeySecret", ossFileSystemProperties.getSecretKey());
        return FileSystem.get(conf);
    }

    @SuppressWarnings("all")
    @Bean
    @ConfigurationProperties(prefix = "file-system")
    @ConditionalOnProperty(value = "file-system.type", havingValue = "hdfs")
    public HDFSFileSystemProperties hdfsFileSystemProperties() {
        return new HDFSFileSystemProperties();
    }

    @Bean
    @ConditionalOnProperty(value = "file-system.type", havingValue = "hdfs")
    public FileSystem hdfsFileSystem(HDFSFileSystemProperties hdfsFileSystemProperties) throws URISyntaxException, IOException {
//        HadoopFsFactory factory = new HadoopFsFactory();
//        org.apache.flink.configuration.Configuration config = new org.apache.flink.configuration.Configuration();
//        config.setString(CoreOptions.FLINK_HADOOP_CONF_DIR, hdfsFileSystemProperties.getHadoopConfPath());
//        factory.configure(config);
//        return factory.create(new URI(hdfsFileSystemProperties.getDefaultFS()));
        return null;
    }
}
