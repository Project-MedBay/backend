package com.medbay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Therapy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String therapyCode;
    private String description;

    @ManyToOne
    @JoinColumn(name = "required_specialization_id")
    private Specialization requiredSpecialization;

    @ManyToOne
    @JoinColumn(name = "required_equipment_id")
    private Equipment requiredEquipment;


}
