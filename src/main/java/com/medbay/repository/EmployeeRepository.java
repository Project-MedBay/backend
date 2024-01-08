package com.medbay.repository;

import com.medbay.domain.Employee;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT MAX(id) FROM _user", nativeQuery = true)
    Long findByMaxId();

    @Query("SELECT COUNT(e) FROM Employee e")
    int employeeCapacity();

    @Query("SELECT e FROM Employee e WHERE e NOT IN " +
            "(SELECT a.employee FROM Appointment a WHERE a.dateTime = :dateTime)" +
            "AND e.specialization = :specialization")
    List<Employee> findAllByAppointmentsDateTimeAndSpecialization(LocalDateTime dateTime, Specialization specialization);

    List<Employee> findAllByStatus(ActivityStatus status);

    int countBySpecialization(Specialization specialization);


}
