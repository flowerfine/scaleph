package cn.sliew.scaleph.storage.service;

import java.io.IOException;
import java.io.InputStream;

public interface BlobService {

    InputStream get(String fileName) throws IOException;

    void upload(InputStream inputStream, String fileName) throws IOException;

    void delete(String fileName) throws IOException;

    Long getFileSize(String fileName) throws IOException;
}
