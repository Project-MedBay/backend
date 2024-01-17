package com.medbay.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDate;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Patient extends User {


    private String address;
    private LocalDate dateOfBirth;
    private String MBO;
    private String phoneNumber;
    @JdbcTypeCode(Types.VARBINARY)
    private byte[] photo;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"patient", "therapy", "employee", "session"})
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"patient", "employee"})
    private List<Therapy> therapies;


}
