package com.medbay.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class FacilityData {
    private List<Equipment> equipmentList;
    private List<TherapyType> therapyTypeList;

    // Constructors, getters, and setters
}

