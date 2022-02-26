package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.service.StringEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Log4j2
public class Base64StringEncoder implements StringEncoder {

    @Override
    public String encode(String rawString) {

        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(
                        rawString.getBytes()
                );
    }

    @Override
    public String decode(String encodedString) {

        return new String(
                Base64.getDecoder().decode(encodedString),
                StandardCharsets.UTF_8
        );
    }

    @Override
    public Integer decodeToInteger(String encodedString) {

        return Integer.valueOf(new String(
                Base64.getDecoder().decode(encodedString),
                StandardCharsets.UTF_8
        ));
    }

    @Override
    public Long decodeToLong(String encodedString) {

        return Long.valueOf(
                new String(
                        Base64.getDecoder().decode(encodedString),
                        StandardCharsets.UTF_8
                )
        );
    }
}
