package com.cerclex.epr.apiGateway.config;

import com.cerclex.epr.apiGateway.filters.AuthenticationPrefilter;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cerclex.epr.apiGateway.filters.AuthorizationFilter;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder,
            AuthenticationPrefilter authFilter) {
        return builder.routes()
                .route("authentication-service-route", r -> r.path("/authentication-service/**")
                        .filters(f ->
                                f.rewritePath("/authentication-service(?<segment>/?.*)", "$\\{segment}")
                                        .filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                        .uri("lb://authentication-service"))
                .route("management-route", r -> r.path("/management/**")
                        .filters(f ->
                                        f.filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                        .uri("lb://management-service"))
                .route("delivery-route", r -> r.path("/delivery/**")
                        .filters(f ->
                                f.filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                        .uri("lb://delivery-service"))
                .route("target-route", r -> r.path("/target/**")
                        .filters(f ->
                                f.filter(authFilter.apply(
                                        new AuthenticationPrefilter.Config())))
                        .uri("lb://target-service"))
                .route("attachment-route", r -> r.path("/attachment/**")
                        .filters(f ->
                                f.filter(authFilter.apply(
                                        new AuthenticationPrefilter.Config())))
                        .uri("lb://attachment-service"))
                .route("invoice-route", r -> r.path("/invoice/**")
                        .filters(f ->
                                f.filter(authFilter.apply(
                                        new AuthenticationPrefilter.Config())))
                        .uri("lb://invoice-service"))
                .route("api-gateway-route", r-> r.path("/v3/api-docs/**")
                        .filters(f ->
                                f.rewritePath("/v3/api-docs/(?<path>.*)","/$\\{path}/v3/api-docs")
                                        .filter(authFilter.apply(
                                                new AuthenticationPrefilter.Config())))
                                .uri("lb://api-gateway"))
                .build();
    }

}