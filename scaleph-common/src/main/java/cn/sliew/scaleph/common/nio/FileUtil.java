/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.common.nio;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public enum FileUtil {
    ;

    private static boolean SUPPORT_POSIX = supportPosix();

    public static final FileAttribute<Set<PosixFilePermission>> ATTRIBUTES = PosixFilePermissions.asFileAttribute(
            new HashSet<>(Arrays.asList(
                    PosixFilePermission.OWNER_WRITE,
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_EXECUTE,
                    PosixFilePermission.GROUP_READ,
                    PosixFilePermission.GROUP_WRITE,
                    PosixFilePermission.GROUP_EXECUTE)));

    public static boolean supportPosix() {
        return FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
    }

    public static Path createDir() throws IOException {
        if (SUPPORT_POSIX) {
            return Files.createTempDirectory(null, ATTRIBUTES);
        }
        return Files.createTempDirectory(null);
    }

    public static Path getBaseTempDir() throws IOException {
        try {
            String path = System.getProperty("java.io.tmpdir");
            return Paths.get(path);
        } catch (Exception e) {
            return createDir();
        }
    }

    public static Path createTempDir(String dirName) throws IOException {
        return createDir(getBaseTempDir(), dirName);
    }

    public static Path createTempFile(String fileName) throws IOException {
        return createTempFile(getBaseTempDir(), fileName);
    }

    public static Path createTempFile(Path tempDir, String fileName) throws IOException {
        return createTempFile(tempDir, fileName, null);
    }

    public static Path createTempFile(Path tempDir, String prefix, String suffix) throws IOException {
        if (SUPPORT_POSIX) {
            return Files.createTempFile(tempDir, prefix, suffix, ATTRIBUTES);
        }
        return Files.createTempFile(tempDir, prefix, suffix);
    }

    public static Path createDir(Path parentDir, String dirName) throws IOException {
        Path dir = Paths.get(parentDir.toString(), dirName);
        if (Files.notExists(dir)) {
            if (SUPPORT_POSIX) {
                return Files.createDirectories(dir, ATTRIBUTES);
            }
            return Files.createDirectories(dir);
        } else {
            return dir;
        }
    }

    public static Path createDir(Path dir) throws IOException {
        if (Files.notExists(dir)) {
            if (SUPPORT_POSIX) {
                return Files.createDirectories(dir, ATTRIBUTES);
            }
            return Files.createDirectories(dir);
        } else {
            return dir;
        }
    }

    public static Path createFile(Path parent, String fileName) throws IOException {
        Path filePath = Paths.get(parent.toString(), fileName);
        return createFile(filePath);
    }

    public static Path createFile(Path filePath) throws IOException {
        Files.deleteIfExists(filePath);
        if (SUPPORT_POSIX) {
            return Files.createFile(filePath, ATTRIBUTES);
        }
        return Files.createFile(filePath);
    }

    public static void deleteFile(Path file) throws IOException {
        Files.deleteIfExists(file);
    }

    public static void deleteDir(Path dir) throws IOException {
        if (Files.exists(dir)) {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.deleteIfExists(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.deleteIfExists(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            Files.deleteIfExists(dir);
        }
    }

    public static OutputStream getOutputStream(File file) throws IOException {
        if (!file.exists()) {
            createFile(file.toPath());
        }
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    public static OutputStream getOutputStream(Path path) throws IOException {
        return getOutputStream(path.toFile());
    }

    public static Writer getWriter(File file) throws IOException {
        if (!file.exists()) {
            createFile(file.toPath());
        }
        return new FileWriter(file);
    }

    public static Writer getWriter(Path path) throws IOException {
        return getWriter(path.toFile());
    }

    public static InputStream getInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    public static InputStream getInputStream(Path filePath) throws FileNotFoundException {
        return getInputStream(filePath.toFile());
    }

    public static List<Path> listFiles(Path path) throws IOException {
        if (path == null) {
            return null;
        }
        return Files.list(path).collect(Collectors.toList());
    }

}
