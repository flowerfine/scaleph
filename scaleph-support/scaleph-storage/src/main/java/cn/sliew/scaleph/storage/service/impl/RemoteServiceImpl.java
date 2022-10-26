package cn.sliew.scaleph.storage.service.impl;

import cn.sliew.scaleph.storage.service.FileSystemService;
import cn.sliew.scaleph.storage.service.RemoteService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

@Service
public class RemoteServiceImpl implements RemoteService, InitializingBean {

    private OkHttpClient client;

    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public void afterPropertiesSet() throws Exception {
        client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30L))
                .callTimeout(Duration.ofSeconds(30L))
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
        try (Response response = client.newCall(request).execute();
             InputStream inputStream = response.body().byteStream()) {
            fileSystemService.upload(inputStream, fileName);
        }
    }

    @Override
    public void generate(String fileName) throws IOException {
        throw new UnsupportedOperationException();
    }
}
