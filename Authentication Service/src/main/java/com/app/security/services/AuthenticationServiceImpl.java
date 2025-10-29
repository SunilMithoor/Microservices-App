package com.app.security.services;

import com.app.config.LoggerService;
import com.app.exception.custom.InvalidParamException;
import com.app.exception.custom.ServiceException;
import com.app.exception.custom.UserAlreadyExistException;
import com.app.exception.custom.UserNotFoundException;
import com.app.model.dto.payload.request.RegisterUser;
import com.app.model.entity.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.app.utils.Utils.tagMethodName;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String TAG = "AuthenticationService";
    private final UserRepository userRepository;
    private final LoggerService logger;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, LoggerService logger, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.logger = logger;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String name, String rawPassword) {
        String methodName = "authenticate";
        try {
            Optional<User> user = userRepository.findByUserName(name);
            if (user.isEmpty()) {
                throw new UserNotFoundException();
            }
            // Compare entered password with stored Bcrypt hash
            return passwordEncoder.matches(rawPassword, user.get().getPasswordHash());
        }
        catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to get user data ", e);
            return false;
        }
    }

    public Optional<User> getUserData(String emailId) {
        String methodName = "getUserData";
        try {
            logger.request(tagMethodName(TAG, methodName), "User emailId: " + emailId);
            return userRepository.findByEmailId(emailId);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to get user data ", e);
            return Optional.empty();
        }
    }

    public Optional<User> findByEmailIdOrMobileNoOrUserName(String input) {
        String methodName = "findByEmailIdOrMobileNoOrUserName";
        try {
            logger.request(tagMethodName(TAG, methodName), "User findByEmailIdOrMobileNoOrUserName: " + input);
            return userRepository.findByEmailIdOrMobileNoOrUserName(input, input, input);
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Unable to get user data ", e);
            return Optional.empty();
        }
    }


    public Optional<User> saveUser(RegisterUser registerUser) {
        String methodName = "saveUser";
        try {
            logger.request(tagMethodName(TAG, methodName), registerUser);

//            // Check for existing user
            Optional<User> existingUser = userRepository.findByUserName(
                    registerUser.getUsername()
            );
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistException();
            }

            // Convert DTO → Entity and save
            User userEntity = new User();
            userEntity.setEmailId(registerUser.getEmailId());
            userEntity.setPassword(passwordEncoder.encode(registerUser.getPassword()));
            userEntity.setFirstName(registerUser.getFirstName());
            userEntity.setLastName(registerUser.getLastName());
            userEntity.setUserName(registerUser.getUsername());
            userEntity.setMobileNo(registerUser.getMobileNo());
            userEntity.setCountryCode(registerUser.getCountryCode());
            userEntity.setDateOfBirth(registerUser.getDateOfBirth());
            userEntity.setRole(registerUser.getRole());

            User savedUser = userRepository.save(userEntity);
            logger.response(tagMethodName(TAG, methodName), "User saved successfully: " + savedUser.getEmailId());
            return Optional.of(savedUser); // or map back to DTO

        } catch (UserAlreadyExistException | InvalidParamException e) {
            throw e;
        } catch (Exception e) {
            logger.error(tagMethodName(TAG, methodName), "Error during user registration", e);
            throw new ServiceException("Internal server error");
        }
    }


}
