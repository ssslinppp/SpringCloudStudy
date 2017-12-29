package com.ssslinppp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SleuthServerAApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleuthServerAApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(3_000);
        httpRequestFactory.setConnectTimeout(3_000);
        httpRequestFactory.setReadTimeout(3_000);

        return new RestTemplate(httpRequestFactory);
    }

    /**
     * 设置采样频率，也可以使用配置文件的方式设置采样频率:
     * {@code spring.sleuth.sampler.percentage=10% }
     *
     * @return
     */
    @Bean
    public AlwaysSampler defaultSampler() {
        return new AlwaysSampler();
    }
}
