package com.tvpss.service;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    public static void sendRejectionEmail(String recipientEmail, String rejectReason) throws UnsupportedEncodingException {
        // Sender's email and credentials
        final String senderEmail = "sarveish_simbu@yahoo.com";
        final String senderPassword = "vxlartnmzenvqeey"; // Use an app password, not your Gmail password

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.yahoo.com"); // Correct Yahoo SMTP host
        props.put("mail.smtp.port", "465"); // Port for SSL
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "TVPSSCore")); // Set From Name
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Application Rejected");

            // Email content
            String emailContent = "Dear Teacher,\n\n"
                    + "Your application has been rejected for the following reason:\n\n"
                    + rejectReason + "\n\n"
                    + "If you have any questions, please contact District Officer.\n\n"
                    + "Best regards,\n"
                    + "TVPSS Management";

            message.setText(emailContent);

            // Send the message
            Transport.send(message);
            System.out.println("Rejection email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send rejection email.");
        }
    }
    
    public static void sendApprovalEmail(String recipientEmail) throws UnsupportedEncodingException {
        // Sender's email and credentials
        final String senderEmail = "sarveish_simbu@yahoo.com";
        final String senderPassword = "vxlartnmzenvqeey"; // Use an app password, not your Gmail password

        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.yahoo.com"); // Correct Yahoo SMTP host
        props.put("mail.smtp.port", "465"); // Port for SSL
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // Create a session with an authenticator
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "TVPSSCore")); // Set From Name
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Application Approved");

            // Email content
            String emailContent = "Dear Teacher,\n\n"
                    + "Your application has been APPROVED!!\n\n"
                    + "If you have any questions, please contact District Officer.\n\n"
                    + "Best regards,\n"
                    + "TVPSS Management";

            message.setText(emailContent);

            // Send the message
            Transport.send(message);
            System.out.println("Rejection email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send rejection email.");
        }
    }
}
