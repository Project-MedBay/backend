package com.medbay.repository;

import com.medbay.domain.Therapy;
import com.medbay.domain.TherapyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapyRepository extends JpaRepository<Therapy, Long> {
    Therapy findByTherapyType(TherapyType therapyType);

}
