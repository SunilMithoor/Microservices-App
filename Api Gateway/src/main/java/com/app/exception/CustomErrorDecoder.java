package com.app.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        String message = extractResponseBody(response);
        HttpStatus status = HttpStatus.resolve(response.status());

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        log.error("[{}] Downstream service returned {}: {}", methodKey, status, message);

        return new DownstreamServiceException(status, message);
    }

    private String extractResponseBody(Response response) {
        try {
            if (response.body() != null) {
                return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Failed to read Feign response body", e);
        }
        return "{\"status\":\"failure\",\"message\":\"Error reading downstream response\"}";
    }
}


