package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.storage.service.FileSystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.core.fs.FileSystem;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileSystemServiceImpl implements FileSystemService {

    private FileSystem fs;

    public FileSystemServiceImpl() {
        this.fs = fs;
    }
}
