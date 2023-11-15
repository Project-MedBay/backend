package com.medbay.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.medbay.domain.enums.Role;
import com.medbay.domain.enums.Specialization;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class CreateEmployeeRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Specialization specialization;
    private boolean active;
    private Role role;
}
