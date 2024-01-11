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
public class AppointmentDTO {

    private Long appointmentId;
    private String sessionNotes;
    private String therapyName;
    private LocalDateTime appointmentDate;
    private boolean show;
}
