package com.medbay.repository;

import com.medbay.domain.AppointmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentInfoRepository extends JpaRepository<AppointmentInfo, Long> {
    List<AppointmentInfo> findByDate(LocalDate date);
}

