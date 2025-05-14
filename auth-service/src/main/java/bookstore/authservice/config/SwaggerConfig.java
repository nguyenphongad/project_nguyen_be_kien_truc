package bookstore.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    @Primary
    public OpenAPI authServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl(contextPath);
        localServer.setDescription("Local Environment");

        Contact contact = new Contact()
                .name("BookStore Authentication Service")
                .email("support@bookstore.com")
                .url("https://bookstore.com");

        License license = new License()
                .name("Apache License, Version 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0");

        Info info = new Info()
                .title("BookStore Authentication Service API")
                .version("1.0.0")
                .contact(contact)
                .description("API for BookStore Authentication Service")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}
