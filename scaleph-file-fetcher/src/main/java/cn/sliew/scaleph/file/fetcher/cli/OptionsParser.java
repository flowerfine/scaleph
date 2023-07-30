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

import org.apache.commons.cli.*;

/**
 * A simple command line parser (based on Apache Commons CLI) that extracts command line options.
 */
public class OptionsParser {

    public static final Option HELP_OPTION =
            Option.builder()
                    .option("h")
                    .longOpt("help")
                    .required(false)
                    .hasArg(false)
                    .desc("Show the help message for the CLI Frontend or the action.")
                    .build();

    public static final Option FILE_FETCHER_JSON =
            Option.builder()
                    .longOpt("file-fetcher-json")
                    .required(true)
                    .hasArg(true)
                    .argName("[{\"uri\": \"...\", \"path\": \"...\"}]")
                    .desc("file fetcher json.")
                    .build();

    public static final Option URI_OPTION =
            Option.builder()
                    .option("uri")
                    .required(true)
                    .hasArg()
                    .desc("File remote uri.")
                    .build();

    public static final Option PATH_OPTION =
            Option.builder()
                    .option("path")
                    .required(true)
                    .hasArg()
                    .desc("File local path.")
                    .build();

    public static final Option DYNAMIC_PROPERTIES =
            Option.builder("D")
                    .argName("property=value")
                    .numberOfArgs(2)
                    .valueSeparator('=')
                    .desc("Allows specifying multiple generic configuration options.")
                    .build();

    private static Options buildGeneralOptions(Options options) {
        options.addOption(HELP_OPTION);
        options.addOption(FILE_FETCHER_JSON);
        options.addOption(DYNAMIC_PROPERTIES);
        return options;
    }

    /**
     * Prints the help for the client.
     */
    public static void printHelp() {
        System.out.println("./scaleph <uri> <path> [-Dproperty=value]");
        System.out.println();

        printHelpForInfo();
        System.out.println();
    }

    public static void printHelpForInfo() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(5);
        formatter.setWidth(80);

        formatter.printHelp(" ", buildGeneralOptions(new Options()));

        System.out.println();
    }

    // --------------------------------------------------------------------------------------------
    //  Line Parsing
    // --------------------------------------------------------------------------------------------

    public static CommandLine parse(String[] args, boolean stopAtNonOptions) throws CliArgsException {
        Options options = new Options()
                .addOption(FILE_FETCHER_JSON)
                .addOption(DYNAMIC_PROPERTIES);
        DefaultParser parser = new DefaultParser();

        try {
            return parser.parse(options, args, stopAtNonOptions);
        } catch (ParseException e) {
            throw new CliArgsException(e.getMessage());
        }
    }

}
