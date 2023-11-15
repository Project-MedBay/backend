package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.ToOne;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "session")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("appointments")
    private Employee employee;

    private String feedback;
    private LocalDateTime dateTime;

}
