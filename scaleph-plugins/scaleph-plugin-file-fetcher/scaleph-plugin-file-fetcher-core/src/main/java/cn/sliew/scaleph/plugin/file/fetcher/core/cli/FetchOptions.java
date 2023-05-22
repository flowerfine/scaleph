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

package cn.sliew.scaleph.plugin.file.fetcher.core.cli;

import lombok.Getter;
import org.apache.commons.cli.CommandLine;

import static cn.sliew.scaleph.plugin.file.fetcher.core.cli.OptionsParser.PATH_OPTION;
import static cn.sliew.scaleph.plugin.file.fetcher.core.cli.OptionsParser.URI_OPTION;

@Getter
public class FetchOptions extends CommandLineOptions {

    private final String uri;
    private final String path;

    public FetchOptions(CommandLine line) {
        super(line);
        this.uri = line.getOptionValue(URI_OPTION);
        this.path = line.getOptionValue(PATH_OPTION);
    }

}
