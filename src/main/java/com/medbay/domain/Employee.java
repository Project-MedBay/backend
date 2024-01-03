package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.Specialization;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class Employee extends User {

    private Specialization specialization;

    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "employee")
    @JsonIgnoreProperties("employee")
    private List<Therapy> therapies;

    public void addTherapy(Therapy therapy) {
        if (therapies == null) {
            therapies = new ArrayList<>();
        }
        therapies.add(therapy);
        therapy.setEmployee(this);
    }
}
