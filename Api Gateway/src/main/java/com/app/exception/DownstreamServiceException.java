package com.app.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DownstreamServiceException extends RuntimeException {

    private final HttpStatus status;
    private final String responseBody;

    public DownstreamServiceException(HttpStatus status, String responseBody) {
        super(responseBody);
        this.status = status;
        this.responseBody = responseBody;
    }

}
