package org.dbs24.tik.assist.service.tiktok.resolver;

import org.dbs24.tik.assist.service.exception.CannotFindTiktokUsernameException;
import org.springframework.http.HttpStatus;

import java.net.MalformedURLException;
import java.net.URL;

public class TiktokLinkHelper {

    public static String extractUsernameFromLink(String weblink) {
        String username;

        try {
            URL url = new URL(weblink);
            String[] split = url.getPath().split("/");

            username = removeAtSymbol(split[1]);
        } catch (MalformedURLException e) {
            throw new CannotFindTiktokUsernameException(HttpStatus.BAD_REQUEST);
        }

        return username;
    }

    /**
     * Method allows get tiktok username without '@' if needed
     */
    public static String removeAtSymbol(String username) {

        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        return username;
    }
}
