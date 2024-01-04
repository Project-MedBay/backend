package com.medbay.DTO;

import com.medbay.domain.Equipment;
import com.medbay.domain.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedAppointmentDTO {
    private LocalDateTime appointmentDate;
    private Equipment equipment;
    private Patient patient;
    private String therapyTypeDescription;
}
