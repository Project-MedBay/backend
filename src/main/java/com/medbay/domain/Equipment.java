package com.medbay.domain;

import com.medbay.domain.enums.Specialization;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(generator = "IdGenerator")
    @GenericGenerator(name = "IdGenerator", strategy = "com.medbay.config.IdGenerator")
    private Long id;
    private String name;
    private int capacity;
    @Size(max = 600)
    private String description;
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    private String roomName;

}
