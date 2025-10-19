//package com.app.handler;
//
//import com.app.exception.UserServiceException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//
//public class CustomErrorDecoder implements ErrorDecoder {
//
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        String message = "Unknown error";
//
//        try {
//            if (response.body() != null) {
//                message = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
//            }
//        } catch (IOException e) {
//            message = "Error reading response body";
//        }
//
//        return switch (response.status()) {
//            case 400 -> new UserServiceException("Bad Request from user service: " + message);
//            case 500 -> new UserServiceException("Internal Server Error from user service: " + message);
//            case 503 -> new UserServiceException("User service unavailable: " + message);
//            default -> new Exception("Unexpected error: " + message);
//        };
//    }
//}
