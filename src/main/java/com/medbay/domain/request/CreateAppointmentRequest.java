package com.medbay.domain.request;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    private LocalDateTime dateTime;

    private Long patientId;

    private Long therapyId;

    private Long employeeId;

}
