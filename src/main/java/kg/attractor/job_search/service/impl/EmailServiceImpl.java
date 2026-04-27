package kg.attractor.job_search.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kg.attractor.job_search.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String to, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(EMAIL_FROM, "Job Search support");
        helper.setTo(to);

        String subject = "Ссылка для смены пароля";
        String content = "<p>Здравствуйте,</p>"

                + "<p>Вы сделали запрос на смену пароля.</p>"

                + "<p>Для сброса старого пароля и установки нового перейдите по ссылке ниже:</p>"

                + "<p><a href=\"" + link + "\">Сбросить пароль</a></p>"

                + "<br>"

                + "<p>Если вы не отправляли запрос на смену пароля или помните свой пароль, "

                + "просто проигнорируйте это сообщение.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
