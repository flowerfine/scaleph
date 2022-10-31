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

package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.storage.service.RemoteService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Slf4j
@Service
public class RemoteServiceImpl implements RemoteService, InitializingBean {

    private OkHttpClient client;

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMinutes(1L))
                .callTimeout(Duration.ofHours(3L))
                .readTimeout(Duration.ofHours(3L))
                .writeTimeout(Duration.ofHours(3L))
                .addInterceptor(new HttpLoggingInterceptor())
                .build();
    }

    @Override
    public void fetch(String url, String fileName) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.error("download remote file: {} from {} error!", fileName, url, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() == false) {
                    log.error("download remote file: {} from {} error! code: {}, message: {}",
                            fileName, url, response.code(), response.message());
                    return;
                }
                try (InputStream inputStream = response.body().byteStream()) {
                    fileSystemService.upload(inputStream, fileName);
                }
            }
        };
        client.newCall(request).enqueue(callback);
    }

    @Override
    public void generate(String fileName) throws IOException {
        throw new UnsupportedOperationException();
    }
}
