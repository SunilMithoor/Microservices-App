package com.app.security.services;


import com.app.model.entity.User;
import com.app.model.dto.payload.request.RegisterUser;

import java.util.Optional;


public interface AuthenticationService {

    Optional<User> getUserData(String emailId);

    Optional<User> findByEmailIdOrMobileNoOrUserName(String input);

    Optional<User> saveUser(RegisterUser registerUser);
}