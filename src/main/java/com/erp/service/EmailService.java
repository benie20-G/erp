package com.example.erp.service;

import com.example.erp.entity.Employee;
import com.example.erp.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSalaryEmail(Message message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(message.getEmployee().getEmail());
        helper.setSubject("Salary Credit Notification");
        String htmlContent = generateSalaryEmailContent(message);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendVerificationEmail(Employee employee) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(employee.getEmail());
        helper.setSubject("Verify Your Email");
        String htmlContent = generateVerificationEmailContent(employee);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(Employee employee) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(employee.getEmail());
        helper.setSubject("Password Reset Request");
        String htmlContent = generatePasswordResetEmailContent(employee);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private String generateSalaryEmailContent(Message message) {
        Employee employee = message.getEmployee();
        String[] monthYear = message.getMonthYear().split("/");
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Salary Notification</title>
            </head>
            <body>
                <h2>Salary Credit Notification</h2>
                <p>Dear %s,</p>
                <p>We are pleased to inform you that your salary for %s/%s amounting to %s RWF 
                   from RCA has been credited to your account %s successfully.</p>
                <p>Thank you for your service.</p>
                <p>Best regards,<br>RCA</p>
            </body>
            </html>
            """.formatted(
                employee.getFirstName(),
                monthYear[0],
                monthYear[1],
                message.getMessage().split("amounting to ")[1].split(" ")[0],
                employee.getCode()
            );
    }

    private String generateVerificationEmailContent(Employee employee) {
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + employee.getVerificationToken();
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Email Verification</title>
            </head>
            <body>
                <h2>Email Verification</h2>
                <p>Dear %s,</p>
                <p>Please verify your email by clicking the link below:</p>
                <p><a href="%s">Verify Email</a></p>
                <p>If you did not register, please ignore this email.</p>
                <p>Best regards,<br>RCA</p>
            </body>
            </html>
            """.formatted(
                employee.getFirstName(),
                verificationUrl
            );
    }

    private String generatePasswordResetEmailContent(Employee employee) {
        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + employee.getResetPasswordToken();
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Password Reset Request</title>
            </head>
            <body>
                <h2>Password Reset Request</h2>
                <p>Dear %s,</p>
                <p>You have requested to reset your password. Please click the link below to reset it:</p>
                <p><a href="%s">Reset Password</a></p>
                <p>This link will expire in 24 hours. If you did not request a password reset, please ignore this email.</p>
                <p>Best regards,<br>RCA</p>
            </body>
            </html>
            """.formatted(
                employee.getFirstName(),
                resetUrl
            );
    }
}