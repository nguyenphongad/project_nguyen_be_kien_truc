package bookstore.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Cho phép tất cả các nguồn cụ thể
        config.setAllowCredentials(true);
        // KHÔNG sử dụng * với allowCredentials=true
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3333"));
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        
        // Cho phép tất cả các header
        config.addAllowedHeader("*");
        
        // Cho phép tất cả các phương thức HTTP
        config.addAllowedMethod("*");
        
        // Thêm header cho preflight requests
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); // 1 giờ
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
