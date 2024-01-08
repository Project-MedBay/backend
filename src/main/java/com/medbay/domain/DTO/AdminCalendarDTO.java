package com.medbay.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCalendarDTO {

    private Long therapyId;
    private String therapyTypeName;
    private String patientFirstName;
    private String patientLastName;
    private String employeeFirstName;
    private String employeeLastName;


}
