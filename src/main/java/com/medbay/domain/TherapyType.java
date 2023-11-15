package com.medbay.domain;

import com.medbay.domain.Equipment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TherapyType {

    @Id
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "required_equipment_id")
    private Equipment requiredEquipment;


}
