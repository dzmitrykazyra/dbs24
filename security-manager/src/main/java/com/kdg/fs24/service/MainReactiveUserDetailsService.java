/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.service;

import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.entity.security.ApplicationUser;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.*;
import reactor.core.publisher.Mono;
import com.kdg.fs24.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.kdg.fs24.entity.security.ApplicationUserDetails;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Козыро Дмитрий
 */
@Service
public class MainReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Value("${reactive.rest.debug:false}")
    private Boolean restDebug = SysConst.BOOLEAN_FALSE;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(final String userName) {

        final PasswordEncoder encoder = passwordEncoder;

        if (restDebug) {
            LogService.LogInfo(this.getClass(), () -> String.format("Try 2 loggin '%s' [%s]",
                    userName,
                    encoder.encode(userName)));
        }

        final ApplicationUser applicationUser = applicationUserRepository.findByLogin(userName)
                .orElseThrow(() -> new RuntimeException(String.format("User/login not Found {%s}", userName)));

        if (restDebug) {
            LogService.LogInfo(applicationUser.getClass(), () -> String.format("Find user: %s:%s [%s]",
                    applicationUser.getApplicationUserDetails().getUsername(),
                    applicationUser.getApplicationUserDetails().getPassword(),
                    applicationUser.getApplicationUserDetails().getAuthorities()
                            .stream().findFirst().orElseThrow(() -> new RuntimeException("No role assigned!"))
            ));
        }

//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        final UserDetails userDetails = User.builder()
//                .passwordEncoder(encoder::encode)
//                .username(applicationUser.getLogin())
//                .password("8d0adcf-8e81-42b6-a")
//                .roles("ADMIN4")
//                .build();
        final UserDetails userDetails = applicationUser.getApplicationUserDetails();

        if (restDebug) {
            LogService.LogInfo(applicationUser.getClass(), () -> String.format("userDetails: %s:%s [%s]",
                    userDetails.getUsername(),
                    userDetails.getPassword(),
                    userDetails.getAuthorities().stream()
                            .findFirst().orElseThrow(() -> new RuntimeException("No role assigned!"))
            ));
        }

        return Mono.just(userDetails);

//        return Mono.just(au)
//                .map(ApplicationUser::getApplicationUserDetails);
    }

}
