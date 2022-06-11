package cn.sliew.scaleph.storage.service;

import org.apache.flink.core.fs.FileSystem;

import java.io.IOException;
import java.io.InputStream;

public interface FileSystemService {

    FileSystem getFileSystem();

    InputStream get(String fileName) throws IOException;

    void upload(InputStream inputStream, String fileName) throws IOException;

    boolean delete(String fileName) throws IOException;

    Long getFileSize(String fileName) throws IOException;
}
