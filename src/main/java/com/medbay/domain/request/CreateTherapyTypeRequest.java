package com.medbay.domain.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyTypeRequest {

    private String name;
    private String bodyPart;
    private int numberOfSessions;
    private String description;
    private Long requiredEquipmentId;
}
