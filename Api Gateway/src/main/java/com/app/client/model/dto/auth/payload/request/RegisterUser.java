package com.app.client.model.dto.auth.payload.request;


import com.app.client.model.enums.auth.ERole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

import static com.app.client.utils.auth.MessageConstants.*;


@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {

    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = FIRST_NAME_REQUIRED)
    @Size(min = 3, max = 50, message = FIRST_NAME_LENGTH)
    @JsonProperty("first_name")
    private String firstName;

    @Size(max = 50, message = LAST_NAME_MAX_LENGTH)
    @JsonProperty("last_name")
    private String lastName;

    @Size(min = 5, max = 50, message = USER_NAME_LENGTH)
    @NotBlank(message = USER_NAME_REQUIRED)
    @JsonProperty("username")
    private String username;

    @Email(message = EMAIL_INVALID_FORMAT)
    @Size(max = 50, message = EMAIL_MAX_LENGTH)
    @Pattern(
            regexp = "^(|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6})$",
            message = EMAIL_INVALID_FORMAT
    )
    @JsonProperty("email_id")
    private String emailId;

    @JsonProperty(value = "is_email_id_verified", access = JsonProperty.Access.READ_ONLY)
    private Boolean isEmailIdVerified = false;

    @NotBlank(message = COUNTRY_CODE_REQUIRED)
    @Pattern(regexp = "\\d{1,5}", message = COUNTRY_CODE_MAX_LENGTH_5)
    @JsonProperty("country_code")
    private String countryCode = "91";

    @NotBlank(message = MOBILE_NUMBER_REQUIRED)
    @Size(min = 10, max = 15, message = MOBILE_NUMBER_MAX_LENGTH)
    @Pattern(regexp = "\\d{10,15}", message = MOBILE_NUMBER_INVALID_FORMAT)
    @JsonProperty("mobile_no")
    private String mobileNo;

    @JsonProperty(value = "is_mobile_no_verified", access = JsonProperty.Access.READ_ONLY)
    private Boolean isMobileNoVerified = false;

    @Size(min = 5, max = 50, message = PASSWORD_MAX_LENGTH)
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$",
            message = PASSWORD_REGEX
    )
    @JsonProperty("password")
    private String password;

    @Size(max = 255, message = PASSWORD_HASH_MAX_LENGTH)
    @JsonProperty(value = "password_hash", access = JsonProperty.Access.READ_ONLY)
    private String passwordHash;

    @PastOrPresent(message = DATE_OF_BIRTH_INVALID)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("role")
    private ERole role = ERole.USER;

    @JsonProperty(value = "is_active", access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive = true;
}

