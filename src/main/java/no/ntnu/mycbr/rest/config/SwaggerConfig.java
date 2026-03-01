package no.ntnu.mycbr.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by kerstin on 02/10/16.
 *
 * OpenAPI configuration for springdoc.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("myCBR RESTful API documentation")
                        .description("This page documents RESTful Web Service endpoints used to create Case-Based Reasoning services with the myCBR SDK.")
                        .version("2.1")
                        .termsOfService("http://mycbr-project.org/download.html")
                        .contact(new Contact()
                                .name("myCBR Team")
                                .url("http://mycbr-project.org/")
                                .email("https://github.com/ntnu-ai-lab/mycbr-rest/"))
                        .license(new License()
                                .name("LGPL")
                                .url("https://opensource.org/licenses/gpl-3.0.html")));
    }
}
