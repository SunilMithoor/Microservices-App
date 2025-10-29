package com.app.service;


import com.app.model.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UserService extends UserDetails {

    User getUserById(Long id);

    User getUserByEmailId(String emailId);

    boolean isEmailExists(String email);

    boolean isMobileExists(String mobileNo);

    User saveUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();
}
