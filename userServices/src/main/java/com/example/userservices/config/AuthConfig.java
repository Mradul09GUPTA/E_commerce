package com.example.userservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class AuthConfig {
    @Bean
    public BCryptPasswordEncoder  getBCryptPasswordEncoder() {
        return new  BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain getFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable(); //In case you deploy service on cloud, you might not need this line
        httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return httpSecurity.build();
    }
}
