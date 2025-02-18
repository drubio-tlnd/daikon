package org.talend.daikon.spring.auth.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ExceptionHandlingTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionHandlingTestApplication.class, args);
    }

    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
    public class CustomWebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

        @Autowired
        private TalendBearerTokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

        @Bean("securityFilterChain.oauth2Configuration")
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests().antMatchers("/auth/**").authenticated().and().oauth2ResourceServer()
                    .authenticationManagerResolver(authenticationManagerResolver)
                    .authenticationEntryPoint(tokenAuthenticationEntryPoint).and().authorizeRequests().anyRequest().permitAll();
            return http.build();
        }
    }

    @RestController
    public static class SampleEndpoint {

        @GetMapping("/auth/test")
        @PreAuthorize("hasAuthority('VIEW')")
        public String authGet() {
            return "auth-result";
        }

        @GetMapping("/no-auth/test")
        public String noAuthGet() {
            return "no-auth-result";
        }
    }
}
