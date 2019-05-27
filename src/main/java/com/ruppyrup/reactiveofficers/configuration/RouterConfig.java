package com.ruppyrup.reactiveofficers.configuration;

import com.ruppyrup.reactiveofficers.controller.OfficerHander;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> route(OfficerHander handler) {
        return RouterFunctions
                .route(GET("/route/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        handler::getOfficer)
                .andRoute(GET("/route").and(accept(MediaType.APPLICATION_JSON)),
                        handler::listOfficers)
                .andRoute(POST("/route").and(accept(MediaType.APPLICATION_JSON)),
                        handler::createOfficer)
                .andRoute(PUT("/route/{id}").and(accept(MediaType.APPLICATION_JSON)),
                        handler::updateOfficer);
    }
}
