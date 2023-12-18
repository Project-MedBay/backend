package com.medbay.repository;

import com.medbay.domain.Equipment;
import com.medbay.domain.HealthRefferal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface HealthRefferalRepository extends JpaRepository<HealthRefferal,Long> {
}
