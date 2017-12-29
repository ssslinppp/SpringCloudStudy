package com.ssslinppp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SleuthZipkin2RabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthZipkin2RabbitmqApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
