package org.dbs24.tik.mobile.service;

/**
 * Service for link expiration check
 */
public interface LinkExpirationService {

    /**
     * Checks the link to see if it needs to be updated
     *
     * @param link url for check
     * @return true if link need update, else - false
     */
    boolean checkExpiration(String link);
}
