package cn.sliew.scaleph.storage;

import cn.hutool.core.io.FileUtil;
import cn.sliew.scaleph.ApplicationTest;
import org.apache.flink.core.fs.FSDataOutputStream;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemTest extends ApplicationTest {

    @Autowired
    private FileSystem fileSystem;

    @Test
    public void testFileSystem() throws IOException {
        fileSystem.mkdirs(fileSystem.getWorkingDirectory());
        final Path path = new Path(fileSystem.getWorkingDirectory(), "single_split.png");

        final FileSystem.WriteMode writeMode = FileSystem.WriteMode.OVERWRITE;
        try (FSDataOutputStream outputStream = fileSystem.create(path, writeMode)) {
            File file = new File("/Users/wangqi/Downloads/single_split.png");
            FileUtil.writeToStream(file, outputStream);
        }

        assertTrue(fileSystem.exists(path));
    }
}
