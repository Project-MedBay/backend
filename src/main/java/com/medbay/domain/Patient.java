package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Patient extends User {


    private String address;
    private LocalDate dateOfBirth;
    private String OIB;
    private String MBO;
    private String phoneNumber;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"patient", "employee"})
    private List<Therapy> therapies;


}
