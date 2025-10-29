package com.app.controller;

import com.app.model.dto.Users;
import com.app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUsersById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<String> saveuser(@RequestBody Users  users) {
        return ResponseEntity.ok("User registered successfully!");
    }
}
