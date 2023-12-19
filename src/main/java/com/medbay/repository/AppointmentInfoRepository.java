package com.medbay.repository;

import com.medbay.domain.AppointmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentInfoRepository extends JpaRepository<AppointmentInfo, Long> {
}

