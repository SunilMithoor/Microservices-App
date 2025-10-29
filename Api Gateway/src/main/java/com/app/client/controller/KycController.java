package com.app.client.controller;

import com.app.client.model.dto.kyc.payload.response.KycResponseItem;
import com.app.client.service.KycService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kyc")
public class KycController {

    private final KycService kycService;

    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping("/search/{id}")
    public KycResponseItem search(@PathVariable int id) {
        return kycService.searchKyc(id);
    }

    @GetMapping("")
    public List<KycResponseItem> getAllKyc() {
        return kycService.getAllKyc();
    }

}
