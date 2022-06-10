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

public class FileSystemTest extends ApplicationTest {

    @Autowired
    private FileSystem fileSystem;

    @Test
    public void testFileSystem() throws IOException {
        final Path path = Path.fromLocalFile(new File("driver"));
        final FileSystem.WriteMode writeMode = FileSystem.WriteMode.NO_OVERWRITE;
        final FSDataOutputStream outputStream = fileSystem.create(path, writeMode);
        final File file = new File("/Users/wangqi/Documents/software/flink/flink-1.13.6/lib/mysql-connector-java-8.0.16.jar");
        FileUtil.writeToStream(file, outputStream);

    }
}
