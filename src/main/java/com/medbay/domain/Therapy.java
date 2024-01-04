package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.TherapyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private TherapyType therapyType;

    @Enumerated(EnumType.STRING)
    private TherapyStatus therapyStatus;

    //equipment
    @OneToMany(mappedBy = "therapy")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    List<Appointment> appointments;

}
