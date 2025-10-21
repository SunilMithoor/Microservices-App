package com.app.service;

import com.app.model.dto.kycdata.KycResponseItem;
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
public class KycService {

    private List<KycResponseItem> kycDataList = new ArrayList<>();

    @PostConstruct
    public void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = new ClassPathResource("kyc.json").getInputStream()) {
            KycResponseItem[] users = mapper.readValue(is, KycResponseItem[].class);
            kycDataList = new ArrayList<>(Arrays.asList(users));
        } catch (IOException e) {
            e.printStackTrace(); // In real use, log it properly
        }
    }

    public List<KycResponseItem> getAllKyc() {
        return kycDataList;
    }

    public Optional<KycResponseItem> getKycDataById(int id) {
        return kycDataList.stream()
                .filter(kyc -> kyc.getId() == id)
                .findFirst();
    }


}
