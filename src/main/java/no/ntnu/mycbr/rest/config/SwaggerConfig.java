package no.ntnu.mycbr.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerstin on 02/10/16.
 *
 * Following the tutorial at http://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket apiDocket() {

        Contact contact = new Contact(
                "myCBR Team",
                "http://mycbr-project.org/",
                "https://github.com/ntnu-ai-lab/mycbr-rest/"
        );

        List<VendorExtension> vendorExtensions = new ArrayList<>();

        ApiInfo apiInfo = new ApiInfo(
                "myCBR RESTful API documentation",
                "This pages documents RESTful Web Service endpoints to be used to create Case-Based Reasoning services. The RESTful API uses the myCBR SDK.", "1.0",
                "http://mycbr-project.org/download.html", contact,
                "LGPL", "https://opensource.org/licenses/gpl-3.0.html",vendorExtensions);

        Docket docket =  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("no.ntnu.mycbr.rest"))
                .paths(PathSelectors.any())
                .build();

        return docket;

    }
}