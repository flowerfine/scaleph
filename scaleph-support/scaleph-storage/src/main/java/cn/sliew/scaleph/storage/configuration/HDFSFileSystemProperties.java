package cn.sliew.scaleph.storage.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HDFSFileSystemProperties extends FileSystemProperties {

    private String hadoopConfPath;

    private String krb5ConfPath;
}
