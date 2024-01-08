package com.medbay.config;

import com.medbay.domain.Doctor;
import com.medbay.repository.DoctorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.medbay.util.Helper.log;

@Component
@RequiredArgsConstructor
public class DoctorDataLoader implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public void run(String... args) {
        ClassPathResource resource = new ClassPathResource("doctors.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {


            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");

                Doctor doctor = Doctor.builder()
                        .hlkid(fields[0].trim())
                        .firstName(fields[1].trim())
                        .lastName(fields[2].trim())
                        .active("t".equals(fields[3].trim()))  //active (convert "t" to true)
                        .build();
                doctorRepository.save(doctor);
            }
        } catch (Exception e) {
            log("Error occurred while loading doctor data: " + e.getMessage());
        }
    }
}
