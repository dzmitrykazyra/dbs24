package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.service.EmailSendingService;
import org.dbs24.tik.mobile.service.exception.http.UnprocessableEntityException;
import org.dbs24.tik.mobile.service.mail.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
public class EmailSendingServiceImpl implements EmailSendingService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String staffEmail;

    public EmailSendingServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public Mono<Void> send(Email email) {

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

            return Mono.empty();
        } catch (MessagingException e) {
            throw new UnprocessableEntityException();
        }
    }
}
