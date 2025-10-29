package com.app.client.model.dto.auth.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.app.client.utils.auth.MessageConstants.REFRESH_TOKEN_REQUIRED;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenRefreshRequest {

    @NotBlank(message = REFRESH_TOKEN_REQUIRED)
    @JsonProperty("refresh_token")
    private String refreshToken;

}
