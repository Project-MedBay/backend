package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private TherapyType therapyType;

    //Therapy status: PENDING, ACTIVE

    //equipment
    @Getter
    @OneToMany(mappedBy = "therapy")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    List<Appointment> appointments;

}
