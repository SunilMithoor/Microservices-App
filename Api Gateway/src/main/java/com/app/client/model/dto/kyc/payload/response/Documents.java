package com.app.client.model.dto.kyc.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documents{
    @JsonProperty("pan")
	private Pan pan;
    @JsonProperty("aadhar")
	private Aadhar aadhar;
}
