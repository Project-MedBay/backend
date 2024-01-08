package com.medbay.domain.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateTherapyTypeRequest {
    private String description;
    private Long requiredEquipmentId;
}
