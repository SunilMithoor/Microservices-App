package com.app.exception;

import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<String> handleBadRequest(FeignException.BadRequest ex) {
        return ResponseEntity.badRequest()
                .body(ex.contentUTF8());
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    public ResponseEntity<String> handleServerError(FeignException.InternalServerError ex) {
        return ResponseEntity.internalServerError()
                .body(ex.contentUTF8());
    }

    @ExceptionHandler(FeignException.ServiceUnavailable.class)
    public ResponseEntity<String> handleServiceUnavailable(FeignException.ServiceUnavailable ex) {
        return ResponseEntity.internalServerError()
                .body(ex.contentUTF8());
    }

    @ExceptionHandler(RetryableException.class)
    public ResponseEntity<String> handleRetryableException(RetryableException ex) {
        return ResponseEntity.internalServerError()
                .body(ex.contentUTF8());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        int status = ex.status();
        String message = ex.contentUTF8();

        return switch (status) {
            case 400 -> ResponseEntity.badRequest().body(message);
            case 500 -> ResponseEntity.internalServerError().body(message);
            case 503 -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
            default -> ResponseEntity.status(status).body("Unexpected error: " + message);
        };
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ex.getMessage());
    }


    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<Object> handleDownstreamServiceException(DownstreamServiceException ex) {
        log.error("Downstream service error: {}", ex.getResponseBody());
        return buildResponse(ex.getStatus(), ex.getResponseBody());
    }

    /**
     * Helper method to create a consistent error response.
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status_code", status.value());
        errorResponse.put("success", false);
        errorResponse.put("data", null);
        errorResponse.put("message", message);

        return new ResponseEntity<>(errorResponse, status);
    }

}
