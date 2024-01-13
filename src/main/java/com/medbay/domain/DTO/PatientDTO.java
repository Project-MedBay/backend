package com.medbay.domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;
    private String address;
    private LocalDate dateOfBirth;
    private String MBO;
    private String phoneNumber;
    private boolean show;
    private byte[] photo;
    private List<AppointmentDTO> appointments;

}
