package com.example.holiday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Configuration
public class HolidayApiSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(HolidayApiSpringBootApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
