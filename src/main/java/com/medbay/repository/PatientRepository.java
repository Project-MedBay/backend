package com.medbay.repository;

import com.medbay.domain.Patient;
import com.medbay.domain.enums.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{
    boolean existsByMBOOrOIB(String mbo, String oib);

    @Query(value = "SELECT MAX(id) FROM _user", nativeQuery = true)
    Long findByMaxId();
    @Query("SELECT p FROM Patient p JOIN FETCH p.therapies WHERE p.id = :patientId")
    Optional<Patient> findByIdWithTherapies(@Param("patientId") Long patientId);

    List<Patient> findAllByStatus(ActivityStatus activityStatus);

    Patient findByMBO(String patientMBO);
}
