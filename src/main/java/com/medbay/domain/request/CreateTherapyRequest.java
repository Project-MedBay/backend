package com.medbay.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyRequest {

    @NotBlank
    private String therapyCode;
    @NotNull
    private List<LocalDateTime> appointmentDates;
    @NotBlank
    private String hlkid;
    @NotBlank
    private String healthReferralId;


}
