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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static Path getBaseTempDir() throws IOException {
        try {
            String path = System.getProperty("java.io.tmpdir");
            return Paths.get(path);
        } catch (Exception e) {
            return createTempDir();
        }
    }

    public static Path createTempDir(String dirName) throws IOException {
        return createTempDir(getBaseTempDir(), dirName);
    }

    public static Path createTempDir(Path parentDir, String dirName) throws IOException {
        Path dir = parentDir.resolve(dirName);
        if (Files.notExists(dir)) {
            if (supportPosix()) {
                return Files.createDirectory(dir, attributes);
            }
            return Files.createDirectory(dir);
        } else {
            return dir;
        }
    }

    public static Path createTempFile(String fileName) throws IOException {
        return createTempFile(getBaseTempDir(), fileName);
    }

    public static Path createTempFile(Path tempDir, String fileName) throws IOException {
        return createTempFile(tempDir, fileName, null);
    }

    public static Path createTempFile(Path tempDir, String prefix, String suffix) throws IOException {
        if (supportPosix()) {
            return Files.createTempFile(tempDir, prefix, suffix, attributes);
        }
        return Files.createTempFile(tempDir, prefix, suffix);
    }

}
