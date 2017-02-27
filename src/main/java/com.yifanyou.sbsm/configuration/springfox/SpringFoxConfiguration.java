package com.yifanyou.sbsm.configuration.springfox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by yifanyou on 16/12/4.
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfiguration {

    @Bean
    public Docket imscRestDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .securitySchemes(newArrayList(apiKey()))
//                .securityContexts(newArrayList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
//                .paths(input -> !PathSelectors.regex("/api-v1/.*").apply(input))
                .build()
                .groupName("test");
    }

    private ApiKey apiKey() {
        return new ApiKey("X-Access-Token", "X-Access-Token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(path -> !PathSelectors.regex("/security/.*").apply(path))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference("X-Access-Token", authorizationScopes));
    }

}
