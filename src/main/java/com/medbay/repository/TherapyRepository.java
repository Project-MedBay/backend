package com.medbay.repository;

import com.medbay.domain.Appointment;
import com.medbay.domain.Therapy;
import com.medbay.domain.enums.TherapyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long> {
    List<Therapy> findByTherapyStatus(TherapyStatus therapyStatus);

}
