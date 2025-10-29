package com.app.client.model.dto.auth.payload.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("jwt_token")
    private String token;
    @JsonProperty("type")
    private String type = "Bearer";
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email_id")
    private String email;
    @JsonProperty("role")
    private String role;


//    @JsonProperty("country_code")
//    private String countryCode;
//    @JsonProperty("mobile_no")
//    private String mobileNo;



}
