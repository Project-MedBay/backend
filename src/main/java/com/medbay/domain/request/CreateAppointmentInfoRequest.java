package com.medbay.domain.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateAppointmentInfoRequest {

    private LocalDate appointmentDate;
    private Long employeeId;
    private Long equipmentId;

}
