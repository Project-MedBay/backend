package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.medbay.config.IdGenerator")
    private Long id;

    private LocalDateTime dateTime;

    private String sessionNotes;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties("appointments")
    private Patient patient;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "therapy_id")
    @JsonIgnoreProperties("appointments")
    private Therapy therapy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("appointments")
    private Employee employee;


}
