package com.msintro.gateway_service;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringCloudConfig {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("resources",
                        r -> r.path("/resources/**")
                        .uri("lb://RESOURCE-SERVICE"))
                .route("songs",
                        r -> r.path("/songs/**")
                                .uri("lb://SONG-SERVICE"))
                .build();
    }
}
