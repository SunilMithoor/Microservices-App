package com.app.client.model.dto.kyc.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result{
    @JsonProperty("first_name")
	private String firstName;
    @JsonProperty("last_name")
	private String lastName;
    @JsonProperty("reference_id")
	private String referenceId;
    @JsonProperty("dob")
	private String dob;
    @JsonProperty("name")
	private String name;
    @JsonProperty("middle_name")
	private String middleName;
    @JsonProperty("pan_status")
	private String panStatus;
    @JsonProperty("pincoce")
	private int pincoce;
    @JsonProperty("mobile_hash")
	private String mobileHash;
    @JsonProperty("full_name")
	private String fullName;
    @JsonProperty("address")
	private String address;
    @JsonProperty("gender")
	private String gender;
    @JsonProperty("father_name")
	private String fatherName;
    @JsonProperty("email_hash")
	private String emailHash;
    @JsonProperty("aadhaar_number")
	private String aadhaarNumber;
    @JsonProperty("photo")
	private String photo;
}
