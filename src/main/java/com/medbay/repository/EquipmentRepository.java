package com.medbay.repository;

import com.medbay.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query(value = "SELECT MAX(id) FROM equipment", nativeQuery = true)
    Long findByMaxId();
}
