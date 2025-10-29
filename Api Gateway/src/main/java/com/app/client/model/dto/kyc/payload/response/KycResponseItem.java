package com.app.client.model.dto.kyc.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class KycResponseItem {
    @JsonProperty("user_data")
    private UserData userData;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("id")
    private int id;
    @JsonProperty("customer_id")
    private String customerId;
    @JsonProperty("kyc_data")
    private KycData kycData;
    @JsonProperty("status")
    private String status;
}
