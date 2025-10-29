package com.app;


import com.app.config.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

import static com.app.utils.Utils.tagMethodName;

@SpringBootApplication
@ComponentScan(basePackages = "com.app")
public class AuthenticationServiceApplication implements CommandLineRunner {

    private static final String TAG = "AuthenticationServiceApplication";
    private final LoggerService loggerService;

    @Autowired
    public AuthenticationServiceApplication(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String methodName = "application main";
        loggerService.info(tagMethodName(TAG, methodName),
                "Auth Service Started: " + new Date(System.currentTimeMillis()));
    }
}