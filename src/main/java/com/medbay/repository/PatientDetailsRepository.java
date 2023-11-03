package com.medbay.repository;

import com.medbay.domain.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientDetailsRepository extends JpaRepository<PatientDetails, Long>{
}
