package com.renato.projects.ecommerce.config;


import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityDebugConfig {

    @Bean
    ApplicationRunner logSecurityFilters(FilterChainProxy proxy) {
        return args -> {
            int chainIndex = 1;
            for (SecurityFilterChain chain : proxy.getFilterChains()) {
                System.out.println("SecurityFilterChain #" + chainIndex++);
                chain.getFilters().forEach(filter ->
                    System.out.println("  - " + filter.getClass().getName())
                );
            }
        };
    }
}