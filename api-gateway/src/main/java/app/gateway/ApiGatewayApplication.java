package app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }

//    @EnableWebSecurity
//    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .csrf().ignoringAntMatchers("/swagger-ui.html**") // ignoring cross site request for eureka/**
//                    .and().authorizeRequests(
//                            authorizeRequests ->
//                                    authorizeRequests
//                                            .antMatchers("/api/**").permitAll());
//
//            super.configure(http);
//        }
//    }
}
