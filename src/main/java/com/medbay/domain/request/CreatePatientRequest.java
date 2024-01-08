package com.medbay.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class CreatePatientRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private LocalDate dateOfBirth;
    @JsonProperty("MBO")
    private String MBO;
    @JsonProperty("OIB")
    private String OIB;
    private String phoneNumber;

}
