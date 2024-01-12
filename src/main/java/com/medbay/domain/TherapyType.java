package com.medbay.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapyType {

    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.medbay.config.IdGenerator")
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
