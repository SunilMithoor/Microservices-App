package com.app.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

//    @Bean
//    public RequestInterceptor authInterceptor() {
//        return requestTemplate -> requestTemplate.header("Authorization", "Bearer token");
//    }

//    @Bean
//    public ErrorDecoder errorDecoder() {
//        return new CustomErrorDecoder();
//    }
}
