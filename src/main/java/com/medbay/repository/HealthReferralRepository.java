package com.medbay.repository;

import com.medbay.domain.Equipment;
import com.medbay.domain.HealthReferral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface HealthReferralRepository extends JpaRepository<HealthReferral,Long> {
}
