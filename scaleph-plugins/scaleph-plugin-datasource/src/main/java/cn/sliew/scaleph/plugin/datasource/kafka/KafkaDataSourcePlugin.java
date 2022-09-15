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

package cn.sliew.scaleph.plugin.datasource.kafka;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.common.exception.Rethrower;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static cn.sliew.scaleph.plugin.datasource.kafka.KafkaProperties.BOOTSTRAP_SERVERS;

@AutoService(DatasourcePlugin.class)
public class KafkaDataSourcePlugin extends DatasourcePlugin<Admin> {

    private Admin adminClient;

    public KafkaDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.KAFKA.getValue(), "kafka datasource",  KafkaDataSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(BOOTSTRAP_SERVERS);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void start() {
        if (!Optional.ofNullable(properties).isPresent()) {
            throw new IllegalStateException("kafka datasource plugin not initialized!");
        }
        final Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, properties.getString(BOOTSTRAP_SERVERS));
        try {
            this.adminClient = Admin.create(props);
            ListTopicsResult topicsResult = adminClient.listTopics();
            KafkaFuture<Set<String>> kafkaFuture = topicsResult.names();
            kafkaFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void shutdown() {
        if (this.adminClient != null) {
            this.adminClient.close();
        }
    }

    @Override
    public Admin getDatasource() {
        return this.adminClient;
    }

    @Override
    public boolean testConnection() {
        return this.adminClient != null;
    }

}
