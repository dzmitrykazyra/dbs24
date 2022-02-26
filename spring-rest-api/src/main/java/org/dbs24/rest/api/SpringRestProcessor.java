/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.rest.api;


import java.time.LocalDateTime;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
import org.dbs24.spring.core.api.AbstractApplicationService;

@Deprecated
public abstract class SpringRestProcessor extends AbstractApplicationService {

    public String receivePingMessage( String pingMessage) {
        return String.format("connection is successfull \n {'%s', %s, %s} ",
                pingMessage,
                this.getClass().getCanonicalName(),
                LocalDateTime.now());
    }
    //==========================================================================
}
