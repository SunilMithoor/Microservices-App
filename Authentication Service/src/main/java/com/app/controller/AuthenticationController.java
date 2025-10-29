package com.app.controller;

import com.app.config.LoggerService;
import com.app.exception.common.BaseRunTimeException;
import com.app.exception.custom.InvalidParamException;
import com.app.exception.custom.ServiceException;
import com.app.exception.custom.TokenRefreshException;
import com.app.exception.custom.UserAlreadyExistException;
import com.app.model.common.ResponseHandler;
import com.app.model.dto.payload.request.LoginUser;
import com.app.model.dto.payload.request.RegisterUser;
import com.app.model.dto.payload.request.TokenRefreshRequest;
import com.app.model.dto.payload.response.TokenRefreshResponse;
import com.app.model.dto.payload.response.UserData;
import com.app.model.entity.RefreshToken;
import com.app.model.entity.User;
import com.app.security.jwt.JwtUtil;
import com.app.security.services.AuthenticationServiceImpl;
import com.app.security.services.RefreshTokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.app.utils.Utils.tagMethodName;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private static final String TAG = "AuthenticationController";
    private final AuthenticationServiceImpl authenticationService;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final LoggerService logger;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationServiceImpl authenticationService,
                                    LoggerService logger, JwtUtil jwtUtil,
                                    RefreshTokenServiceImpl refreshTokenService
    ) {
        this.authenticationService = authenticationService;
        this.logger = logger;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/hello")
    public String serverStatus() {
        return "Hello from auth service";
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(
            @Valid @RequestBody LoginUser loginUser,
            BindingResult result) throws BaseRunTimeException {

        String methodName = "authenticateUser";
        logger.request(tagMethodName(TAG, methodName), loginUser);

        // Step 1: Validate input
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            logger.warn(tagMethodName(TAG, methodName), "Validation failed: " + errorMessages);
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }

        // Step 2: Find user
        Optional<User> user = authenticationService.findByEmailIdOrMobileNoOrUserName(loginUser.getUsername());
        if (user.isEmpty()) {
            return ResponseHandler.failure(HttpStatus.NOT_FOUND, "User not found");
        }

        // Step 3: Authenticate credentials
        boolean isAuthenticated = authenticationService.authenticate(loginUser.getUsername(), loginUser.getPassword());
        if (isAuthenticated) {
            // Step 4: Generate JWT token
            String jwtToken = jwtUtil.generateToken(user.get());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get().getId());

            // Step 5: Build response
            UserData loginResponse = new UserData();
            loginResponse.setId(user.get().getId());
            loginResponse.setToken(jwtToken);
            loginResponse.setRefreshToken(refreshToken.getToken());
            loginResponse.setUsername(user.get().getUsername());
            loginResponse.setEmail(user.get().getEmailId());
            loginResponse.setRole(String.valueOf(user.get().getRole()));

            logger.response(tagMethodName(TAG, methodName), "Login success for " + loginUser.getUsername());
            return ResponseHandler.success(HttpStatus.OK, loginResponse, "Login successful");
        } else {
            return ResponseHandler.failure(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> saveUser(@Valid @RequestBody RegisterUser registerUser, BindingResult result) {
        String methodName = "saveUser";
        logger.request(tagMethodName(TAG, methodName), registerUser);
        // Step 1: Validate input
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            logger.warn(tagMethodName(TAG, methodName), "Validation failed: " + errorMessages);
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }

        try {
            // Step 2: Save user
            Optional<User> savedUser = authenticationService.saveUser(registerUser);
            if (savedUser.isEmpty()) {
                throw new ServiceException("User registration failed");
            }

            // Step 3: Generate JWT token
            String jwtToken = jwtUtil.generateToken(savedUser.get());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.get().getId());

            // Step 4: Build response
            UserData response = new UserData();
            response.setToken(jwtToken);
            response.setRefreshToken(refreshToken.getToken());
            response.setId(savedUser.get().getId());
            response.setUsername(savedUser.get().getUsername());
            response.setEmail(savedUser.get().getEmailId());
            response.setRole(String.valueOf(savedUser.get().getRole()));

            logger.response(tagMethodName(TAG, methodName), "User registered successfully: " + savedUser.get().getUsername());
            return ResponseHandler.success(HttpStatus.OK, response, "User registered successfully");

        } catch (UserAlreadyExistException e) {
            return ResponseHandler.failure(HttpStatus.CONFLICT, "User already exists");
        } catch (InvalidParamException e) {
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unexpected error occurred while saving user", e);
            return ResponseHandler.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<Object> refreshtoken(@Valid @RequestBody TokenRefreshRequest request, BindingResult result) {
        String methodName = "refreshtoken";
        logger.request(tagMethodName(TAG, methodName), request);

        // Step 1: Validate input
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            logger.warn(tagMethodName(TAG, methodName), "Validation failed: " + errorMessages);
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }

        try {
            return refreshTokenService.findByToken(request.getRefreshToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        String token = jwtUtil.generateToken(user);
                        TokenRefreshResponse response = new TokenRefreshResponse();
                        response.setAccessToken(token);
                        response.setRefreshToken(request.getRefreshToken());
                        return ResponseHandler.success(HttpStatus.OK, response, "Token refreshed successfully");
                    })
                    .orElseThrow(() -> new TokenRefreshException(request.getRefreshToken(),
                            "Refresh token is not in database!"));
        } catch (TokenRefreshException e) {
            logger.warn(tagMethodName(TAG, methodName), "Invalid refresh token: " + e.getMessage());
            return ResponseHandler.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Error occurred while refreshing token", e);
            return ResponseHandler.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while refreshing token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logoutUser(@Valid @RequestBody TokenRefreshRequest request, BindingResult result) {
        String methodName = "logoutUser";
        logger.request(tagMethodName(TAG, methodName), request);

        // Step 1: Validate input
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            logger.warn(tagMethodName(TAG, methodName), "Validation failed: " + errorMessages);
            return ResponseHandler.failure(HttpStatus.BAD_REQUEST, String.valueOf(errorMessages));
        }
        try {
            return refreshTokenService.findByToken(request.getRefreshToken())
                    .map(token -> {
                        refreshTokenService.deleteToken(token);
                        return ResponseHandler.success(HttpStatus.OK, null, "Logged out successfully.");
                    })
                    .orElseThrow(() -> new TokenRefreshException(request.getRefreshToken(),
                            "Invalid refresh token."));
        } catch (TokenRefreshException e) {
            logger.warn(tagMethodName(TAG, methodName), "Invalid refresh token: " + e.getMessage());
            return ResponseHandler.failure(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Error occurred while refreshing token", e);
            return ResponseHandler.failure(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while refreshing token");
        }
    }

}
