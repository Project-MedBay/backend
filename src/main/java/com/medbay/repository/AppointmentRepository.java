package com.medbay.repository;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.TherapyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByEmployeeAndDateTimeAfter(Employee employee, LocalDateTime dateTime);
    List<Appointment> findByDateTime(LocalDateTime dateTime);
    List<Appointment> findByEmployee(Employee employee);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.therapy.therapyType = :therapyType AND a.dateTime = :dateTime")
    int countAppointmentsByTherapyTypeAndDateTime(@Param("therapyType") TherapyType therapyType, @Param("dateTime") LocalDateTime dateTime);

    int countAppointmentsByDateTime(LocalDateTime dateTime);

    @Query(value = "SELECT MAX(id) FROM appointment", nativeQuery = true)
    Long findByMaxId();

    List<Appointment> findAllByDateTime(LocalDateTime dateTime);
}
