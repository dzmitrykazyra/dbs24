/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;

import org.dbs24.application.core.log.LogService;
import java.time.LocalDateTime;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
import org.dbs24.spring.core.api.ApplicationService;

/**
 *
 * @author Козыро Дмитрий
 */
//@RestController
//@CrossOrigin
//@RequestMapping(LogService.PATH_REST)
public abstract class SpringRestProcessor implements ApplicationService {

//    @GetMapping(LogService.PATH_4PING)
//    @ResponseStatus(HttpStatus.OK)
    public String receivePingMessage( String pingMessage) {
        return String.format("connection is successfull \n {'%s', %s, %s} ",
                pingMessage,
                this.getClass().getCanonicalName(),
                LocalDateTime.now());
    }
    //==========================================================================
}
