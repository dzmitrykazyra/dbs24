package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.tik.mobile.service.PasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PasswordServiceImpl implements PasswordService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PasswordServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public String encodePassword(String rawPassword) {

        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean isMatch(String rawPassword, String encodedPassword) {

        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
