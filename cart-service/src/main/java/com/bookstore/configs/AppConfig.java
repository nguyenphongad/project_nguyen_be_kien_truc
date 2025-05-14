/*
 * @ (#) AppConfig.java    1.0    22/04/2025
 * Copyright (c) 2025 IUH. All rights reserved.
 */
package com.bookstore.configs;/*
 * @description:
 * @author: Bao Thong
 * @date: 22/04/2025
 * @version: 1.0
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class AppConfig {

    @Value("${book.service.url}")
    private String bookServiceUrl;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }
    
    @Bean(name = "bookServiceUrl")
    public String bookServiceUrl() {
        return bookServiceUrl;
    }
    
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
