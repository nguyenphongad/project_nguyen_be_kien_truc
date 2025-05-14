package com.bookstore.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3333") // Liệt kê cụ thể các origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Liệt kê cụ thể các origins được phép thay vì dùng *
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3333"));
        
        // Hoặc sử dụng allowedOriginPatterns thay vì allowedOrigins nếu muốn dùng pattern
        // config.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Cho phép tất cả các headers
        config.addAllowedHeader("*");
        
        // Cho phép tất cả các methods
        config.addAllowedMethod("*");
        
        // Cho phép credentials
        config.setAllowCredentials(true);
        
        // Đặt max age
        config.setMaxAge(3600L);
        
        // Áp dụng cấu hình cho tất cả các endpoints
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
