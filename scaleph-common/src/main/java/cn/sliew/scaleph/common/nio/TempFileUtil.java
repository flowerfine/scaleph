package cn.sliew.scaleph.common.nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum TempFileUtil {
    ;

    public static final FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(
            new HashSet<>(Arrays.asList(
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_EXECUTE,
                    PosixFilePermission.GROUP_READ,
                    PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.GROUP_EXECUTE)));

    /**
     * TempFileHelper
     */
    public static boolean supportPosix() {
        return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    }

    public static Path createTempDir() throws IOException {
        if (supportPosix()) {
            return Files.createTempDirectory(null, attributes);
        }
        return Files.createTempDirectory(null);
    }

    public static Path createTempDir(String dirName) throws IOException {
        return createTempDir(createTempDir(), dirName);
    }

    public static Path createTempDir(Path parentDir, String dirName) throws IOException {
        Path dir = parentDir.resolve(dirName);
        if (supportPosix()) {
            return Files.createDirectory(dir, attributes);
        }
        return Files.createDirectory(dir);
    }

    public static Path createTempFile(String fileName) throws IOException {
        return createTempFile(createTempDir(), fileName);
    }

    public static Path createTempFile(Path tempDir, String fileName) throws IOException {
        if (supportPosix()) {
            return Files.createTempFile(tempDir, fileName, null, attributes);
        }
        return Files.createTempFile(tempDir, fileName, null);
    }

}
