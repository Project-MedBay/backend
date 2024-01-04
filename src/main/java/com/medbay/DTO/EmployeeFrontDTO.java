package com.medbay.DTO;

import com.medbay.domain.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFrontDTO {

    private Appointment appointment;
    private Equipment equipment;

}
