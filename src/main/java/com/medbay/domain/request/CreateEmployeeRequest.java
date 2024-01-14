package com.medbay.domain.request;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeRequest {

    private String email;
    private LocalDateTime createdAt;
    private String password;
    private String firstName;
    private String lastName;
    private String specialization;
}
