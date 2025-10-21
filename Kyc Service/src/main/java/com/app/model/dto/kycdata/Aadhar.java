package com.app.model.dto.kycdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aadhar {
    @JsonProperty("result")
    private Result result;
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("back")
    private Back back;
    @JsonProperty("front")
    private Front front;
    @JsonProperty("status")
    private String status;
}

