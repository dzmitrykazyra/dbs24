/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.component;

import lombok.extern.log4j.Log4j2;
import org.dbs24.reactor.AbstractHotSubscriber;
import org.dbs24.rest.api.*;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class LoginReactor extends AbstractHotSubscriber<LoginResult> {

    @Override
    protected void processEvent(LoginResult loginResult) {
        log.debug("store user token in database");
    }
}
