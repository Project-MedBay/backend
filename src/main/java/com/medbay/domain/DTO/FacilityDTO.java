package com.medbay.domain.DTO;

import com.medbay.domain.Equipment;
import com.medbay.domain.Therapy;
import com.medbay.domain.TherapyType;
import com.medbay.domain.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FacilityDTO {

    private List<Equipment> equipment;
    private List<TherapyType> therapyTypes;
    private List<Specialization> specializations;

}
