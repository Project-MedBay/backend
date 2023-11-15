package com.medbay.repository;

import com.medbay.domain.TherapyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TherapyTypeRepository extends JpaRepository<TherapyType, Long> {
}
