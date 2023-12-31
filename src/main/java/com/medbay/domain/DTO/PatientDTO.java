package com.medbay.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private String address;
    private LocalDate dateOfBirth;
    private String OIB;
    private String MBO;
    private String phoneNumber;
    private boolean show;
    private byte[] photo;

}
