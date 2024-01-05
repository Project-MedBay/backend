package com.medbay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

@Entity
@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TherapyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //add therapy code
    private String therapyCode;
    private int numOfSessions;
    private String name;
    //add number of sessions

    private String description;

    //One to one
    @ManyToOne
    @JoinColumn(name = "required_equipment_id")
    private Equipment requiredEquipment;

}
