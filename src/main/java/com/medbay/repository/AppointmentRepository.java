package com.medbay.repository;

import com.medbay.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByEmployeeId(Long employeeId);
    List<Appointment> findByDateTime(LocalDateTime dateTime);


}
