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

package cn.sliew.scaleph.file.fetcher.cli;

import cn.sliew.milky.common.util.JacksonUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;

import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.file.fetcher.cli.OptionsParser.DYNAMIC_PROPERTIES;
import static cn.sliew.scaleph.file.fetcher.cli.OptionsParser.FILE_FETCHER_JSON;

@Getter
public class FetchOptions extends CommandLineOptions {

    private final List<FileFetcherParam> params;
    private final Properties properties;

    public FetchOptions(CommandLine line) {
        super(line);
        this.params = JacksonUtil.parseJsonArray(line.getOptionValue(FILE_FETCHER_JSON), FileFetcherParam.class);
        this.properties = line.getOptionProperties(DYNAMIC_PROPERTIES);
    }

    @Getter
    @Setter
    public static class FileFetcherParam {
        private String uri;
        private String path;
    }

}
