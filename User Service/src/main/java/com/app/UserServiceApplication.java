package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        System.out.println("User Service started");
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
