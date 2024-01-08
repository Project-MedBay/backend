package com.medbay.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TherapyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bodyPart;
    private String therapyCode;
    private int numOfSessions;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "required_equipment_id")
    private Equipment requiredEquipment;

}
