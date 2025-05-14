package bookstore.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8001");
        server.setDescription("User Management REST API Documentation");

        Info information = new Info()
                .title("User Management REST API Documentation")
                .version("1.0")
                .description("This API exposes endpoints to manage users.");

        return new OpenAPI().info(information).servers(List.of(server));
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api_user")
                .pathsToMatch("bookstore.userservice.controllers")
                .build();
    }

    // Thêm cấu hình CORS vào đây
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Hoặc cụ thể: "http://localhost:3000"
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}

//http://localhost:8001/swagger-ui/index.html
//http://localhost:8001/v3/api-docs
