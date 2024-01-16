package com.medbay.config;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static com.medbay.util.Helper.log;

@Component
public class PythonScriptRunner {


    @SneakyThrows
    public String runMedBotScript(String chatHistory, String input, String patientName, String language) {

        StringBuilder sb = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/scripts/therapyHelper.py", patientName, input, chatHistory, language);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            log(e.getMessage());
            throw e;
        }
        return sb.toString();
    }

    @SneakyThrows
    public String runBayBotScript(String chatHistory, String input, String patientName, String language) {

        StringBuilder sb = new StringBuilder();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/resources/scripts/userGuide.py", patientName, input, chatHistory, language);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

        } catch (Exception e) {
            log(e.getMessage());
            throw e;
        }
        return sb.toString();
    }


}
