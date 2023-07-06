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

package cn.sliew.scaleph.file.fetcher.executor;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.file.fetcher.FileFetcher;
import cn.sliew.scaleph.file.fetcher.FileFetcherFactory;
import cn.sliew.scaleph.file.fetcher.cli.FetchOptions;
import cn.sliew.scaleph.file.fetcher.cli.OptionsParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class FetcherExecutor implements ApplicationRunner {

    @Autowired
    private FileFetcherFactory fileFetcherFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("命令行参数: {}", JacksonUtil.toJsonString(Arrays.asList(args.getSourceArgs())));
            CommandLine line = OptionsParser.parse(args.getSourceArgs(), true);
            FetchOptions options = new FetchOptions(line);
            Optional<FileFetcher> fileFetcher = fileFetcherFactory.find(options.getUri(), options.getProperties());
            fileFetcher.orElseThrow().fetch(options.getUri(), options.getPath());
        } catch (Exception e) {
            log.error("下载文件异常! 参数: {}", JacksonUtil.toJsonString(Arrays.asList(args.getSourceArgs())), e);
        }
    }
}
