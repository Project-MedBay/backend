package com.medbay.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Patient extends User {


    private String address;
    private LocalDate dateOfBirth;
    private String OIB;
    private String MBO;
    private String phoneNumber;
//    @OneToMany(mappedBy = "employee")
//    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
//    private List<Appointment> appointments;
//}
}
