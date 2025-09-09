package org.example.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.example.model.Booking;
import org.example.model.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

public class EmailService {

    private final Properties props = new Properties();
    private final String senderEmail;
    private final String senderPassword;
    private final String adminEmail;

    public EmailService() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find email.properties");
                throw new RuntimeException("email.properties not found in classpath");
            }
            props.load(input);
            this.senderEmail = props.getProperty("sender.email");
            this.senderPassword = props.getProperty("sender.password");
            this.adminEmail = props.getProperty("admin.email");
        } catch (Exception ex) {
            throw new RuntimeException("Error loading email properties", ex);
        }
    }

    public void sendBookingConfirmation(User user, Booking booking) {
        String subject = "APEX TURF Booking Confirmation (ID: " + booking.getId() + ")";
        String template = loadTemplate("booking-confirmation.html");

        String body = template
                .replace("{{userName}}", user.getName())
                .replace("{{bookingId}}", booking.getId().toString())
                .replace("{{turfName}}", booking.getTurfName())
                .replace("{{sportName}}", booking.getSportName())
                .replace("{{startTime}}", booking.getFormattedStartTime())
                .replace("{{endTime}}", booking.getFormattedEndTime());

        // Send to user
        send(user.getEmail(), subject, body);
        // Send notification to admin
        send(adminEmail, "[Admin] New Booking: " + booking.getId(), "A new booking was made by " + user.getName() + " for " + booking.getTurfName() + " at " + booking.getFormattedStartTime());
    }

    public void sendCancellationConfirmation(User user, Booking booking, String cancelledBy) {
        String subject = "APEX TURF Booking Cancellation (ID: " + booking.getId() + ")";
        String template = loadTemplate("booking-cancellation.html");

        String body = template
                .replace("{{userName}}", user.getName())
                .replace("{{bookingId}}", booking.getId().toString())
                .replace("{{turfName}}", booking.getTurfName())
                .replace("{{sportName}}", booking.getSportName())
                .replace("{{startTime}}", booking.getFormattedStartTime());

        // Send to user
        send(user.getEmail(), subject, body);
        // Send notification to admin
        send(adminEmail, "[Admin] Booking Cancelled: " + booking.getId(), "Booking " + booking.getId() + " was cancelled by " + cancelledBy);
    }

    private void send(String to, String subject, String htmlBody) {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.err.println("Failed to send email. Error: " + e.getMessage());
            // In a real app, you would log this error more robustly
        }
    }

    private String loadTemplate(String fileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/" + fileName)) {
            if (is == null) throw new RuntimeException(fileName + " not found");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template " + fileName, e);
        }
    }
}