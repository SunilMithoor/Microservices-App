package com.app.client;

import com.app.client.model.dto.user.payload.response.Users;
import com.app.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(
        name = "user-service",
        path = "/api/v1/users", // Specify the base path for the user-service
        configuration = FeignClientConfig.class)
public interface UserClient {

    @GetMapping()
    List<Users> users();
    //create a proxy for the UserClient
    //RestTemplate -> build the request
    // URL , GET , NO , List<User>

    @GetMapping("/{id}")
    Users user(@PathVariable int id);

}
