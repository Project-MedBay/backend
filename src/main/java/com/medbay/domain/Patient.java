package com.medbay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Patient extends User {


    private String address;
    private LocalDate dateOfBirth;
    private String MBO;
    private String phoneNumber;
    @OneToMany(mappedBy = "employee")
    private List<Appointment> appointments;
}
