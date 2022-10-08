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

package cn.sliew.scaleph.storage;

import cn.hutool.core.io.FileUtil;
import cn.sliew.scaleph.ApplicationTest;
import org.apache.flink.core.fs.FSDataOutputStream;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.core.fs.Path;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileSystemTest extends ApplicationTest {

    @Autowired
    private FileSystem fileSystem;

    @Disabled
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
