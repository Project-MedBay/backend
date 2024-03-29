package com.medbay.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSessionsDTO {

    private long id;
    private long appointmentId;
    private String equipmentRoomName;
    private LocalDateTime dateTime;
    private String sessionNotes;
    private int numberOfSessions;
    private int numberOfSessionsCompleted;
    private String therapyTypeName;
    private PatientDTO patient;

}
