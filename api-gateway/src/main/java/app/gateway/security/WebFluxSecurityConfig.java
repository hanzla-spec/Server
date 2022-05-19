package app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable();
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/**","/private/**").permitAll()
                        .anyExchange().authenticated()
                );
        http
                .httpBasic();
        return http.build();
    }
}
