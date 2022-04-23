package com.liyu.breeze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * main class
 *
 * @author gleiyu
 */
@EnableAsync
@EnableCaching
@SpringBootApplication
public class BreezeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BreezeApplication.class, args);
    }
}
