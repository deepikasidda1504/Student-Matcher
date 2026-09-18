import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    public static void sendEmail(String to, String subject, String messageText) {

        final String fromEmail = "sdphanisri@gmail.com";
        final String appPassword =System.getenv( "MAIL_APP_PASSWORD");
             if(appPassword==null||appPassword.trim().isEmpty()){
                     throws new RuntimeException("MAIL_APP_PASSWORD is notconfigured");
}


        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
                props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                fromEmail,
                                appPassword
                        );
                    }
                }
        );

        try {

            Message message = new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(fromEmail)
            );

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            message.setSubject(subject);

            message.setText(messageText);

            System.out.println(
                    "Trying to send email to: " + to
            );

            Transport.send(message);

            System.out.println(
                    "Email sent successfully to " + to
            );

        } catch (Exception e) {

            System.out.println(
                    "EMAIL SENDING FAILED"
            );

            e.printStackTrace();

            throw new RuntimeException(
                    "Email could not be sent: " + e.getMessage()
            );
        }
    }
}