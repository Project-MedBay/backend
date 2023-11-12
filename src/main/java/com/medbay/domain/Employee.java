package com.medbay.domain;

import com.medbay.domain.enums.Specialization;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends User {

    private Specialization specialization;

    @OneToMany(mappedBy = "employee")
    private List<Appointment> appointments;


}
