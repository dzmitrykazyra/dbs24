package org.dbs24.tik.mobile.service;

public interface PasswordService {

    String encodePassword(String rawPassword);

    boolean isMatch(String rawPassword, String encodedPassword);
}
