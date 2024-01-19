package com.medbay;

import com.medbay.domain.Therapy;
import com.medbay.repository.*;
import com.medbay.service.EmailService;
import com.medbay.service.TherapyService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TherapyServiceTest {

    private TherapyService therapyService;

    @Mock
    private EmailService emailService;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private HealthReferralRepository healthReferralRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private TherapyRepository therapyRepository;
    @Mock
    private TherapyTypeRepository therapyTypeRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        therapyService = new TherapyService(therapyRepository, therapyTypeRepository, emailService,
                                            appointmentRepository, patientRepository, employeeRepository,
                                            healthReferralRepository, doctorRepository);
    }

    @Test
    void getTherapies_ReturnsListOfTherapies() {
        // Arrange
        List<Therapy> expectedTherapies = Arrays.asList(buildTherapy("1"), buildTherapy("2"));
        when(therapyRepository.findAll()).thenReturn(expectedTherapies);

        // Act
        ResponseEntity<List<Therapy>> response = therapyService.getTherapies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTherapies, response.getBody());
    }

    @Test
    void getTherapies_WhenNoTherapies_ReturnsEmptyList() {
        // Arrange
        when(therapyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<Therapy>> response = therapyService.getTherapies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }


    @Test
    void deleteTherapy_WhenTherapyExists_DeletesTherapy() {
        // Arrange
        Long therapyId = 1L;
        when(therapyRepository.existsById(therapyId)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = therapyService.deleteTherapy(therapyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(therapyRepository).deleteById(therapyId);
    }

    @Test
    void deleteTherapy_WhenTherapyDoesNotExist_ReturnsNotFound() {
        // Arrange
        Long therapyId = 1L;
        when(therapyRepository.existsById(therapyId)).thenReturn(false);

        // Act
        ResponseEntity<Void> response = therapyService.deleteTherapy(therapyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(therapyRepository, never()).deleteById(therapyId);
    }

    private Therapy buildTherapy(String id) {
        return Therapy.builder()
                .id(Long.parseLong(id))
                .build();
    }



}
