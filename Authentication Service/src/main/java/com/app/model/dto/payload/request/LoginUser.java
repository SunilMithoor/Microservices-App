package com.app.model.dto.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.app.utils.MessageConstants.*;


@Getter
@Setter
@ToString
@Data
public class LoginUser {

    @NotBlank(message = USER_NAME_REQUIRED)
    @Size(min = 5, max = 50, message = USER_NAME_LENGTH)
    @JsonProperty("username")
    private String username;

    @Size(max = 255, message = PASSWORD_MAX_LENGTH)
    @NotBlank(message = PASSWORD_REQUIRED)
    @JsonProperty("password")
    private String password;

}