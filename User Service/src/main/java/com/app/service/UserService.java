package com.app.service;

import com.app.model.dto.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<Users> usersList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new ClassPathResource("users.json").getInputStream()) {
            Users[] users = mapper.readValue(is, Users[].class);
            usersList = new ArrayList<>(Arrays.asList(users));
        } catch (IOException e) {
            e.printStackTrace(); // In real use, log it properly
        }
    }

    public List<Users> getAllUsers() {
        return usersList;
    }

    public Optional<Users> getUsersById(int id) {
        return usersList.stream()
                .filter(users -> users.getId() == id)
                .findFirst();
    }


}
