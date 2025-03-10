package com.example.employee.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Bean("apiConsumir")
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
