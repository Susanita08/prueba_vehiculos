package com.ing.interview.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.of(plugins));

    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.ing.interview.api.rest"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                ;

    }


    /*@Bean
    public Docket carApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api-cars")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/com.ing.interview.api.rest.*"))
                .build();
    }*/

    /*private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Vehicle Test API Rest")
                .description("API RESTful for the interview ing car.")
                .termsOfServiceUrl("http://en.wikipedia.org/wiki/Terms_of_service")
                .contact(new Contact("Susana Galán", "http://car.com", "susanagalanga30@gmail.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("2.0")
                .build();
    }*/


   private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Order Service API",
                "Order Service API Description",
                "1.0",
                "http://localhost:8080/cars/api/v1",
                new Contact("Cars", "https://cars.com", "apis@cars.com"),
                "LICENSE",
                "LICENSE URL",
                Collections.emptyList()
        );
    }

}