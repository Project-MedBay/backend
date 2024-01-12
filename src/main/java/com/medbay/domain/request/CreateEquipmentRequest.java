package com.medbay.domain.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEquipmentRequest {
    private String name;
    private int capacity;
    private String description;
    private String roomName;
    private String specialization;
}
