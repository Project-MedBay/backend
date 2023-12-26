package com.medbay.domain.request;


import com.medbay.domain.enums.ActivityStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateAppointmentRequest {

    private LocalDateTime dateTime;
    private ActivityStatus status;

    private Long patientId;

    private Long therapyId;

    private Long employeeId;

    private Long sessionId;

}
