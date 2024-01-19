package com.medbay;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.domain.enums.Specialization;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import com.medbay.service.TherapyTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class TherapyTypeServiceTest {

    private TherapyTypeService therapyTypeService;

    @Mock
    private TherapyTypeRepository therapyTypeRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        therapyTypeService = new TherapyTypeService(therapyTypeRepository, equipmentRepository);
    }

    @Test
    void getTherapyType_ReturnsListOfTherapyTypes() {
        // Arrange
        List<TherapyType> expectedTherapyTypes = Arrays.asList(buildTherapyType("1"), buildTherapyType("2"));
        when(therapyTypeRepository.findAll()).thenReturn(expectedTherapyTypes);

        // Act
        ResponseEntity<List<TherapyType>> response = therapyTypeService.getTherapyType();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTherapyTypes, response.getBody());
    }

    @Test
    void getTherapyType_WhenNoTherapyTypes_ReturnsEmptyList() {
        // Arrange
        when(therapyTypeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<TherapyType>> response = therapyTypeService.getTherapyType();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void getTherapyType_WhenRepositoryThrowsException_ReturnsErrorResponse() {
        // Arrange
        when(therapyTypeRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> therapyTypeService.getTherapyType());
    }


    private TherapyType buildTherapyType(String id) {
        return TherapyType.builder()
                .therapyCode("#" + id)
                .name(id)
                .bodyPart("leg")
                .numOfSessions(6)
                .description("description")
                .id(Long.parseLong(id))
                .requiredEquipment(Equipment.builder()
                        .specialization(Specialization.CRYOTHERAPIST)
                        .roomName("room")
                        .description("description")
                        .capacity(5)
                        .id(Long.parseLong(id))
                        .build())
                .build();
    }

    // Additional test methods...
}
