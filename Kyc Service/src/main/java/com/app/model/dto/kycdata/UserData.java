package com.app.model.dto.kycdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    @JsonProperty("contact_no")
    private String contactNo;
    @JsonProperty("name")
    private String name;
    @JsonProperty("serial_number")
    private String serialNumber;
}
