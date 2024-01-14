package com.medbay.domain.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTherapyTypeRequest {

    private String name;
    private String bodyPart;
    private Integer numberOfSessions;
    private String description;
    private Long requiredEquipmentId;
}
