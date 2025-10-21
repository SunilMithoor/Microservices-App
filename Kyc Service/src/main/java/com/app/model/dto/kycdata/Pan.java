package com.app.model.dto.kycdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pan{
    @JsonProperty("result")
	private Result result;
    @JsonProperty("image")
	private Image image;
    @JsonProperty("success")
	private boolean success;
    @JsonProperty("status")
	private String status;
}
