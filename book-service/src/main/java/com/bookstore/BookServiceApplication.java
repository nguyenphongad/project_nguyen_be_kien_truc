package com.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.bookstore", 
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX, 
            pattern = "com\\.bookstore\\.configs\\.AppConfig"
        ),
    })
public class BookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }

}