package com.app.client.controller;

import com.app.client.model.common.ResponseHandler;
import com.app.client.model.dto.auth.payload.request.LoginUser;
import com.app.client.model.dto.auth.payload.request.RegisterUser;
import com.app.client.model.dto.auth.payload.request.TokenRefreshRequest;
import com.app.client.model.dto.auth.payload.response.TokenRefreshResponse;
import com.app.client.model.dto.auth.payload.response.UserDataResponse;
import com.app.client.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello")
    String checkServer() {
        return authService.checkServer();
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@Valid @RequestBody LoginUser loginUser, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }
        try {
            UserDataResponse data = authService.signIn(loginUser);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseHandler.failure(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody RegisterUser registerUser, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }
        try {
            UserDataResponse data = authService.signUp(registerUser);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseHandler.failure(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<Object> refreshtoken(@RequestBody TokenRefreshRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }
        try {
            TokenRefreshResponse data = authService.refreshtoken(request);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseHandler.failure(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody TokenRefreshRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }
        try {
            TokenRefreshResponse data = authService.logout(request);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseHandler.failure(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}