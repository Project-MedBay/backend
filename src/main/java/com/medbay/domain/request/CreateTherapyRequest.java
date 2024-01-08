package com.medbay.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
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
