package com.medbay.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentStatisticsDTO {

    private Map<String, Double> percentageOfHoursWorkedLastMonth;
    private Map<String, Long> numberOfSessions;

}
