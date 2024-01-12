package com.medbay.domain.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
}
