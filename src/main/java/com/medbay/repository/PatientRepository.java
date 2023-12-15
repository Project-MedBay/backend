package com.medbay.repository;

import com.medbay.domain.Patient;
import com.medbay.domain.enums.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{
    boolean existsByMBOOrOIB(String mbo, String oib);

    @Query(value = "SELECT MAX(id) FROM _user", nativeQuery = true)
    Long findByMaxId();

    List<Patient> findAllByStatus(ActivityStatus activityStatus);
}
