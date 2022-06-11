package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.storage.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.fs.FSDataOutputStream;
import org.apache.flink.core.fs.FileStatus;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.apache.flink.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class FileSystemServiceImpl implements FileSystemService {

    @Resource
    private FileSystem fs;

    @Override
    public FileSystem getFileSystem() {
        return fs;
    }

    @Override
    public InputStream get(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.open(path);
    }

    @Override
    public void upload(InputStream inputStream, String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        try (final FSDataOutputStream outputStream = fs.create(path, FileSystem.WriteMode.NO_OVERWRITE)) {
            IOUtils.copyBytes(inputStream, outputStream);
        }
    }

    @Override
    public boolean delete(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        return fs.delete(path, true);
    }

    @Override
    public Long getFileSize(String fileName) throws IOException {
        Path path = new Path(fs.getWorkingDirectory(), fileName);
        final FileStatus fileStatus = fs.getFileStatus(path);
        return fileStatus.getBlockSize();
    }
}
