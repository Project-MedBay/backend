package com.medbay.domain.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateEmployeeRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
}
