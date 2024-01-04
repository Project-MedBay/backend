package com.medbay.repository;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT MAX(id) FROM _user", nativeQuery = true)
    Long findByMaxId();

    @Query("SELECT COUNT(e) FROM Employee e")
    int employeeCapacity();

}
