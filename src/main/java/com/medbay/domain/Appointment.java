package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.ActivityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;
    @Getter
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;
    //add feedback

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties("appointments")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "therapy_id")
    @JsonIgnoreProperties("appointments")
    private Therapy therapy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("appointments")
    private Employee employee;
//
//    @OneToOne
//    @JoinColumn(name = "session_id")
//    @JsonIgnoreProperties("appointments")
//    private Session session;


}
