package com.medbay.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.util.List;

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
    private Long appointmentId;
    private List<LocalDateTime> appointmentDates;


}
