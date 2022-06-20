package app.gateway.config;

import app.gateway.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                                .route("auth-service", r -> r.path("/api/v1/auth/**","/private/v1/auth/**","/private/v1/email/**").
                                        filters(f -> f.filter(filter)).uri("http://localhost:8083"))
                                .route("question-service",r -> r.path("/api/v1/question/**","/private/v1/question/**","/api/v1/answer/**",
                                                "/private/v1/answer/**", "/api/v1/comment/**", "/private/v1/comment/**").
                                        filters(f -> f.filter(filter)).uri("http://localhost:8082"))

                .build();
    }

}