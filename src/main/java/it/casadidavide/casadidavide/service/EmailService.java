package it.casadidavide.casadidavide.service;

//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;

//@Service
public class EmailService {

//	private final JavaMailSender mailSender;

//    public EmailService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

//    public void inviaNotificaRegistrazione(String to, String nominativo, String ruolo) {
//        MimeMessage message = mailSender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setFrom("davideluisi002@gmail.com");
//            helper.setTo(to);
//            helper.setSubject("Associazione: nuova registrazione");
//            helper.setText("Nuovo utente registrato: " + nominativo + " - " + ruolo, true);
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Errore nell'invio dell'email", e);
//        }
//    }
}
