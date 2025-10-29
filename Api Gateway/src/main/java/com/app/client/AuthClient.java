package com.app.client;

import com.app.client.model.dto.auth.payload.request.LoginUser;
import com.app.client.model.dto.auth.payload.request.RegisterUser;
import com.app.client.model.dto.auth.payload.request.TokenRefreshRequest;
import com.app.client.model.dto.auth.payload.response.TokenRefreshData;
import com.app.client.model.dto.auth.payload.response.TokenRefreshResponse;
import com.app.client.model.dto.auth.payload.response.UserDataResponse;
import com.app.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "auth-service",
        path = "/api/v1/auth", // Specify the base path for the user-service
        configuration = FeignClientConfig.class)
public interface AuthClient {

    @GetMapping("/hello")
    String checkServer();

    @PostMapping("/signin")
    UserDataResponse signin(@RequestBody LoginUser loginUser);

    @PostMapping("/signup")
    UserDataResponse signup(@RequestBody RegisterUser registerUser);

    @PostMapping("/refreshtoken")
    TokenRefreshResponse refreshtoken(@RequestBody TokenRefreshRequest request);

    @PostMapping("/logout")
    TokenRefreshResponse logout(@RequestBody TokenRefreshRequest request);

}
