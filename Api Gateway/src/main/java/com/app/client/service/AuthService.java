package com.app.client.service;

import com.app.client.AuthClient;
import com.app.client.model.dto.auth.payload.request.LoginUser;
import com.app.client.model.dto.auth.payload.request.RegisterUser;
import com.app.client.model.dto.auth.payload.request.TokenRefreshRequest;
import com.app.client.model.dto.auth.payload.response.TokenRefreshResponse;
import com.app.client.model.dto.auth.payload.response.UserDataResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthClient authClient;

    public AuthService(AuthClient authClient) {
        this.authClient = authClient;
    }

    public UserDataResponse signIn(LoginUser loginUser) {
        return authClient.signin(loginUser);
    }

    public UserDataResponse signUp(RegisterUser registerUser) {
        return authClient.signup(registerUser);
    }

    public TokenRefreshResponse refreshtoken(TokenRefreshRequest request) {
        return authClient.refreshtoken(request);
    }

    public TokenRefreshResponse logout(TokenRefreshRequest request) {
        return authClient.logout(request);
    }

    public String checkServer() {
        return authClient.checkServer();
    }

}
