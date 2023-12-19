package com.medbay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private LocalDateTime appointmentDate;
    private int equipmentCapacity;
    private int employeeCapacity;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

   // @ManyToOne
    //@JoinColumn(name = "employee_id")
    //private Employee employee;


}
