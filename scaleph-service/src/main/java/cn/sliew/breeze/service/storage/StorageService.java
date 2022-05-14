package cn.sliew.breeze.service.storage;

import java.io.InputStream;

public interface StorageService {

    boolean exists(String filePath);

    void delete(String filePath, String fileName);

    void mkdirs(String filePath);

    String upload(InputStream inputStream, String filePath, String fileName);

    boolean isDirectory(String filePath);

    InputStream get(String filePath, String fileName);

    Long getFileSize(String filePath, String fileName);
}
