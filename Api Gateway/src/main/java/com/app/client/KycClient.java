package com.app.client;

import com.app.client.model.dto.kyc.payload.response.KycResponseItem;
import com.app.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "kyc-service",
        path = "/api/v1/kyc", // Specify the base path for the user-service
        configuration = FeignClientConfig.class)
public interface KycClient {

    @GetMapping
    List<KycResponseItem> kycs();
    //create a proxy for the UserClient
    //RestTemplate -> build the request
    // URL , GET , NO , List<User>

    @GetMapping("/{id}")
    KycResponseItem kyc(@PathVariable int id);

}