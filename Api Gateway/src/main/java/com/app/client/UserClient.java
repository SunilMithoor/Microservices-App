package com.app.client;

import com.app.client.model.dto.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


//@FeignClient(
//        name = "user-client",
//        url = "${application.services.user-service.url}",
//        configuration = FeignClientConfig.class)

@FeignClient(
        name = "user-client",
        url = "${application.services.user-service.url}")
public interface UserClient {

    @GetMapping
    List<Users> users();
    //create a proxy for the UserClient
    //RestTemplate -> build the request
    // URL , GET , NO , List<User>

    @GetMapping("/{id}")
    Users user(@PathVariable int id);

}