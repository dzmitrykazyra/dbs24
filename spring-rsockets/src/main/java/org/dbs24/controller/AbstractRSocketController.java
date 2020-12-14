/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.controller;

import lombok.extern.log4j.Log4j2;
import org.dbs24.spring.core.api.AbstractApplicationBean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import reactor.core.publisher.Mono;

@Log4j2
public abstract class AbstractRSocketController extends AbstractApplicationBean {

}
