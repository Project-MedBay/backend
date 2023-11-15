package com.medbay.domain.request;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateAppointmentRequest {

    private LocalDateTime dateTime;
    private String status;

    private Long patientId;

    private Long therapyId;

    private Long employeeId;

    private Long sessionId;

}
