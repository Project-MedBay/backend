package com.medbay.domain;

import com.medbay.domain.Equipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRefferal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String health_refferal_id;
    private String hlkid;
    private int MBO;
    private String therapyCode;
    public void setHealth_refferal_id(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
