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

import cn.sliew.milky.test.MilkyTestCase;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TarUtilTest extends MilkyTestCase {

    @Test
    @Disabled("source tar.gz file not exists")
    void testUntar() throws IOException {
        final Path source = Paths.get("/Users/wangqi/Documents/software/flink/flink-1.14.5-bin-scala_2.11.tgz");
        final Path target = TarUtil.untar(source);
        assertTrue(Files.exists(target));
        final List<Path> childs = Files.list(target).collect(Collectors.toList());
        assertThat(childs).size().isGreaterThan(0);
        FileUtils.deleteDirectory(target.toFile());
    }
}
