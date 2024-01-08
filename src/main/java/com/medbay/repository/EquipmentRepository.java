package com.medbay.repository;

import com.medbay.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT e FROM Equipment e WHERE e.id = :equipmentId AND " +
           "(SELECT COUNT(a) FROM Appointment a JOIN a.therapy t JOIN t.therapyType tt " +
           "WHERE tt.requiredEquipment = e AND a.dateTime = :dateTime) >= e.capacity")
    Equipment isCapacityReachedForEquipmentOnDate(Long equipmentId, LocalDateTime dateTime);
}
