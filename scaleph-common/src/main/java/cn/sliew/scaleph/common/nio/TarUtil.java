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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public enum TarUtil {
    ;

    public static Path untar(Path source) throws IOException {
        try (BufferedInputStream fi = (BufferedInputStream) FileUtil.getInputStream(source);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(fi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {
            String targetDir = getTaregtDir(gzi.getMetaData(), source.getFileName().toString());
            Path target = source.getParent().resolve(targetDir);
            FileUtil.createDir(target);

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {
                Path newPath = zipSlipProtect(entry, target);
                if (entry.isDirectory()) {
                    FileUtil.createDir(newPath);
                } else {
                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            FileUtil.createDir(parent);
                        }
                    }
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return target;
        }
    }

    private static String getTaregtDir(GzipParameters parameters, String fileName) {
        String filename = parameters.getFilename();
        if (StringUtils.hasText(filename)) {
            return filename;
        }
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private static Path zipSlipProtect(ArchiveEntry entry, Path targetDir) throws IOException {
        Path targetDirResolved = Paths.get(targetDir.toString(), entry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("tar file already breakdown: " + entry.getName());
        }
        return normalizePath;
    }
}
