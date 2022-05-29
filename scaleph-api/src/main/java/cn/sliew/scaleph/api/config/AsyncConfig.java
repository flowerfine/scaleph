package cn.sliew.scaleph.api.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * thread pool config
 *
 * @author gleiyu
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig {

    private ThreadPoolPropConfig config;

    @Autowired
    public void setConfig(ThreadPoolPropConfig config) {
        this.config = config;
    }

    @Bean
    public Executor asyncExecutor() {
        log.info("启动异步线程池");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setThreadNamePrefix("async-executor-");
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "thread.task.pool")
    public static class ThreadPoolPropConfig {
        /**
         * 核心线程池大小
         */
        private int corePoolSize;
        /**
         * 线程池最大大小
         */
        private int maxPoolSize;
        /**
         * 活跃时间
         */
        private int keepAliveSeconds;
        /**
         * 队列大小
         */
        private int queueCapacity;
    }


}
