package com.app.controller;

import com.app.model.dto.kycdata.KycResponseItem;
import com.app.service.KycService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/kyc")
public class KycController {

    private final KycService kycService;

    public KycController(KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping
    public ResponseEntity<List<KycResponseItem>> getAllKyc() {
        return ResponseEntity.ok(kycService.getAllKyc());
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<KycResponseItem>> getKycDataById(@PathVariable int id) {
        return ResponseEntity.ok(kycService.getKycDataById(id));
    }
}
