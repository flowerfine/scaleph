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

package cn.sliew.scaleph.plugin.seatunnel.flink;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

class ConfConverterTest {

    private Properties properties;

    @BeforeEach
    private void beforeEach() throws Exception {
        final InputStream inputStream = ConfConverterTest.class.getClassLoader().getResourceAsStream("conf.properties");
        properties = new Properties();
        properties.load(inputStream);
    }

    @Test
    void testCreate() {
//        ConfigFileConverter converter = new ConfigFileConverter();
//        converter.addEnvConverter(new CheckpointEnvConverter(properties));
//        converter.addSourceConverter(new JdbcSourceConnector(properties));
//        converter.addSinkConverter(new JdbcSinkConnector(properties));
//        ObjectNode conf = converter.create();
//        System.out.println(conf.toPrettyString());
    }
}
