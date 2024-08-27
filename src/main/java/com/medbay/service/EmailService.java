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
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_RELATED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    ClassPathResource imageClassPathResource = new ClassPathResource("MedBay.png");

    @SneakyThrows
    public void sendConfirmationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to MedBay - Registration Confirmation");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Registration Confirmation</title>"
                + emailSytleCSS
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\" style=\"text-align: center;\">"
                + "<img src='cid:logo' alt='MedBay Logo'>"
                + "<h1>Welcome to MedBay!</h1>"
                + "</div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>Your registration with MedBay has been successfully confirmed. We're thrilled to welcome you to our community dedicated to providing the best medical rehabilitation services.</p>"
                + "<p>Start exploring our platform today and discover the wide range of tools and resources we offer to support your health and well-being. Don't hesitate to reach out if you have any questions or need guidance!</p>"
                + "<div style=\"text-align: center;\">"
                + "<a href='https://medbay.life' style='display: inline-block; padding: 10px 20px; margin: 10px 0; background-color: #0d6efd; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>Visit MedBay Now</a>"
                + "</div>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Warm regards,</p>"
                + "<p>The MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        helper.addInline("logo", imageClassPathResource);


        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            log("Error sending email: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void sendTherapyConfirmationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail());
        helper.setSubject("MedBay - Therapy Confirmation");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Therapy Terms Confirmed</title>"
                + emailSytleCSS
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\">"
                + "<img src='cid:logo' alt='MedBay Logo'>"
                + "<h1>Therapy Terms Confirmed</h1>"
                + "</div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>Great news – your journey with MedBay is officially underway! Your therapy plan has been tailored to meet your unique needs and set you on the path to wellness.</p>"
                + "<p>With your therapy terms now confirmed, a personalized approach to your health and recovery awaits. Our dedicated team is committed to providing you with the highest quality of care and support throughout your healing process.</p>"
                + "<p>On our website, you’ll find a wealth of resources, from expert advice to community stories, all designed to keep you informed and engaged as you move forward. Plus, your personal dashboard provides a quick overview of upcoming sessions, progress tracking, and easy communication with your therapy team.</p>"
                + "<div style=\"text-align: center; margin-top: 30px; margin-bottom: 20px;\">"
                + "<a href='https://medbay.life' style='display: inline-block; padding: 10px 20px; margin: 10px 0; background-color: #0d6efd; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>Explore Your Dashboard</a>"
                + "</div>"
                + "<p>Should you have any questions or require further assistance, our support team is just a message away. We’re here to ensure a smooth and successful therapy experience.</p>"
                + "<p>Thank you for choosing MedBay. Let’s embark on this path to better health together!</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Warm regards,</p>"
                + "<p>The MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);
        helper.addInline("logo", imageClassPathResource);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }


    @SneakyThrows
    public void sendChangePasswordEmail(String email, String token) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String resetLink = "https://medbay.life/reset-password?token=" + token;

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(email);
        helper.setSubject("MedBay - Password Reset");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Password Reset</title>"
                + emailSytleCSS
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\" style=\"text-align: center;\">"
                + "<img src='cid:logo' alt='MedBay Logo'>"
                + "<h1>Password Reset</h1>"
                + "</div>"
                + "<div class=\"content\">"
                + "<p>We've received a request to reset your password. If you did not request this, please ignore this email, and your password will remain the same. Otherwise, please click the button below to choose a new password:</p>"
                + "<div style=\"text-align: center; margin-top: 30px; margin-bottom: 20px;\">"
                + "<a href='" + resetLink + "' style='display: inline-block; padding: 10px 20px; margin: 10px 0; background-color: #0d6efd; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;'>Reset Password</a>"
                + "</div>"
                + "<p>If you're having trouble with the button above, copy and paste the URL below into your web browser:</p>"
                + "<p><a href='" + resetLink + "'>" + resetLink + "</a></p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Warm regards,</p>"
                + "<p>The MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        helper.addInline("logo", imageClassPathResource);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void sendRejectionEmail(User user, String rejectionReason) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail());
        helper.setSubject("MedBay - Application Rejection");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Application Rejection</title>"
                + emailSytleCSS
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\" style=\"text-align: center;\">"
                + "<img src='cid:logo' alt='MedBay Logo'>"
                + "<h1>Application Update</h1>"
                + "</div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>We regret to inform you that after careful consideration, we are unable to proceed with your application. Please see the message from our administrator below:</p>"
                + "<blockquote style='background-color: #ddebe9; padding: 20px; border-left: 6px solid #0d6efd; margin: 20px 0; font-style: italic;'>"
                + "<p>" + rejectionReason + "</p>"
                + "</blockquote>"
                + "<p>If you have any questions or require further clarification, please do not hesitate to reach out to us.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Warm regards,</p>"
                + "<p>The MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        helper.addInline("logo", imageClassPathResource);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }

    @SneakyThrows
    public void sendTherapyRejectionEmail(User user, String rejectionReason) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MULTIPART_MODE_RELATED, "UTF-8");

        helper.setFrom("noreply@medbay.life", "MedBay Team");
        helper.setTo(user.getEmail());
        helper.setSubject("MedBay - Therapy Request Update");

        String emailContent = "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head>"
                + "<meta charset=\"UTF-8\">"
                + "<title>Therapy Request Rejection</title>"
                + emailSytleCSS
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\" style=\"text-align: center;\">"
                + "<img src='cid:logo' alt='MedBay Logo'>"
                + "<h1>Therapy Request Status</h1>"
                + "</div>"
                + "<div class=\"content\">"
                + "<p>Hello " + user.getFirstName() + ",</p>"
                + "<p>We regret to inform you that your recent therapy request could not be approved. The detailed reason for the decision is provided below by our administrative team:</p>"
                + "<blockquote style='background-color: #fcdede; padding: 20px; border-left: 6px solid #dc3545; margin: 20px 0; font-style: italic;'>"
                + "<p><strong>Administrator's Remarks:</strong><br>" + rejectionReason + "</p>"
                + "</blockquote>"
                + "<p>We understand that this news may be disappointing, and we sincerely apologize for any inconvenience this may have caused. If you believe this decision requires further review, or if you wish to discuss alternative therapy options, please contact us directly.</p>"
                + "<p>Thank you for your understanding, and we look forward to assisting you in any way possible.</p>"
                + "</div>"
                + "<div class=\"footer\">"
                + "<p>Kind regards,</p>"
                + "<p>The MedBay Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(emailContent, true);

        helper.addInline("logo", new ClassPathResource("MedBay.png"));

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log("Error sending email: " + e.getMessage());
        }
    }


    private static final String emailSytleCSS =
                    "<style>"
                    + ".container {"
                    + "  width: 100%;"
                    + "  max-width: 600px;"
                    + "  margin: 0 auto;"
                    + "  background-color: #f7f7f7;"
                    + "  padding: 40px;"
                    + "  border-radius: 15px;"
                    + "  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);"
                    + "}"
                    + ".header img {"
                    + "  max-width: 150px;"
                    + "  margin-bottom: 20px;"
                    + "}"
                    + ".header h1 {"
                    + "  color: #0d6efd;"
                    + "  margin: 0;"
                    + "  font-size: 28px;"
                    + "}"
                    + ".content {"
                    + "  color: #333333;"
                    + "  line-height: 1.6;"
                    + "  font-size: 16px;"
                    + "  margin-bottom: 30px;"
                    + "  text-align: left;"
                    + "}"
                    + ".content a {"
                    + "  color: #0d6efd;"
                    + "  text-decoration: underline;"
                    + "}"
                    + ".footer {"
                    + "  text-align: center;"
                    + "  margin-top: 20px;"
                    + "  color: #777777;"
                    + "}"
                    + ".footer p {"
                    + "  margin: 5px 0;"
                    + "  font-size: 14px;"
                    + "}"
                    + "</style>";

}
