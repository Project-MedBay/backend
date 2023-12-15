package com.medbay.service;

import com.medbay.domain.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;



    @SneakyThrows
    public void sendEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("noreply@medbay.life", "Đanko");
        helper.setTo("ian.balen6@gmail.com");

        helper.setSubject("Testing");
        helper.setText("Testing bato");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void sendChangePasswordEmail(String email, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("noreply@medbay.life", "Đanko");
        helper.setTo("ian.balen6@gmail.com");

        helper.setSubject("Testing");
        helper.setText("Testing bato");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }
}
