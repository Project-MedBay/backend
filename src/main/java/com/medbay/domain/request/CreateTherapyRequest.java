package com.medbay.domain.request;

import com.medbay.domain.TherapyType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateTherapyRequest {
    private TherapyType therapyType;

    private Long patientId;

    private Long employeeId;

    private Long appointmentId;
}
