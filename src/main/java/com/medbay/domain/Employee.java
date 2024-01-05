package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.Specialization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Employee extends User {

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

//    @OneToMany(mappedBy = "employee")
//    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
//    private List<Appointment> appointments;


    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties("employee")
    private List<Therapy> therapies;

}
