package com.medbay.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.medbay.util.Helper.log;

@Component
@RequiredArgsConstructor
public class PythonScriptRunner {


    @SneakyThrows
    public String runTherapyScript(String patientId, String input) {

        StringBuilder sb = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/therapyHelper.py", patientId, input);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            int exitCode = process.waitFor();
            log("\nExited with code: " + exitCode);
        } catch (Exception e) {
            log(e.getMessage());
            throw e;
        }
        return sb.toString();
    }
}
