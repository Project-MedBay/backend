package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.ActivityStatus;
import jakarta.persistence.*;
import org.hibernate.mapping.ToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
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


}
