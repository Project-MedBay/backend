package com.medbay.domain.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateSessionRequest {
    private Long appointmentId;
    private Long employeeId;
    private String feedback;
    private LocalDateTime dateTime;
}
