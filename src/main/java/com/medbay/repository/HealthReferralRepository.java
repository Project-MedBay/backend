package com.medbay.repository;

import com.medbay.domain.HealthReferral;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthReferralRepository extends JpaRepository<HealthReferral,Long> {

    boolean existsByHlkidAndHealthReferralIdAndTherapyCode(String hlkid, String healthReferralId, String therapyCode);
}
