package com.medbay.repository;

import com.medbay.domain.TherapyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TherapyTypeRepository extends JpaRepository<TherapyType, Long> {
    Optional<TherapyType> findByTherapyCode(String therapyCode);

    @Query(value = "SELECT MAX(id) FROM therapy_type", nativeQuery = true)
    Long findByMaxId();

    boolean existsByTherapyCode(String therapyCode);
}
