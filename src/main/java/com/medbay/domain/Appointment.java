package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;
    private String status;

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
