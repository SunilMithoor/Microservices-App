package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiGateWayApplication {

    public static void main(String[] args) {
        System.out.println("Api Gateway started");
        SpringApplication.run(ApiGateWayApplication.class, args);
    }

}
