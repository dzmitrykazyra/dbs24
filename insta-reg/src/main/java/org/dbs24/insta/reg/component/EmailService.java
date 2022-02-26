/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import org.dbs24.insta.reg.action.ValidationCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.dbs24.consts.SysConst;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.dbs24.insta.reg.entity.*;
import org.dbs24.insta.reg.email.*;
import org.dbs24.insta.reg.repo.*;
import static org.dbs24.insta.reg.consts.InstaConsts.EmailStatuses.*;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.dbs24.service.JavaFakerService;
import java.time.LocalDateTime;
import org.dbs24.application.core.locale.NLS;

@Log4j2
@Component
@Data
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "refs")
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class EmailService extends AbstractApplicationService {
    
    final EmailRepo emailRepo;
    final RefsService refsService;
    final JavaFakerService javaFakerService;
    
    private Map<Integer, String> emailsList;
    
    @Value("${config.email.host}")
    private String host;
    
    @Value("${config.email.proxy-host}")
    private String proxyHost;
    
    @Value("${config.email.proxy-port}")
    private String proxyPort;
    
    @Value("${config.email.proxy-user}")
    private String proxyUser;
    
    @Value("${config.email.proxy-pass}")
    private String proxyPass;
    
    @Value("${config.email.proxy-change}")
    private String proxyChange;
    
    @Value("${config.proxy.accept-header}")
    private String acceptHeader;
    
    @Value("${config.proxy.accept-language}")
    private String acceptLanguage;
    
    @Value("${config.config.email.pause:15000}")
    private Integer pauseLength;
    
    private String userAgent;

    // in minutes
    @Value("${config.email.change-ip.freq:3}")
    private Integer changeIpFreq;
    
    private LocalDateTime lastChangeIp = LocalDateTime.now().minusDays(1);
    
    public String getUserAgent() {
        StmtProcessor.ifNull(userAgent, () -> userAgent = javaFakerService.createUserAgent());
        return userAgent;
    }
    
    public EmailService(EmailRepo emailRepo, RefsService refsService, JavaFakerService javaFakerService) {
        this.emailRepo = emailRepo;
        this.refsService = refsService;
        this.javaFakerService = javaFakerService;
    }

    //@Transactional(readOnly = true)
    public void changeIp() {
        
        StmtProcessor.execute(() -> {
            
            lastChangeIp = LocalDateTime.now();
            
            final OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new UnzippingInterceptor())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS).build();
            
            final RequestBody formBody = new FormBody.Builder()
                    .build();
            
            log.info("change ip: {}", proxyChange);
            
            final Request request = new Request.Builder()
                    .url(proxyChange)
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("accept", acceptHeader)
                    .addHeader("accept-language", acceptLanguage)
                    .post(formBody)
                    .get()
                    .build();
            
            final Response response;
            
            synchronized (EmailService.class) {
                response = httpClient.newCall(request).execute();
            }
            
            final String changeIp = new String(response.body().bytes());
            
            log.info("chaneg ip: {}", changeIp);
            
        });
    }

    //==========================================================================
    //@Transactional(readOnly = true)
    public void validateGmailAccount(String email, String pass) {
        
        StmtProcessor.execute(() -> {
            
            if (lastChangeIp.plusMinutes(changeIpFreq).compareTo(LocalDateTime.now()) < 0)
            synchronized (EmailService.class) {
                changeIp();
            }
            
            final Response response;
            
            final String url = host + "/readMessages?email=" + email + "&pass=" + pass + "&proxy_host=" + proxyHost + "&proxy_port=" + proxyPort + "&proxy_user=" + proxyUser + "&proxy_pass=" + proxyPass;
            
            synchronized (EmailService.class) {
                
                StmtProcessor.sleep(3000);
                
                final OkHttpClient httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new UnzippingInterceptor())
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS).build();
                
                final RequestBody formBody = new FormBody.Builder()
                        .build();
                
                final Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", getUserAgent())
                        .addHeader("accept", acceptHeader)
                        .addHeader("accept-language", acceptLanguage)
                        .post(formBody)
                        .get()
                        .build();
                
                log.info("creating call: {}", url);
                response = httpClient.newCall(request).execute();
            }
            
            final ObjectMapper om = new ObjectMapper();
            final EmailEntry root = om.readValue((new String(response.body().bytes())), EmailEntry.class);
            
            StmtProcessor.ifNotNull(root.getError(), () -> {
                //final String errmsg = String.format(email + ": mail read error: %s \n %s", e, url);
                throw new RuntimeException(String.format(email + ": mail read error: %s \n %s", root.getError(), url));
            });
        });
    }

    //==========================================================================
    //@Transactional(readOnly = true)
    public ValidationCode getValidateCodeFromMail(Email email, LocalDateTime legalLetterDate) {
        
        return StmtProcessor.create(ValidationCode.class, or -> {

//            changeIp();
            or.setCode(Byte.valueOf("-2"));
            or.setMessage("Validation code is missing or invalid");
            or.setValidationCode(SysConst.EMPTY_STRING);
            
            int attemptNum = 0;
            final int attemptLimit = 10;
            boolean codeNotFound = true;
            final String currEmail = email.getEmailAddress();
            final long legalTimeStamp = NLS.localDateTime2long(legalLetterDate);
            
            log.info("{}: legalTimeStamp = {}", currEmail, legalTimeStamp);
            
            final String url = host + "/readMessages?email=" + currEmail + "&pass=" + email.getPwd() + "&proxy_host=" + proxyHost + "&proxy_port=" + proxyPort + "&proxy_user=" + proxyUser + "&proxy_pass=" + proxyPass;
            
            do {
                
                attemptNum++;
                
                final Response response;

                //restricted access
                synchronized (EmailService.class) {
                    
                    StmtProcessor.sleep(pauseLength);
                    
                    log.info("{}: attempt {}: reading mail {}:{} \n {}", currEmail, attemptNum, currEmail, email.getPwd(), url);
                    
                    final RequestBody formBody = new FormBody.Builder()
                            .build();
                    final Request request = new Request.Builder()
                            .url(url)
                            .addHeader("User-Agent", getUserAgent())
                            .addHeader("accept", acceptHeader)
                            .addHeader("accept-language", acceptLanguage)
                            .post(formBody)
                            .get()
                            .build();
                    
                    final OkHttpClient httpClient = new OkHttpClient.Builder()
                            .addInterceptor(new UnzippingInterceptor())
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS).build();
                    
                    log.info("creating call: {}", url);
                    response = httpClient.newCall(request).execute();
                    
                    final ObjectMapper om = new ObjectMapper();
                    final EmailEntry root = om.readValue((new String(response.body().bytes())), EmailEntry.class);
                    
                    if (StmtProcessor.notNull(root.getError())) {
                        final String errMsg = currEmail + ": mail read error: " + root.getError();
                        or.setMessage(errMsg);
                        log.error(errMsg);
                        continue;
                    }
                    
                    final List<Message> mailBoxMessages
                            = root.getMessages()
                                    .stream()
                                    // only newest messages
                                    .filter(m -> m.timestamp >= legalTimeStamp)
                                    .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                                    .collect(Collectors.toList());
                    
                    if (StmtProcessor.isNull(mailBoxMessages)) {
                        final String errMsg = currEmail + ": mail list is empty";
                        or.setMessage(errMsg);
                        log.warn(errMsg);
                        continue;
                    }
                    
                    log.info("{}: {} new message(s)", currEmail, mailBoxMessages.size());
                    
                    if (mailBoxMessages.isEmpty()) {
                        continue;
                    }
                    
                    final String regex = "([0-9]{6}) is your Instagram code";
                    
                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    
                    for (final Message message : mailBoxMessages) {
                        
                        log.info(currEmail + ": process mail: {}", NLS.long2LocalDateTime(message.getTimestamp()).toString() + ": " + message.getTitle() + " " + message.getBodyTruncated());
                        
                        final Matcher matcher = pattern.matcher(message.getTitle());
                        
                        if (matcher.find()) {
                            
                            final String validationCode = matcher.group(1);
                            
                            log.info(currEmail + ": found validation code: {}", validationCode);

                            // old validation code
                            if (email.getLastKnownValidationCode().equals(validationCode)) {
                                log.warn(currEmail + ": found deprecated validation code: {}", validationCode);
                                continue;
                            }
                            
                            or.setCode(Byte.valueOf("0"));
                            or.setValidationCode(validationCode);
                            or.setMessage("Found matching validationCode");
                            codeNotFound = false;
                            email.setLastKnownValidationCode(validationCode);
                            break;
                        }
                    }
                }
                
            } while ((attemptNum <= attemptLimit) && codeNotFound);
            
            if (codeNotFound) {
                or.setCode(Byte.valueOf("-3"));
                final String errMsg = currEmail + ": validation code not found: " + or.getMessage();
                or.setMessage(errMsg);
                log.error(errMsg);

//                throw new RuntimeException(errMsg);
            } else {
                StmtProcessor.sleep(pauseLength);
            }
        });
    }
    //==========================================================================

    public Collection<Email> findActualEmails() {
        return emailRepo.findByEmailStatus(refsService.findEmailStatus(ES_ACTUAL));
    }

    //==========================================================================
    @Override
    public void initialize() {
        
        super.initialize();
    }

    //==========================================================================
    public Collection<String> buildFakedLogins(String mail, int count) {
        
        final int size = mail.length();
        
        final List<String> maillist = new ArrayList<>();
        
        for (int i = 0; i < (1 << (size - 1)); i++) {
            
            String newFakedName = new String(mail);
            for (int k = 0; k < size; k++) {
                
                if ((i & (1 << k)) != 0) {
                    
                    newFakedName = addChar(newFakedName, '.', size - k - 1);
                    
                } else {
                    
                }
            }
            
            maillist.add(newFakedName);
            if ((i) > count - 2) {
                break;
            }
        }
        
        return maillist;
    }
    
    private String addChar(String str, char ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

    //==========================================================================
    @Cacheable("emailByAddress")
    public Optional<Email> findEmail(String emailAddress) {
        
        return emailRepo.findByEmailAddress(emailAddress);
    }

    //==========================================================================
    public void saveAllEmails(Collection<Email> emails) {
        
        this.emailRepo.saveAll(emails);
    }
    
    @Transactional
    public void synchronizeRefs() {
        //======================================================================
        StmtProcessor.assertNotNull(Map.class, emailsList, "emailsList is not initialized");
        
        saveAllEmails(
                emailsList
                        .entrySet()
                        .stream()
                        .map(entry -> {
                            
                            final String emailInfo = entry.getValue();
                            
                            final String[] recs = emailInfo.split(":");
                            
                            final String mail = recs[0];
                            final String pwd = recs[1];
                            
                            return findEmail(mail)
                                    .orElseGet(() -> StmtProcessor.create(Email.class, record -> {
                                
                                record.setCreateDate(LocalDateTime.now());
                                record.setEmailAddress(mail);
                                record.setPwd(pwd);
                                record.setEmailStatus(refsService.findEmailStatus(ES_ACTUAL));
                                record.setAccountNotes(emailInfo);
                                
                                log.info("register new email account: {}, {}", mail, pwd);
                                
                            }));
                            
                        }).collect(Collectors.toList()));
        
        log.info("initialize emailsList: {} record(s)", emailsList.size());
        
    }
}
