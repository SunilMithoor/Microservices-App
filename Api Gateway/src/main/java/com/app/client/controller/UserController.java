package com.app.client.controller;

import com.app.client.model.dto.user.payload.response.Users;
import com.app.client.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/{id}")
    public Users search(@PathVariable int id) {
        return userService.searchUser(id);
    }

    @GetMapping()
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

}
