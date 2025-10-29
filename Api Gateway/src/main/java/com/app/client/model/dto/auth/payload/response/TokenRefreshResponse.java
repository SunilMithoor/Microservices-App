package com.app.client.model.dto.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {
    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("data")
    private TokenRefreshData data;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("timestamp")
    private String timestamp;

}