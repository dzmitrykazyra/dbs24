package org.dbs24.tik.mobile.service;

public interface StringEncoder {

    String encode(String rawString);

    String decode(String encodedString);

    Integer decodeToInteger(String encodedString);

    Long decodeToLong(String encodedString);
}
