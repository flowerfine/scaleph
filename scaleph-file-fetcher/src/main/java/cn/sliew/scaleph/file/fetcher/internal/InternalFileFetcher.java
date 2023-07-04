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

package cn.sliew.scaleph.file.fetcher.internal;

import cn.sliew.scaleph.common.nio.FileUtil;
import cn.sliew.scaleph.file.fetcher.FileFetcher;
import cn.sliew.scaleph.storage.service.impl.FileSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URI;

@Component
public class InternalFileFetcher implements FileFetcher {

    @Autowired
    private FileSystemServiceImpl fileSystemService;

    @Override
    public boolean support(URI uri) {
        return uri.getScheme().equals("scaleph");
    }

    @Override
    public void fetch(URI uri, String path) throws IOException {
        if (fileSystemService.exists(uri.getPath()) == false) {
            throw new FileNotFoundException(uri.getPath());
        }
        try (InputStream inputStream = fileSystemService.get(uri.getPath());
             OutputStream outputStream = FileUtil.getOutputStream(new File(path))) {
            FileCopyUtils.copy(inputStream, outputStream);
        }
    }
}
