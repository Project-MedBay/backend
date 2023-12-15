package com.medbay.repository;

import com.medbay.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT MAX(id) FROM _user", nativeQuery = true)
    Long findByMaxId();

}
