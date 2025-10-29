package com.app.client.model.dto.kyc.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture{
    @JsonProperty("expires")
	private int expires;
    @JsonProperty("url")
	private String url;
}
