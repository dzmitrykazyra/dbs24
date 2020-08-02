/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.spring.core.mail;

import org.dbs24.spring.core.bean.AbstractApplicationBean;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.dbs24.application.core.nullsafe.NullSafe;
import java.util.Collection;
import java.util.function.Function;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.springframework.beans.factory.annotation.Value;
import lombok.Data;
/**
 *
 * @author Козыро Дмитрий
 */
@Data
public class MailManager extends AbstractApplicationBean {

    private final Properties props = NullSafe.createObject(Properties.class);
    @Value("${mailLogin}")
    private String username;
    @Value("${mailPassword}")
    private String password;
    @Value("${mailHost}")
    private String host;
    @Value("${mailPort}")
    private String port;
    @Value("${mailDefaultSender}")
    private String mailDefaultSender;
    @Value("${mailRecipientsList}")
    private String mailRecipientsList;

    //==========================================================================
    public void send(final String subject, final String text, final String address) {
        // отсылаем отдельным потоком

        synchronized (MailManager.class) {

            NullSafe.runNewThread(() -> {

                //final Session session = 
                NullSafe.create()
                        .execute(() -> {
                            final Message message = NullSafe.createObject(MimeMessage.class, Session.getInstance(getProps(), new Authenticator() {
                                @Override
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(getUsername(), getPassword());
                                }
                            }));
                            message.setFrom(new InternetAddress(this.getMailDefaultSender()));
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(NullSafe.notNull(address) ? address : getMailRecipientsList()));
                            message.setSubject(subject);
                            message.setContent(format2htmlmail(text), "text/html; charset=UTF-8");
                            //message.setText(text);

                            Transport.send(message);

                        });
            });
        }
    }

    private final static Collection<String> RED_WORDS = ServiceFuncs.<String>createCollection();

    private String format2htmlmail(final String body) {

        String newBody = "<html><body><p>" + body
                .replace("},{", "}</p><hr><p>")
                .replace("[{", "<p>")
                .replace("}]", "</p>")
                .replace("\\n", "<br/>")
                .replace("\",\"", "\",<br/>\"")
                .replace("null,\"", "\",<br/>\"");

        newBody = RED_WORDS.stream()
                .map(toReplace -> (Function<String, String>) s -> s.replaceAll(toReplace, fillInRed(toReplace)))
                .reduce(Function.identity(), Function::andThen)
                .apply(newBody);

        return newBody.concat("</p></body></html>");
    }

    private String fillInRed(final String keyWord) {
        return String.format("<b><font color=\"red\">%s</font></b>", keyWord);
    }

    //==========================================================================
    public <T extends MailRecord> void sendList(final Collection<T> collection) {

        collection
                .stream()
                .unordered()
                .forEach((mailRecord) -> {
                    this.send(mailRecord.getHeader(),
                            this.format2htmlmail(String.format("%s: %s",
                                    mailRecord.getRecClass(),
                                    mailRecord.getBody()
                            )),
                            mailRecord.getAddress());
                });
    }
}
