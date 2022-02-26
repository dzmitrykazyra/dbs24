package org.dbs24.tik.mobile.service.impl;

import org.dbs24.tik.mobile.service.LinkExpirationService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LinkExpirationServiceImpl implements LinkExpirationService {

    private static final String EXPIRATION_TIME_REGEX = "(?<=x-expires=).*?(?=&|$)";
    private static final int TIME_BUFFER_IN_MINUTES = 30;

    @Override
    public boolean checkExpiration(String link) {
        String expirationTimeValue = extractTimestampFromLink(link);
        if (expirationTimeValue == null) {
            return true;
        }
        LocalDateTime timeForUpdate = LocalDateTime.now().plusMinutes(TIME_BUFFER_IN_MINUTES);
        long expirationTimeInMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(expirationTimeValue));
        Timestamp expirationTime = new Timestamp(expirationTimeInMillis);
        return expirationTime.before(Timestamp.valueOf(timeForUpdate));
    }

    private String extractTimestampFromLink(String cover) {
        if (cover == null) {
            return null;
        }
        Pattern p = Pattern.compile(EXPIRATION_TIME_REGEX);
        Matcher matcher = p.matcher(cover);
        return matcher.find() ? matcher.group() : null;
    }
}
