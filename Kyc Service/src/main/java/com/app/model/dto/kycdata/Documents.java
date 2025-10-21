package com.app.model.dto.kycdata;

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
