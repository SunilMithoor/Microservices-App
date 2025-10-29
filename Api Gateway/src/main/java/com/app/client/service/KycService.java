package com.app.client.service;

import com.app.client.KycClient;
import com.app.client.model.dto.kyc.payload.response.KycResponseItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycService {

    private final KycClient kycClient;

    public KycService(KycClient kycClient) {
        this.kycClient = kycClient;
    }

    public KycResponseItem searchKyc(int id) {
        return kycClient.kyc(id);
    }

    public List<KycResponseItem> getAllKyc() {
        return kycClient.kycs();
    }


}
