package com.app.client.service;

import com.app.client.UserClient;
import com.app.client.model.dto.user.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public Users searchUser(int id) {
        return userClient.user(id);
    }

    public List<Users> getAllUsers() {
        return userClient.users();
    }


}
