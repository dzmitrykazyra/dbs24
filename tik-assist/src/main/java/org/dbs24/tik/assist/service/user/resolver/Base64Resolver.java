package org.dbs24.tik.assist.service.user.resolver;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Log4j2
@Component
public class Base64Resolver {

    public String encodeStringWithBase64(String rawString) {

        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(rawString.getBytes());
    }

    public String decodeStringWithBase64(String encodedString) {

        return new String(
                Base64.getDecoder().decode(encodedString),
                StandardCharsets.UTF_8
        );
    }
}
