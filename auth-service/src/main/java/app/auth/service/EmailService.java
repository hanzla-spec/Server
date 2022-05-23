package app.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true,"utf-8");
        helper.setTo(to);
        helper.setFrom("findall.message.service@gmail.com");
        helper.setSubject(subject);
        helper.setText(message, true);
        javaMailSender.send(msg);
    }
}
