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

package cn.sliew.scaleph.plugin.datasource.elasticsearch;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.elasticsearch.ElasticsearchProperties.*;

@Slf4j
@AutoService(DatasourcePlugin.class)
public class ElasticsearchDataSourcePlugin extends DatasourcePlugin<RestHighLevelClient> {

    private RestHighLevelClient client;

    public ElasticsearchDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.ELASTICSEARCH.getValue(), "Elasticsearch Datasource", ElasticsearchDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOSTS);
        props.add(USERNAME);
        props.add(PASSWORD);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void start() {
        final String hosts = properties.get(HOSTS);
        final HttpHost[] httpHosts = Arrays.stream(hosts.split(","))
                .map(host -> HttpHost.create(host)).toArray(length -> new HttpHost[length]);
        RestClientBuilder builder = RestClient.builder(httpHosts);

        final String username = properties.get(USERNAME);
        final String password = properties.get(PASSWORD);
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));
            builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }
        this.client = new RestHighLevelClient(builder);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            client.close();
        } catch (IOException e) {
            log.error("close elasticsearch rest high client error!", e);
        }
    }

    @Override
    public RestHighLevelClient getDatasource() {
        return client;
    }

    @Override
    public boolean testConnection() {
        try {
            return client.ping(RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("test elasticsearch connection by ping error", e);
            return false;
        }
    }
}
