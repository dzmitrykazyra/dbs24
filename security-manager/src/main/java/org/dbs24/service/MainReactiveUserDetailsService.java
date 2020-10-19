/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import static org.dbs24.application.core.sysconst.SysConst.*;
import org.dbs24.application.core.log.LogService;
import org.dbs24.entity.security.ApplicationUser;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.*;
import reactor.core.publisher.Mono;
import org.dbs24.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class MainReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Value("${reactive.rest.debug:false}")
    private Boolean restDebug = BOOLEAN_FALSE;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    //==========================================================================
    final PasswordEncoder passwordEncoder;

    @Autowired
    public MainReactiveUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //==========================================================================
    @Override
    public Mono<UserDetails> findByUsername(final String userName) {

        if (restDebug) {
            LogService.LogInfo(this.getClass(), () -> String.format("Try 2 loggin '%s' [%s]",
                    userName,
                    passwordEncoder.encode(userName)));
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
    }
}
