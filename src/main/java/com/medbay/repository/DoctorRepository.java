package com.medbay.repository;

import com.medbay.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, String> {


    @Query("SELECT d.active FROM Doctor d WHERE d.hlkid = :hlkid")
    boolean isActiveById(String hlkid);

}
