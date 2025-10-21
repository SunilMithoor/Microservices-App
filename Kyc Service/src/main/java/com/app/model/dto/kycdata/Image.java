package com.app.model.dto.kycdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image{
    @JsonProperty("expires")
	private int expires;
    @JsonProperty("verified")
	private boolean verified;
    @JsonProperty("url")
	private String url;
    @JsonProperty("status")
	private String status;
}
