package com.medbay.service;

import com.medbay.domain.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;



    @SneakyThrows
    public void sendConfirmationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail()); // Assuming the user's email is stored in the 'User' object
        helper.setSubject("Welcome to MedBay - Registration Confirmation");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Registration Confirmation</title>"
                + "<style>"
                + "/* Styles here */"
                + ".container {"
                + "  width: 100%;"
                + "  max-width: 600px;"
                + "  margin: 0 auto;"
                + "  background-color: #ffffff;"
                + "  padding: 40px;"
                + "  border-radius: 15px;"
                + "  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".header {"
                + "  text-align: center;"
                + "  margin-bottom: 30px;"
                + "}"
                + ".header h1 {"
                + "  color: #4e89ae;"
                + "  margin: 0;"
                + "  font-size: 42px;"
                + "  text-transform: uppercase;"
                + "}"
                + ".content {"
                + "  color: #333333;"
                + "  line-height: 1.6;"
                + "  font-size: 16px;"
                + "  margin-bottom: 30px;"
                + "  text-align: justify;"
                + "}"
                + ".footer {"
                + "  text-align: center;"
                + "  margin-top: 20px;"
                + "  color: #777777;"
                + "}"
                + ".footer p {"
                + "  margin: 5px 0;"
                + "  font-size: 16px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\"><h1>Registration Confirmed</h1></div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>Your registration with MedBay has been confirmed! Welcome to our community dedicated to medical rehabilitation.</p>"
                + "<p>Explore our platform and discover the various tools and resources available to support your health journey.</p>"
                + "<p><a href='https://medbay.life' style='text-decoration: none; color: #4eae78;'>Get started now</a> and benefit from our services!</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Sincerely,</p>"
                + "<p>MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void sendTherapyConfirmationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail());
        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Therapy Terms Confirmed</title>"
                + "<style>"
                + "/* General styles */"
                + "body {"
                + "    font-family: 'Arial', sans-serif;"
                + "    margin: 0;"
                + "    padding: 0;"
                + "    background-color: #f7f7f7;"
                + "}"
                + ".container {"
                + "    width: 100%;"
                + "    max-width: 600px;"
                + "    margin: 0 auto;"
                + "    background-color: #ffffff;"
                + "    padding: 40px;"
                + "    border-radius: 15px;"
                + "    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".header {"
                + "    text-align: center;"
                + "    margin-bottom: 30px;"
                + "}"
                + ".header h1 {"
                + "    color: #4e89ae;"
                + "    margin: 0;"
                + "    font-size: 42px;"
                + "    text-transform: uppercase;"
                + "}"
                + ".content {"
                + "    color: #333333;"
                + "    line-height: 1.6;"
                + "    font-size: 16px;"
                + "    margin-bottom: 30px;"
                + "    text-align: justify;"
                + "}"
                + ".email-img {"
                + "    display: block;"
                + "    margin: 20px auto;"
                + "    max-width: 100%;"
                + "    height: auto;"
                + "    border-radius: 15px;"
                + "    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                + "}"
                + ".btn-container {"
                + "    text-align: center;"
                + "}"
                + ".btn {"
                + "  display: inline-block;"
                + "  padding: 15px 30px;"
                + "  background-color: #4eae78;"
                + "  color: #ffffff;"
                + "  text-decoration: none;"
                + "  border-radius: 30px;"
                + "  text-transform: uppercase;"
                + "  transition: background-color 0.3s ease-in-out;"
                + "}"
                + ".btn:hover {"
                + "  background-color: #24895f;"
                + "}"
                + ".footer {"
                + "  text-align: center;"
                + "  margin-top: 20px;"
                + "  color: #777777;"
                + "}"
                + ".footer p {"
                + "  margin: 5px 0;"
                + "  font-size: 16px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\"><h1>Therapy Terms Confirmed</h1></div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>Your selected therapy terms have been confirmed. We are excited to proceed with your requested therapies!</p>"
                + "<p>You can now explore our website and find more details about your therapy sessions.</p>"
                + "<div class=\"btn-container\">"
                + "<a class=\"btn\" href=\"https://medbay.life\">Visit Our Website</a>"
                + "</div>"
                + "<p>Start your journey toward better health with MedBay.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Sincerely,</p>"
                + "<p>MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setSubject("MedBay - Therapy Confirmation");
        helper.setText(emailContent, true);


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

        String resetLink = "https://medbay.life/api/security/change-password?token=" + token;


        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(email);
        String emailContent = "<!DOCTYPE html>"
                    + "<html lang=\"en\">"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<title>Password Reset</title>"
                    + "<style>"
                    + "body {"
                    + "  font-family: 'Arial', sans-serif;"
                    + "  margin: 0;"
                    + "  padding: 0;"
                    + "  background-color: #f7f7f7;"
                    + "}"
                    + ".container {"
                    + "  width: 100%;"
                    + "  max-width: 600px;"
                    + "  margin: 0 auto;"
                    + "  background-color: #ffffff;"
                    + "  padding: 40px;"
                    + "  border-radius: 15px;"
                    + "  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                    + "}"
                    + ".header {"
                    + "  text-align: center;"
                    + "  margin-bottom: 30px;"
                    + "}"
                    + ".header h1 {"
                    + "  color: #4e89ae;"
                    + "  margin: 0;"
                    + "  font-size: 42px;"
                    + "  text-transform: uppercase;"
                    + "}"
                    + ".content {"
                    + "  color: #333333;"
                    + "  line-height: 1.6;"
                    + "  font-size: 16px;"
                    + "  margin-bottom: 30px;"
                    + "  text-align: justify;"
                    + "}"
                    + ".btn-container {"
                    + "  text-align: center;"
                    + "}"
                    + ".btn {"
                    + "  display: inline-block;"
                    + "  padding: 15px 30px;"
                    + "  background-color: #4eae78;"
                    + "  color: #ffffff;"
                    + "  text-decoration: none;"
                    + "  border-radius: 30px;"
                    + "  text-transform: uppercase;"
                    + "  transition: background-color 0.3s ease-in-out;"
                    + "}"
                    + ".btn:hover {"
                    + "  background-color: #24895f;"
                    + "}"
                    + ".footer {"
                    + "  text-align: center;"
                    + "  margin-top: 20px;"
                    + "  color: #777777;"
                    + "}"
                    + ".footer p {"
                    + "  margin: 5px 0;"
                    + "  font-size: 16px;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class=\"container\">"
                    + "<div class=\"header\"><h1>Password Reset</h1></div>"
                    + "<div class=\"content\">"
                    + "<p>Hello,</p>"
                    + "<p>We've received a request to reset your password. Click the button below to reset your password:</p>"
                    + "<div class=\"btn-container\">"
                    + "<a class=\"btn\" href=\"" + resetLink + "\">Reset Password</a>"
                    + "</div>"
                    + "<p>If you didn't request this change, please ignore this email. Your password will remain unchanged.</p>"
                    + "</div>"
                    + "<div class=\"footer\">"
                    + "<p>Sincerely,</p>"
                    + "<p>MedBay Team</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

        helper.setSubject("MedBay - Password Reset");
        helper.setText(emailContent, true);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }
}
