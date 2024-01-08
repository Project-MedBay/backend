package com.medbay.domain.request;


import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CreateAppointmentRequest {

    private LocalDateTime dateTime;

    private Long patientId;

    private Long therapyId;

    private Long employeeId;

}
