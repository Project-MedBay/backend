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
public class TherapyDTO {

    private Long therapyId;
    private Long patientId;
    private String therapyTypeName;
    private String therapyTypeCode;
    private LocalDateTime requestDate;
    private int numberOfSessions;
    private List<LocalDateTime> sessionDates;
    private List<String> sessionNotes;

}
