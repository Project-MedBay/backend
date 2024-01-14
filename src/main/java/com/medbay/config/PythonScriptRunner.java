package com.medbay.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static com.medbay.util.Helper.log;

@Component
@RequiredArgsConstructor
public class PythonScriptRunner {

    private final Environment environment;

    @PostConstruct
    @SneakyThrows
    public void runScript(//String patientId, String input
                          ) {
        String[] activeProfiles = environment.getActiveProfiles();
        String patientId = "1";
        String input = "I just had a surgery on my ACL. I need to know what I can do to recover faster.";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/therapyHelper.py", patientId, input);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log(line);
            }

            int exitCode = process.waitFor();
            log("\nExited with code: " + exitCode);
        } catch (Exception e) {
            log(e.getMessage());
            throw e;
        }
    }
}
