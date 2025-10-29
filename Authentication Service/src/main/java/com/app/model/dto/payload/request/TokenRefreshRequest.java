package com.app.model.dto.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.app.utils.MessageConstants.*;


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
