package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medbay.domain.enums.TherapyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Therapy {

    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.medbay.config.IdGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    private TherapyType therapyType;

    @Enumerated(EnumType.STRING)
    private TherapyStatus therapyStatus;

    @OneToMany(mappedBy = "therapy")
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private List<Appointment> appointments;

}

