package com.medbay.domain.request;

import lombok.Getter;

@Getter
public class CreateEquipmentRequest {
    private String name;
    private int capacity;
}
