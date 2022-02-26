package org.dbs24.tik.assist.service.email;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.assist.service.exception.EmailSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Log4j2
@Service
public class EmailSender {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String staffEmail;

    public EmailSender(JavaMailSender javaMailSender, TemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(Email email) {

        try {
            Context context = new Context();
            context.setVariables(email.getTemplateVariables());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            String html = templateEngine.process(email.getTemplateFileName(), context);

            helper.setText(html, true);
            helper.setTo(email.getSendAddress());
            helper.setSubject(email.getSubject());
            helper.setFrom(staffEmail);

            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailSendException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
