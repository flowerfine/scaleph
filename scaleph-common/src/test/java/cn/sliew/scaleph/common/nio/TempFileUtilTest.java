package cn.sliew.scaleph.common.nio;

import cn.sliew.milky.test.MilkyTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TempFileUtilTest extends MilkyTestCase {

    @Test
    void testCreateTempDir() throws IOException {
        final Path tempDir = TempFileUtil.createTempDir();
        assertTrue(Files.exists(tempDir));
        Files.deleteIfExists(tempDir);
    }

    @Test
    void testCreateTempFile() throws IOException {
        final Path tempFile = TempFileUtil.createTempFile(randomAsciiLettersOfLength(5));
        assertTrue(Files.exists(tempFile));
        Files.deleteIfExists(tempFile);
    }
}
