/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.dbs24.consts.SysConst.BOOLEAN_FALSE;

@Service
public class MainReactiveUserDetailsService implements ReactiveUserDetailsService {

    @Value("${reactive.rest.debug:false}")
    private Boolean restDebug = BOOLEAN_FALSE;

    //==========================================================================
    final PasswordEncoder passwordEncoder;

    public MainReactiveUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //==========================================================================
    @Override
    public Mono<UserDetails> findByUsername(String userName) {


//        final ApplicationUser applicationUser = applicationUserRepository.findByLogin(userName)
//                .orElseThrow(() -> new RuntimeException(String.format("User/login not Found {%s}", userName)));
//        if (restDebug) {
//            LogService.LogInfo(applicationUser.getClass(), () -> String.format("Find user: %s:%s [%s]",
//                    applicationUser.getApplicationUserDetails().getUsername(),
//                    applicationUser.getApplicationUserDetails().getPassword(),
//                    applicationUser.getApplicationUserDetails().getAuthorities()
//                            .stream().findFirst().orElseThrow(() -> new RuntimeException("No role assigned!"))
//            ));
//        }
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        final UserDetails userDetails = User.builder()
//                .passwordEncoder(encoder::encode)
//                .username(applicationUser.getLogin())
//                .password("8d0adcf-8e81-42b6-a")
//                .roles("ADMIN4")
//                .build();
//        final UserDetails userDetails = applicationUser.getApplicationUserDetails();
//        if (restDebug) {
//            LogService.LogInfo(applicationUser.getClass(), () -> String.format("userDetails: %s:%s [%s]",
//                    userDetails.getUsername(),
//                    userDetails.getPassword(),
//                    userDetails.getAuthorities().stream()
//                            .findFirst().orElseThrow(() -> new RuntimeException("No role assigned!"))
//            ));
//        }
        final UserDetails userDetails = null;

        return Mono.just(userDetails);
    }
}
