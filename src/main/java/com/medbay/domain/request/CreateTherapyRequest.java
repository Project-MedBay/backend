package com.medbay.domain.request;

import com.medbay.domain.TherapyType;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CreateTherapyRequest {
    private TherapyType therapyType;
    private String therapyCode;
    private Long patientId;
    private LocalDateTime appointmentDate;
    private Long employeeId;
    private Long appointmentId;
}
