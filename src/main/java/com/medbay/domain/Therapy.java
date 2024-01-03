package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.TherapyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @OneToOne
    private TherapyType therapyType;

    @Enumerated(EnumType.STRING)
    private TherapyStatus therapyStatus;

    @OneToMany(mappedBy = "therapy")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private List<Appointment> appointments;

   String rejectionReason;
}

